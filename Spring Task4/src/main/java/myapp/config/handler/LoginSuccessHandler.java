package myapp.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
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
        /**
         * если залогинился админ - вернуть на админку (вывести список всех)
         * иначе - страницук пользователя
         * */

        String path = redirectToURL(authentication);
        if (path.equals("/admin")){
            httpServletResponse.sendRedirect(path);
            return;
        }else {
            httpServletResponse.sendRedirect(path);
            return;
        }
    }

    private String redirectToURL(Authentication authentication) {

        boolean isUser = false;
        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();


        for (GrantedAuthority auth : authorities) {
            if (auth.getAuthority().equals("USER")) {
                isUser = true;
                break;
            } else if (auth.getAuthority().equals("ADMIN")) {
                isAdmin = true;
                break;
            }
        }

        if (isAdmin) {
            return "/admin";
        } else if (isUser) {
            return "/user";
        } else {
            throw new IllegalStateException();
        }
    }

}