package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.IStudentService;
import com.my.service.StudentService;
import com.my.vo.Student;

public class PaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
//		String lessonId1 = request.getParameter("lessonId");
//		int lessonId = Integer.parseInt(lessonId1);
		String studentEmail = (String) session.getAttribute("loginInfo");
		ObjectMapper mapper = new ObjectMapper();
		IStudentService sservice = new StudentService();
		try {
			if(studentEmail != null) {
				Student s = sservice.findByEmail(studentEmail);
				String jsonStr = mapper.writeValueAsString(s);
				System.out.println(jsonStr);
				out.print(jsonStr);
			}else {
				out.print("{\"status\":0}");
			}
		} catch (FindException e) {
			e.printStackTrace();
			out.print("{\"status\":-1}");
		}
	}
}
