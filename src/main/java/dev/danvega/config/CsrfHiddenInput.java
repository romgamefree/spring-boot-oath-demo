package dev.danvega.config;

import gg.jte.Content;
import gg.jte.TemplateOutput;
import org.springframework.security.web.csrf.CsrfToken;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CsrfHiddenInput implements Content {

    private final CsrfToken csrfToken;

    public CsrfHiddenInput(CsrfToken csrfToken) {
        this.csrfToken = csrfToken;
    }

    public static CsrfHiddenInput get() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            HttpServletRequest req = sra.getRequest();
            CsrfToken token = (CsrfToken) req.getAttribute(CsrfToken.class.getName());
            return new CsrfHiddenInput(token);
        }
        return new CsrfHiddenInput(null);
    }

    @Override
    public void writeTo(TemplateOutput templateOutput) {
        if (this.csrfToken != null) {
            templateOutput.writeContent("<input type=\"hidden\" name=\"%s\" value=\"%s\">"
                    .formatted(csrfToken.getParameterName(), csrfToken.getToken()));
        }
    }
}
