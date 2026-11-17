package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.PlayerDAO;
import model.Player;

@WebServlet("/ChangeMapServlet")
public class ChangeMapServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        // ★ここを "player" から "loginUser" に修正
        Player player = (Player) session.getAttribute("loginUser");
        
        if (player == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        try {
            int nextMapId = Integer.parseInt(request.getParameter("map"));
            int nextX = Integer.parseInt(request.getParameter("x"));
            int nextY = Integer.parseInt(request.getParameter("y"));

            PlayerDAO dao = new PlayerDAO();
            dao.updateMapAndPosition(player.getId(), nextMapId, nextX, nextY);

            player.setMapId(nextMapId);
            player.setPosX(nextX);
            player.setPosY(nextY);

            session.setAttribute("mapId", nextMapId);
            session.setAttribute("posX", nextX);
            session.setAttribute("posY", nextY);

            // ★更新したプレイヤー情報を "loginUser" としてセッションに再セット
            session.setAttribute("loginUser", player);

            response.sendRedirect("MapServlet"); // map.jspではなくMapServlet経由に変更
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("MapServlet");
        }
    }
}