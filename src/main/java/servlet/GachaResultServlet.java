package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.GachaItem;

@WebServlet("/GachaResultServlet")
public class GachaResultServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // GachaExecuteServletで作られた結果リストを取得
        @SuppressWarnings("unchecked")
        List<GachaItem> resultList = (List<GachaItem>) session.getAttribute("gachaResult");

        // リクエストスコープにセットしてJSPへ渡す
        request.setAttribute("resultList", resultList);

        // 結果画面へフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/gacha_result.jsp").forward(request, response);
    }
}