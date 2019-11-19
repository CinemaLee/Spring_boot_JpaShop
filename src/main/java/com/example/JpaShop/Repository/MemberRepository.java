package com.example.JpaShop.Repository;

import com.example.JpaShop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository // 얘가 있어야 스프링 빈에 등록이되서 관리가 들어감.
public class MemberRepository  {

    @PersistenceContext // 이게 있으면 엔티티메니저 팩토리 따로 생성할 필요 없이, 스프링이 엔티티 메니저를 주입해줌.
    private EntityManager em;

    public void save(Member member) {
        em.persist(member);

    }

    public Member findOne(Long id){

        return em.find(Member.class, id);
    }


    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name",name) // 파라미터 바인딩.
                .getResultList();
    }

}
