package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.service.ILPSService;
import com.my.service.LPSService;
import com.my.vo.LPS;

public class RegisterLessonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		HttpSession session = request.getSession();
		ILPSService service = new LPSService();
		String lessonId1 = (String)request.getParameter("lessonId");
		int lessonId = Integer.parseInt(lessonId1);
		String studentId1 = (String)request.getParameter("studentId"); //위와 동일
		int studentId = Integer.parseInt(studentId1);
		PrintWriter out = response.getWriter();
		try {
			LPS lps = service.findByStudentLessonId(lessonId, studentId);
			System.out.println(lessonId + ":" + studentId);
			System.out.println(lps);
			if(lps.getLPSId() == 0) {
				service.add(lessonId, studentId);
				out.print("{\"status\" : 1}");
			}else {
				out.print("{\"status\" : 0}");
			}
		} catch (FindException e) {
			e.printStackTrace();
		} catch (AddException e) {
			out.print("{\"status\" : -1, \"msg\": " + e.getMessage() + "}");
		}
	}
}
