package hello.login.logintest.controller;

import hello.login.logintest.domain.member.Member;
import hello.login.logintest.domain.member.memberRepository;
import hello.login.logintest.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class home {
    private final memberRepository memberRepository;
    private final SessionManager sessionManager;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false)
                            Long memberId, Model model) {
        if (memberId == null) {
            return "home";
        }
        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //직접 만든 세션 사용
//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
        //세션 관리자로 저장된 회원 정보 조회
        //Member로 캐스팅
        Member member = (Member) sessionManager.getSession(request);

        //로그인
        if (member == null) {
            return "home";
        }
        model.addAttribute("member", member);
        return "loginHome";
    }

    //서블릿 Http 세션 사용
//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {
        //getSession(true)를 사용하면 처음 들어온 사용자도 세션이 만들어지기 때문에 false로 받음
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }
        //Member로 타입 캐스팅
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    public static class SessionConst{
        public static final String LOGIN_MEMBER = "loginMember";
    }
    //@SessionAttribute 사용
    @GetMapping("/")
    public String homeLoginV3Spring(
            //@SessionAttribute 사용
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
