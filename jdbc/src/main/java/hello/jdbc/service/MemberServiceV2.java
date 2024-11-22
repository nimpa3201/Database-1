package hello.jdbc.service;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 -  파라미터 연동 , 풀을 고려한 종료
 *
 */

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource; // 커넥션 획득하는 방법 추상화한 인터페이스

    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();

        try{
            con.setAutoCommit(false); // 트랜잭션 시작 !!!!!!!!!!!

            // 비지니스 로직

            bizLogic(fromId, toId, money, con);

            con.commit(); // 성공시  커밋 후 종료


        } catch (Exception e){
            con.rollback(); // 실패시 롤백 후 종료
            throw new IllegalStateException(e);
        }finally {
            release(con);
        }
    }   // 문제 없으면 커밋, 있으면 롤백 (트랜잭션 종료)

    private void bizLogic(String fromId, String toId, int money, Connection con) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney()- money);

        validation(toMember); // 검증에서 문제 생기면 예외 발생

        memberRepository.update(con, toId, toMember.getMoney()+ money);
    }


    private void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }

    private void release(Connection con) {
        if ( con != null){
            try{
                con.setAutoCommit(true); // 커넥션이 풀에 반환될떄 자동 커밋으로 되돌림
            }catch (Exception e){
                log.info("error",e);
            }
        }
    }

}
