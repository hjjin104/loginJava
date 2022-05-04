package hello.login.logintest.controller;

import hello.login.logintest.domain.login.loginService;
import hello.login.logintest.domain.member.Member;
import hello.login.logintest.web.login.loginForm;
import hello.login.logintest.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class loginController {
    private final loginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") loginForm form){
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute loginForm form, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getLoginPassword()); //loginForm에 들어온 loginId와 loginPassword값
        log.info("login? {}", loginMember);
        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        //로그인 성공처리

        //쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);
        //cookie 생성로직
//        로그인에 성공하면 쿠키를 생성하고 HttpServletResponse에 담는다.
//        쿠키 이름은 memberId이고, 값은 회원의 id를 담아둔다.
//        웹 브라우저는 종료 전까지 회원의 id를 서버에 계속 보낸다.

        return "redirect:/";
    }

    //직접 만든 세션 사용
    //@PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute loginForm form, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getLoginPassword());

        if(loginMember ==null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        //로그인 성공 처리

        //세션 관리자를 통해 세션을 생성하고, 회원데이터 보관
        sessionManager.createSession(loginMember, response);
        return "redirect:/";
    }

    //서블릿 Http 세션 사용
    //@PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute loginForm form, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getLoginPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성해서 반환
        //getSession():디폴트가 true, false는 세션이 없을 때 새로 만들지 않고 null 반환
        HttpSession session = request.getSession();

        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV4(@Valid @ModelAttribute loginForm form, BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL,
                          HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(),form.getLoginPassword());

        if(loginMember ==null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        //로그인 성공처리
        //세션이 있으면 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "rediredt:"+redirectURL;

    }

    //@PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    //직접 만든 세션 사용
    //@PostMapping("/logout")
    public String logoutV2(HttpServletRequest request){
        //세션 만료 적용
        sessionManager.expire(request);
        return "redirect:/";
    }

    //서블릿 Http 세션 사용
    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request){
        //getSession(false)를 사용해야 함 (세션이 없더라도 새로 생성하면 안되기 때문)
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";
    }
    private void expireCookie(HttpServletResponse response,String cookieName){
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static class SessionConst{
        public static final String LOGIN_MEMBER = "loginMember";
    }
}
