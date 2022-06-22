package hello.core.member;

public class MemberServiceImpl implements MemberService {

    // 이게 null이면 다른 메소드 사용 시 널포인터 예외 뜰 수 있으므로 구현체를 선택해줘야 한다.
    private final MemberRepository memberRepository;

    // 생성자 주입 방식으로 변경
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
    
    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
