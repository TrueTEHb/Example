package myApp.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        String path = redirectToURL(authentication);
        if (path.equals("/admin")) {
            httpServletResponse.sendRedirect("/hello");
        } else {
            httpServletResponse.sendRedirect("login");
        }
    }

    private String redirectToURL(Authentication authentication) {

        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority auth : authorities) {
            if (auth.getAuthority().equals("ADMIN")) {
                isAdmin = true;
            }
        }

        if (isAdmin) {
            return "/admin";
        } else {
            return "/user";
        }
    }
}