package bnilive.bnishop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bnilive.bnishop.domain.Member;
import bnilive.bnishop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired
  private MemberService memberService;

  @Autowired
  private MemberRepository memberRepository;

  @Test
  void 회원가입() throws Exception {
    // region given
    Member member = new Member();
    member.setName("kim");
    // endregion

    // region when
    Long saveId = memberService.join(member);
    // endregion

    // region then
    assertEquals(member, memberRepository.findOne(saveId));
    // endregion
  }

  @Test
  void 중복_회원_예외() throws Exception {
    // region given
    Member member1 = new Member();
    member1.setName("kim");

    Member member2 = new Member();
    member2.setName("kim");

    Exception expectedException = new IllegalStateException("이미 존재하는 회원입니다.");
    // endregion

    // region when
    memberService.join(member1);
    Exception actualException = assertThrows(IllegalStateException.class,
        () -> memberService.join(member2));
    // endregion

    // region then
    assertEquals(expectedException.getMessage(), actualException.getMessage());
    // endregion
  }
}