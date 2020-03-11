package servlete;

import model.User;
import service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"}, servletNames = "/MainServlet")
public class ServletFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String servletPath = req.getServletPath();
        HttpSession session = req.getSession(false);
        String constPath = (String) (session != null ? session.getAttribute("constPath") : null);

        String name = req.getParameter("name");
        String password = req.getParameter("password");

        User loginedUser = (User) (session != null ? session.getAttribute("loginedUser") : null);


        /**
         * выполнить проверку роли на доступ к запрашиваемой странице:
         *  направить на страницу ошибки, если
         *  пользователь вошёл под ролью user, но пытается получить доступ к ресурсам admin'а
         *  */
        if (constPath != null && constPath.equals("/user") && servletPath.equals("/admin")) {
            req.setAttribute("error", "Access denied. No permissions.");
            RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/error.jsp");
            dispatcher.forward(req, resp);
            return;
        } else {
            /**
             * открыть страницу авторизации при запуске приложения
             * если в сесии нет данных о пользователи или пути для запросов
             */
            if (servletPath.equals("/")) {
                RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/login.jsp");
                dispatcher.forward(req, resp);
                return;
            } else if (loginedUser == null || constPath == null) {
                /**
                 * регистрация нового пользователя
                 */
                if (servletPath.equals("/new")) {
                    RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/personForm.jsp");
                    dispatcher.forward(req, resp);
                    return;
                } else if (servletPath.equals("/insert")) {
                    ServletContext context = req.getServletContext();
                    RequestDispatcher dispatcher = context.getRequestDispatcher("/insert");
                    dispatcher.forward(req, resp);
                    return;
                }
                /**
                 * валидация
                 * записать в сессию данные пользователя и установить адресс для разрешенных запросов
                 * **/
                if ((name != null & password != null) & (UserService.getInstance().isExistUser(name, password))) {
                    User userByNamePass = UserService.getInstance().getUserByNamePass(name, password);
                    String allowPath = "/" + userByNamePass.getRole();
                    String newPath = allowPath + servletPath;

                    session.setAttribute("loginedUser", userByNamePass);
                    session.setAttribute("role", userByNamePass.getRole());
                    session.setAttribute("constPath", allowPath);
                    resp.sendRedirect(newPath);
                    return;
                } else {
                    RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/login.jsp");
                    dispatcher.forward(req, resp);
                    return;
                }
                /**
                 * Данные пользователя записаны в сессии
                 * Обработка запросов редактирования, удаления
                 * id хранит значение авторизованного пользователя
                 * edId хранит значение пользователя, которого нужно изменить
                 */
            } else if (!constPath.equals(servletPath)) {
                Long id = loginedUser.getId();
                Long edId;

                if (req.getParameter("id") != null) {
                    edId = Long.valueOf(req.getParameter("id"));
                    session.setAttribute("etId", edId);
                } else {
                    session.setAttribute("etId", id);
                }
                String allowPath = (String) session.getAttribute("constPath");
                String newPath = allowPath + servletPath;
                req.getSession().setAttribute("id", id);
                resp.sendRedirect(newPath);
                return;
            }
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}