package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController // @Controller랑 @Responsebody를 합친 것
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // v1이 주는 유일한 장점은 클래스를 하나 더
    // 화면 validation, presentation을 위한 계층 로직이 모두 엔티티에 들어가있다는 문제점이 있다.
    // 그리고 엔티티의 멤버 이름이 바뀌면 API 스펙이 바뀌어버리는 문제점도 있다.
    // 엔티티는 굉장히 여러 곳에서 사용하기 때문에 자주 바뀌는데, API 스펙이 자주 바뀌어서는 곤란하다.
    // 따라서 API 스펙을 위한 별도의 DTO가 필요하다.
    // 항상 API를 만들 때는 엔티티를 파라미터에 받지 말고, 외부에 노출해서도 안된다.
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
