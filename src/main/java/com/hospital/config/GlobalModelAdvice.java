package com.hospital.config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {
    @ModelAttribute("userRole")
    public String userRole(HttpSession session) {
        return (String) session.getAttribute("role");
    }
    @ModelAttribute("sessionPatientId")
    public Object sessionPatientId(HttpSession session) {
        return session.getAttribute("patientId");
    }
    @ModelAttribute("sessionPatientName")
    public Object sessionPatientName(HttpSession session) {
        return session.getAttribute("patientName");
    }
    /**
     * Exposes the current request URI as a plain model attribute.
     * Used by fragments/nav.html to highlight the active nav link.
     * This replaces the broken #httpServletRequest.requestURI expression
     * which requires Spring Security on the classpath.
     */
    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
