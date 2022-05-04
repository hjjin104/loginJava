package hello.login.logintest.domain.login;

import hello.login.logintest.domain.member.Member;
import hello.login.logintest.domain.member.memberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class loginService {
    // 가입을 하고 회원을 찾으려면 앞서 만든 MemberRepository 인터페이스가 필요함
    private final memberRepository memberRepository;


    //memberRepository에서 들어온 loginId와 password를 비교해서 일치하면 로그인
    public Member login(String loginId, String password){
        return memberRepository.findByLoginId(loginId)
                .filter(member -> member.getPassword().equals(password))
                .orElse(null);
    }
}
