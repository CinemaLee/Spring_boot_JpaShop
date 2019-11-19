package com.example.JpaShop.api;

import com.example.JpaShop.domain.Member;
import com.example.JpaShop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MemberApiController {

    @Autowired
    private MemberService memberService;


    // 엔티티 직접 노출시 엔티티에 변경이 생기면 반환되는 json까지 다바뀌어버림 즉 api에 영향을 미침. 안된다. 치명적.
    @GetMapping("/api/v1/members") // 안좋은 방법, 엔티티를 직접 노출하지도 말고 아예 쓰질맙시다.
    public List<Member> membersV1() {
        return memberService.findMembers();
    }


    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> members = memberService.findMembers();


        List<MemberDto> collect = members.stream()
                .map(m -> new MemberDto(m.getName())) // 구성요소를 MemberDto타입으로 다 바꿔버림.
                .collect(Collectors.toList()); // -> [{"name":"a"},{"name":"b"}.....] 형태가 됨. 즉 List<MemberDto> 타입.


        return new Result(collect.size(), collect); // -> Result로 반환하고 타입은 List<MemberDto>. 한번 묶으면 { "data" : [{"name":"a"},{"name":"b"}.....] } 요런형태로 나옴. 좀 더 확장성에 좋아.
    }


    @Data
    @AllArgsConstructor
    private static class Result<T> {
        private int count;
        private T data;

    }


    @Data
    @AllArgsConstructor
    private static class MemberDto{
        private String name;
    }



    @PostMapping("/api/v1/members")  // Json으로 들어온 데이터를 Member에 쫘악 넣어줌. 안좋은 방법.
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")  // 별도의 dto를 만들어 받아줘야해
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }


    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());

        Member find = memberService.findOne(id);
        return new UpdateMemberResponse(find.getId(), find.getName());

    }


    /**
     * 막간 static 설명.
     * static 변수와 메서드는 인스턴스 생성없이 로딩됨과 동시에 메모리에 스스로 올라감.
     * 생성이 없고 주소값도 없다.
     * static 클래스를 만든다면?
     * 속도가 엄청 빨라짐. new없이 바로 쓸 수 있으니까!
     * 가비지 컬렉션의 대상이 아님. 남용시 메모리가 부족해질 수 도 있어! 누가 공유하는지 모르니 못지우지.
     *
     */

    @Data // 클래스를 공유하자는 뜻으로 static.
    private static class CreateMemberRequest {
        @NotBlank
        private String name;
    }

    @Data
    private static class CreateMemberResponse {

        @NonNull
        private Long id;


    }

    @Data // 클래스를 공유하자는 뜻으로 static.
    private static class UpdateMemberRequest {
        @NotBlank
        private String name;
    }

    @Data // 클래스를 공유하자는 뜻으로 static.
    @AllArgsConstructor
    private static class UpdateMemberResponse {
        private Long id;
        private String name;
    }


}
