package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController // @Controller랑 @Responsebody를 합친 것
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // members 정보만 가져오고 싶은데 엔티티에 포함되어 있는 orders 정보도 모두 나가는 등 의도하지 않은 정보도 나간다.
    // @JsonIgnore 어노테이션을 원하지 않는 필드에 붙이면 되긴 한다.
    // 회원과 관련된 조회 API가 하나가 아닐 텐데 어디는 address가 필요하고 어디는 orders가 필요하고 다를 수 있다.
    // 이럴 경우 엔티티를 변경하다 보면 답이 안나온다.
    // 또한 엔티티가 변경되면 API 스펙이 변하는 문제도 있다.
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

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

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request
    ) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
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
