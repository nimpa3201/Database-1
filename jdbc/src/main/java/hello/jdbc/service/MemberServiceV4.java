package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * 예외 누수 문제 해결
 * SQLException 제거
 * MemberRepository 인터페이스 의존
 */
@Slf4j
public class MemberServiceV4 {

    private final MemberRepository memberRepository;
    public MemberServiceV4(MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
    }

    @Transactional
    // 해당 메서드 호출 시 트랜잭션이 시작된다.
    // 메서드 실행이 성공하면 커밋되고, 실패(예외 발생) 시 롤백된다.
    public void accountTransfer(String fromId, String toId, int money)  {
                bizLogic(fromId, toId, money);
    }
    private void bizLogic(String fromId, String toId, int money)  {
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