package com.example.JpaShop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) { // model에 데이터를 담아서 view로 넘길 수 있다.
        model.addAttribute("data","helloWorld!!");
        return "hello"; // view 이름. hello.html
    }

}
