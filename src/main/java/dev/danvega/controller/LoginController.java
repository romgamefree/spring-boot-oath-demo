package dev.danvega.controller;

import dev.danvega.common.web.FlashMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model, String error, String logout) {

        if (error != null) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "Invalid username or password");
            FlashMessage.error("Tên đăng nhập hoặc mật khẩu không đúng").addTo(model);
        }

        if (logout != null) {
            FlashMessage.info("Bạn đã đăng xuất thành công").addTo(model);
        }

        return "pages/login";
    }
}
