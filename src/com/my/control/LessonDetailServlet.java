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
import com.my.service.ILessonService;
import com.my.service.IStudentService;
import com.my.service.LessonService;
import com.my.service.StudentService;
import com.my.vo.Lesson;
import com.my.vo.Student;

public class LessonDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		HttpSession session = request.getSession();
		String studentEmail = (String) session.getAttribute("loginInfo");
		PrintWriter out = response.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		
		//요청전달데이터 얻기
		String lessonId1 = (String)request.getParameter("lessonId");
//		System.out.println(lessonId1);
		int lessonId = Integer.parseInt(lessonId1);
//		System.out.println(lessonId);
		//비지니스로직 호출(가장 핵심 로직을 호출한다)
		ILessonService service = new LessonService();
		if(lessonId != 0) {
			try {
				Lesson l = service.findById(lessonId);
				//요청속성(속성명:"lesson" , 속성값: l)으로 추가
				request.setAttribute("lesson",l);
				String jsonStr = mapper.writeValueAsString(l);
				System.out.println(jsonStr);
				out.print(jsonStr);
			} catch (FindException e) {
				e.printStackTrace();
			}
		} else {
			
		}
	}
}
