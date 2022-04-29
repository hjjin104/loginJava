package hello.login.logintest.domain.login;

import hello.login.logintest.domain.member.Member;
import hello.login.logintest.domain.member.memberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class loginService {
    private final memberRepository memberRepository;

    public Member login(String userId, String userPassword){
        return memberRepository.findByLoginId(userId)
                .filter(member -> member.getUserPassword().equals(userPassword))
                .orElse(null);
    }
}
