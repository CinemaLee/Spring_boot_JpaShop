package com.example.JpaShop.controller;

import com.example.JpaShop.domain.Address;
import com.example.JpaShop.domain.Member;
import com.example.JpaShop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    // final 키워드는 엔티티를 한 번만 할당합니다. 즉, 두 번 이상 할당하려 할 때 컴파일 오류가 발생하여 확인이 가능합니다.
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }



    @PostMapping // 요기도 마찬가지 Member엔티티를 바로 받지말고 MemberForm,MemberRequest같은 (DTO)를 전용으로 만들어서 분리해주는게 후에 유지보수에 너무 좋다.
    public String create(@Valid MemberForm memberForm, BindingResult result) { // @Valid에서 오류가 있으면 result에 담겨서 전달이 옴.

        if (result.hasErrors()) { // 에러가 있다면~
            return "members/createMemberForm";
        }


        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/"; // 재로딩하면 안좋기 때문에 리다이렉트로 한번에 넘김.

    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers(); // 예제가 단순하기 때매 엔티티를 그냥 뿌려줫지만, 실무에서는 MemberResponse를(DTO) 만들어서 view전용으로 만들자.
        model.addAttribute("members",members);
        return "members/memberList";
    }

}
