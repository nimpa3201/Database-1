package hello.jdbc.service;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 *
 */

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

    //private final DataSource dataSource;  커넥션 획득하는 방법 추상화한 인터페이스

    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;


    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try{
            // 비지니스 로직
            bizLogic(fromId, toId, money);
            //커밋
            transactionManager.commit(status);

        } catch (Exception e){
            transactionManager.rollback(status); // 실패시 롤백 후 종료
            throw new IllegalStateException(e);
        }

    }   // 문제 없으면 커밋, 있으면 롤백 (트랜잭션 종료)

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById( fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney()- money);

        validation(toMember); // 검증에서 문제 생기면 예외 발생

        memberRepository.update( toId, toMember.getMoney()+ money);
    }


    private void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }


}
