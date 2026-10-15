package dev.danvega.config;

import dev.danvega.common.web.FlashMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Map;

@Component
public class FlashMessageInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null && flashMap.get(FlashMessage.FLASH_ATTR) instanceof FlashMessage flash) {
            request.setAttribute(FlashMessage.FLASH_ATTR, flash);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            Object flash = modelAndView.getModel().get(FlashMessage.FLASH_ATTR);
            if (flash instanceof FlashMessage fm) {
                request.setAttribute(FlashMessage.FLASH_ATTR, fm);
                modelAndView.getModel().put("flash", fm);
            }
        }
    }
}
