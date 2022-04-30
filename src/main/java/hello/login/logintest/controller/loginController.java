package hello.login.logintest.controller;

import hello.login.logintest.domain.login.loginService;
import hello.login.logintest.domain.member.Member;
import hello.login.logintest.web.login.loginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class loginController {
    private final loginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") loginForm form){
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute loginForm form, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getLoginPassword());
        log.info("login? {}", loginMember);
        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);
        //cookie 생성로직
//        로그인에 성공하면 쿠키를 생성하고 HttpServletResponse에 담는다.
//        쿠키 이름은 memberId이고, 값은 회원의 id를 담아둔다.
//        웹 브라우저는 종료 전까지 회원의 id를 서버에 계속 보낸다.

        return "redirect:/";
    }

    public String logout(HttpServletResponse response){
        expireCookie(response, "memberId");
        return "redirect:/";
    }

}
