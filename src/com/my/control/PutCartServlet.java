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
import com.my.service.CartService;
import com.my.service.ICartService;
import com.my.service.IStudentService;
import com.my.service.StudentService;
import com.my.vo.Cart;
import com.my.vo.Student;

public class PutCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		HttpSession session = request.getSession();
		IStudentService sservice = new StudentService();
		ICartService service = new CartService();
//		int lessonId = (int) session.getAttribute("lesson"); //"lesson"의 value가 객체일 경우 .getLessonId
		//강좌 아이디 받아오기
		String lessonId1 = request.getParameter("lessonId");
		int lessonId = Integer.parseInt(lessonId1);
		//학생 이메일 받아오기
		String studentEmail = (String) session.getAttribute("loginInfo"); //위와 동일
		System.out.println(studentEmail);
		PrintWriter out = response.getWriter();
		if (studentEmail != null) {
			try {
				Student s = sservice.findByEmail(studentEmail);
				int studentId = s.getStudentId();
				Cart cart = service.findById(lessonId, studentId);
				System.out.println(studentId);
				if(cart.getCartId() == 0) {
					try {
						service.add(lessonId, studentId);
					} catch (AddException e) {
						e.printStackTrace();
					}
					out.print("{\"status\" : 1}");
				}else if (cart.getCartId() != 0){
					out.print("{\"status\" : -2}");
				}
			} catch (FindException e) {
				out.print("{\"status\" : -1, \"msg\": " + e.getMessage() + "}");
			}
		}else {
			out.print("{\"status\" : 0}");
		}
	}
}
