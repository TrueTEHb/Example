package servlete;

import model.User;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "/MainServlet", urlPatterns = {"/insert", "/new", "/user/*", "/admin/*"})
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getPathInfo() == null ? req.getServletPath() : req.getPathInfo();
        try {
            switch (action) {
                case "/new":
                    showNewForm(req, resp);
                    break;
                case "/insert":
                    insertUser(req, resp);
                    break;
                case "/delete":
                    deleteUser(req, resp);
                    break;
                case "/edit":
                    editUser(req, resp);
                    break;
                case "/update":
                    updateUser(req, resp);
                    break;
                case "/logout":
                    logoutUser(req, resp);
                    break;
                default:
                    listUser(req, resp);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/personForm.jsp").forward(req, resp);
    }

    private void listUser(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {

        User loginedUser = (User) req.getSession().getAttribute("loginedUser");

        String role = loginedUser.getRole();
        String name = loginedUser.getName();
        String password = loginedUser.getPassword();
        User user = UserService.getInstance().getUserByNamePass(name, password);
        req.setAttribute("user", user);
        if (role.equals("admin")) {
            List<User> people = UserService.getInstance().getAllUsers();
            req.setAttribute("people", people);
            ServletContext context = req.getServletContext();
            RequestDispatcher dispatcher = context.getRequestDispatcher("/admin.jsp");
            dispatcher.forward(req, resp);
            return;
        } else if (role.equals("user")) {
            ServletContext context = req.getServletContext();
            context.getRequestDispatcher("/user.jsp").forward(req, resp);
            return;
        }
    }

    private void insertUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String name = req.getParameter("name");
        String password = req.getParameter("password");
        Long money = req.getParameter("money") != null || !req.getParameter("money").isEmpty() ?
                Long.valueOf(req.getParameter("money")) : 0L;
        UserService.getInstance().addUser(new User(name, password, money, null));
        resp.sendRedirect("/");
        return;
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        /**
         * Если юзер или админ удаляет свой аккаунт, то после удаления
         * закрыть сессию, вернуть начальную страницу
         *
         * Если админ удаляет чей-то аккаунт, то после удаления
         * вернуться на страницу админа
         */
        Long etId = (Long) req.getSession().getAttribute("etId");
        Long id = (Long) req.getSession().getAttribute("id");
        if (id == etId) {
            UserService.getInstance().deleteUser(etId);
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            ServletContext context = req.getServletContext();
            context.getRequestDispatcher("/login.jsp").forward(req, resp);
        } else {
            UserService.getInstance().deleteUser(etId);
            ServletContext context = req.getServletContext();
            RequestDispatcher dispatcher = context.getRequestDispatcher("/admin.jsp");
            dispatcher.forward(req, resp);
        }
    }

    private void editUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Long etId = (Long) req.getSession().getAttribute("etId");
        Long id = (Long) req.getSession().getAttribute("id");
        User user = (id == etId ? UserService.getInstance().getUser(id) : UserService.getInstance().getUser(etId));
        req.getSession().setAttribute("user", user);
        ServletContext context = req.getServletContext();
        context.getRequestDispatcher("/personForm.jsp").forward(req, resp);
        return;
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            ServletException, SQLException {

        /**
         * В случае изменения роли у авторизованного пользователя,
         * изменить разрешённые пути для обращения и направить
         * на новый адресс
         */
        Long etId = (Long) req.getSession().getAttribute("etId");
        Long id = (Long) req.getSession().getAttribute("id");
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        Long money = Long.valueOf(req.getParameter("money"));
        String role = req.getParameter("role");
        if (etId == id) {
            UserService.getInstance().updateUser(new User(id, name, password, money, role));
            if (!req.getSession().getAttribute("role").equals(role)) {
                req.getSession().setAttribute("role", role);
                req.getSession().setAttribute("constPath", "/" + role);
                User user = UserService.getInstance().getUserByNamePass(name, password);
                req.getSession().setAttribute("loginedUser", user);
            }
        } else {
            UserService.getInstance().updateUser(new User(etId, name, password, money, role));
        }

        if (req.getSession().getAttribute("role").equals("admin")) {
            resp.sendRedirect("/admin.jsp");
        } else {
            resp.sendRedirect("/user.jsp");
        }

        return;
    }

    private void logoutUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        ServletContext context = req.getServletContext();
        context.getRequestDispatcher("/login.jsp").forward(req, resp);
        return;
    }
}