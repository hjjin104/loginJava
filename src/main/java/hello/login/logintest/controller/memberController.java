package hello.login.logintest.controller;


import hello.login.logintest.domain.member.Member;
import hello.login.logintest.domain.member.memberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor //의존성 주입
@RequestMapping("/members") //("/member") 주소에 공통으로 연결, 뒤에 추가적으로 url을 붙이고 싶으면 ("/url주소명")을 써주면 됨
public class memberController {

    private final memberRepository memberRepository;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member){
        return "members/addMemberForm.html";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult){
        //Spring MVC에서 @ModelAttribute를 메소드의 파라미터로 사용할 경우, 프로그램이 어떤 식으로 돌아가는지를 정리하고자 함
        //html input 값들이 각각 member 클래스의 id, password로 setter를 통해 바인딩 됨
        //@ModelAttribute 어노테이션이 붙은 객체가(member객체) 자동으로 Member 객체에 추가되고 뷰단으로 전달
        if (bindingResult.hasErrors()){
            return "members/addMemberForm.html";
        }
        else memberRepository.save(member);
        return "redirect:/";
    }

}
