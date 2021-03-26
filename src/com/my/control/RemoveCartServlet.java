package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.my.exception.DeleteException;
import com.my.service.CartService;
import com.my.service.ICartService;

public class RemoveCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		HttpSession session = request.getSession();
		ICartService service = new CartService();
		int lessonId = (int) session.getAttribute("lesson"); //"lesson"의 value가 객체일 경우 .getLessonId
		int studentId = (int) session.getAttribute("Student"); //위와 동일
		PrintWriter out = response.getWriter();
		try {
			service.remove(lessonId, studentId);
			out.print("{\"status\" : 1}");
		} catch (DeleteException e) {
			out.print("{\"status\" : -1, \"msg\": " + e.getMessage() + "}");
		}
	}
}
