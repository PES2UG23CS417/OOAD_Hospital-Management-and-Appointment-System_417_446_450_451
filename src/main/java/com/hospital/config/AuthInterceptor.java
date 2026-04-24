package com.hospital.config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.List;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static final List<String> PUBLIC = List.of("/login", "/logout", "/error");

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String path = req.getRequestURI();
        if (PUBLIC.stream().anyMatch(path::startsWith)) return true;
        if (req.getSession().getAttribute("role") == null) {
            res.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
