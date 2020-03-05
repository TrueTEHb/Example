package servlete;

import model.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/")
public class MainServlete extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();
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
        req.getRequestDispatcher("PersonForm.jsp").forward(req, resp);
    }

    private void listUser(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {

        List<User> people = UserService.getInstance().getAllUsers();
        req.setAttribute("people", people);
        req.getRequestDispatcher("PersonList.jsp").forward(req, resp);
    }

    private void insertUser(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {

        String name = req.getParameter("name");
        String password = req.getParameter("password");
        Long money;

        money = req.getParameter("money") != null || !req.getParameter("money").isEmpty() ?
                Long.valueOf(req.getParameter("money")) : 0L;

        UserService.getInstance().addUser(new User(name, password, money));
        resp.sendRedirect("list");
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Long id = Long.valueOf(req.getParameter("id"));
        UserService.getInstance().deleteUser(id);
        resp.sendRedirect("list");
    }

    private void editUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Long id = Long.valueOf(req.getParameter("id"));
        User user = UserService.getInstance().getUser(id);
        req.setAttribute("user", user);
        req.getRequestDispatcher("PersonForm.jsp").forward(req, resp);
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Long id = Long.valueOf(req.getParameter("id"));
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        Long money = Long.valueOf(req.getParameter("money"));
        UserService.getInstance().updateUser(new User(id, name, password, money));
        resp.sendRedirect("list");
    }
}