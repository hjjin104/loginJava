package hello.login.logintest.hello.login;


import hello.login.logintest.domain.member.Member;
import hello.login.logintest.domain.member.memberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;
    private final memberRepository memberRepository;

    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 1000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));

        Member member = new Member();
        member.setUserId("test");
        member.setUserPassword("test!");
        member.setUserName("테스터");

        memberRepository.save(member);
    }
}
