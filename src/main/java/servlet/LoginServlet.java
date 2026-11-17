package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.PlayerDAO;
import model.Player;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ログイン画面の表示
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
        dispatcher.forward(request, response);
    }

    // ログインボタンが押された時の処理
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String password = request.getParameter("password"); // パスワードも受け取る

        PlayerDAO dao = new PlayerDAO();
        Player player = dao.login(id, password);

        if (player != null) {
            // ログイン成功：セッションにプレイヤー情報と座標を保存
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", player);
            session.setAttribute("posX", player.getPosX());
            session.setAttribute("posY", player.getPosY());
            
            // （現状はオープニングはスキップして直接マップに行く設定にしています。必要に応じて OpeningServlet に変更してください）
            response.sendRedirect(request.getContextPath() + "/MapServlet");
        } else {
            // ログイン失敗：エラーメッセージを出してログイン画面に戻る
            request.setAttribute("errorMsg", "IDまたはパスワードが間違っています。");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
            dispatcher.forward(request, response);
        }
    }
}