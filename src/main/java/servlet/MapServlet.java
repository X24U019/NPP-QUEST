package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Player;

@WebServlet("/MapServlet")
public class MapServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Player player = (Player) session.getAttribute("loginUser");

        // 初期座標の設定（未設定の場合は X:13, Y:9）
        Integer posX = (Integer) session.getAttribute("posX");
        Integer posY = (Integer) session.getAttribute("posY");
        
        if (posX == null || posY == null) {
            if (player != null && player.getPosX() > 0) {
                posX = player.getPosX();
                posY = player.getPosY();
            } else {
                posX = 25;
                posY = 17;
            }
            session.setAttribute("posX", posX);
            session.setAttribute("posY", posY);
        }

        // マップ画面を表示
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/map.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}