package dev.danvega.controller;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String error() {
        return "pages/error";
    }

    @GetMapping("/error/404")
    public String notFound() {
        return "pages/404";
    }

    @GetMapping("/error/500")
    public String serverError() {
        return "pages/500";
    }

    @GetMapping("/error/403")
    public String accessDenied() {
        return "pages/403";
    }
}
