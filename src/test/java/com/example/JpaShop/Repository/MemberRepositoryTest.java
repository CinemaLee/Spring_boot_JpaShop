package com.example.JpaShop.Repository;

import com.example.JpaShop.domain.Member;
import org.assertj.core.api.Assertions;
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
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional // EntityManager를 사용한 데이터 변경은 항상 트랜잭션 안에서. // 다른곳엣 있으면 상관없는데 test안에만 있으면 다 롤벡해버림.
    @Rollback(false)
    public void testMember() throws Exception {

        Member member = new Member();
        member.setName("memberA");

        memberRepository.save(member); // persist하면서 1차캐시에 저장.
        Member findMember = memberRepository.findOne(member.getId());

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
        Assertions.assertThat(findMember).isEqualTo(member); // 같다. 같은 트랜잭션안에서는 같은 영속성 컨텍스트를 쓴다. Id값이 같으면 같은 엔티티다.
        // select 쿼리조차 안나감. 1차캐시에서 바로 가져왔기 때문.


    }
}