package bnilive.bnishop;

import bnilive.bnishop.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception {
        // region given
        Member member = new Member();
        member.setUsername("memberA");
        // endregion

        // region when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);
        // endregion

        // region then
        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());

        assertEquals(findMember, member); //JPA 엔티티 동일성 보장
        // endregion
    }
}