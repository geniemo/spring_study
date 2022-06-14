package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    // /hello로 요청을 날리면 이 메소드를 호출함
    // model은 MVC 모델에서의 모델
    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        // 모델을 templates 폴더 내의 hello.html에 넘겨라
        return "hello";
    }

    // 쿼리 파라미터로 name을 전달받아야 한다.
    // 만약 파라미터가 전달되지 않아도 정상적으로 동작하도록 하려면
    // @RequestParam("name")를 @RequestParam(value = "name", required = false) 로 바꾸면 된다.
    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    // HTTP 패킷의 body 부분에 반환 내용을 넣어주겠다.
    // name에 spring이 들어있다면 hello spring이 클라이언트에 전달된다.
    @GetMapping("hello-string")
    @ResponseBody
    public String helloMvc(@RequestParam("name") String name) {
        return "hello " + name;
    }

    // name으로 spring을 전달했다면 {"name":"spring"}이 클라이언트에 전달된다.
    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    // static 클래스로 만들어서 HelloController클래스 내에서 바로 사용할 수 있게 함
    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
