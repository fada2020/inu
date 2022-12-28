package com.video.inu.controller;

import com.video.inu.dto.MemberForm;
import com.video.inu.dto.SessionUser;
import com.video.inu.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {
    @GetMapping("/")
    public String index( @SessionAttribute(value = Constant.USER, required = false) SessionUser sessionUser){

        return "index";
    }
    @GetMapping("/login")
    public String login(
            @SessionAttribute(value = Constant.USER, required = false) SessionUser sessionUser
            , Model model
    ){
        if (sessionUser != null) return "redirect:/";
        model.addAttribute("forms",new MemberForm());
        return "login";
    }
    @PostMapping("/login")
    public String login_process(
            @SessionAttribute(value = Constant.USER, required = false) SessionUser sessionUser
            , @ModelAttribute MemberForm forms
            , Model model
    ){
        System.out.println("forms = " + forms);
        if (sessionUser != null) return "redirect:/";

        return "redirect:/";
    }
    @GetMapping("/logout")
    public String logout( @SessionAttribute(value = Constant.USER, required = false) SessionUser sessionUser){

        return "redirect:/";
    }

    @GetMapping("/join")
    public String join( @SessionAttribute(value = Constant.USER, required = false) SessionUser sessionUser
        ,   Model model
    ){
        if (sessionUser != null) return "redirect:/";
        model.addAttribute("forms",new MemberForm());
        return "join";
    }
    @PostMapping("/join")
    public String join_process(
            @SessionAttribute(value = Constant.USER, required = false) SessionUser sessionUser
            , @ModelAttribute MemberForm forms
            ,   Model model
    ){
        System.out.println("forms = " + forms);
        if (sessionUser != null) return "redirect:/";
        return "redirect:/";
    }
    @GetMapping("/content")
    public String store( @SessionAttribute(value = Constant.USER, required = false) SessionUser sessionUser){

        return "content";
    }
}
