package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 지금 static 파일이 있지만 컨트롤러를 우선적으로 찾기 때문에 static 파일을 쓰지 않는다.
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
