package com.example.JpaShop.service;

import com.example.JpaShop.Repository.MemberRepository;
import com.example.JpaShop.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 테스트에 있을 때만 롤백해버림.
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
//    @Rollback(false) // 롤백하지 않겠다. 눈으로 확인할꺼얌.
    public void 회원가입() throws Exception {
        //given - 주어졌을때
        Member member = new Member();
        member.setName("kim");

        //when - 이렇게 하면
        Long savedId = memberService.join(member);

        //then - 이렇게 되야해
        // em.flush() -> 디비에 쿼리를 날리는걸 보여줌. 마지막에 롤백되버리긴 함.
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class) // 이 예외가.
    public void 중복_회원_예외() throws Exception{
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");


        //when
        memberService.join(member1);
        memberService.join(member2); // 예외가 터저야한다.

        //then
        fail("예외가 발생해야 한다."); // 코드가 여기까지 내려오면 안된다는 뜻.
    }
}