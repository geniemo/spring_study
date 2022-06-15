package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {

    private final MemberService memberService;

    // autowired를 하면 컨테이너가 컨트롤러 만들 때 컨테이너에서 멤버서비스 가져와서 씀
    // 서비스 클래스에는 @Service annotation을,
    // 리포지토리 클래스에는 @Repository annotation을 달아준다.
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
