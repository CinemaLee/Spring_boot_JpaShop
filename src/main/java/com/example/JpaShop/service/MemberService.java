package com.example.JpaShop.service;

import com.example.JpaShop.Repository.MemberRepository;
import com.example.JpaShop.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Ctrl + p => 속성 리스트 확인하기
@Transactional // JPA를 활용한 데이터변경은 무적권 트랜잭션안에서 이루어져야함. 그래야 LAZY로딩 등등 이루어짐.
public class MemberService { // 비즈니스 로직, 트랜잭션 처리

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 회원가입
     */

    public Long join(Member member){
        validateDuplicateMember(member); // 중복회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName()); // 멀티 스레드 상황을 고려하여 디비의 멤버 이름에 유니크 조건을 걸어주는게 사실 더 안전.
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }



    /**
     * 회원 전체 조회
      */

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }



    /**
     * 회원 단건 조회
      */
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }



    // 회원 수정
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name); // 변경감지. 따로 업데이트 쿼리를 안날려줘도 flush되면서 디비에 반영됨.
    }
}
