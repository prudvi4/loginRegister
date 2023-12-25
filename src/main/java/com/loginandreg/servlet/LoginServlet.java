package com.loginandreg.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RequestDispatcher dispatcher = null;
		String uemail = request.getParameter("username");
		String upwd = request.getParameter("password");
		Connection connection = null;
		String dbUrl = "jdbc:mysql://127.0.0.1:3306/?user=excelr";
		String user = "excelr";
		String password = "excelr@123";
		String GET_QRY = "SELECT * FROM login_reg.users WHERE uemail=? and upwd=?";
		HttpSession session = request.getSession();
		if (uemail == null || uemail.equals("")) {
			request.setAttribute("status", "invalidemail");
			dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
		}
		if (upwd == null || upwd.equals("")) {
			request.setAttribute("status", "invalidpassword");
			dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(dbUrl, user, password);
			PreparedStatement pst = connection.prepareStatement(GET_QRY);
			pst.setString(1, uemail);
			pst.setString(2, upwd);

			ResultSet resultSet = pst.executeQuery();
			if (resultSet.next()) {
				session.setAttribute("name", resultSet.getString("uname"));
				dispatcher = request.getRequestDispatcher("index.jsp");
			} else {
				request.setAttribute("status", "failed");
				dispatcher = request.getRequestDispatcher("login.jsp");
			}
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
