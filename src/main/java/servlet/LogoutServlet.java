package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 現在のセッションを取得（存在しなければnull）
        HttpSession session = request.getSession(false);
        if (session != null) {
            // セッションを破棄（ログイン情報をリセット）
            session.invalidate();
        }
        
        // ログイン画面へリダイレクト
        response.sendRedirect(request.getContextPath() + "/LoginServlet");
    }
}