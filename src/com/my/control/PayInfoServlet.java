package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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

public class PayInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		HttpSession session = request.getSession();
		String studentEmail = (String) session.getAttribute("loginInfo");
		PrintWriter out = response.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		String lessonId1 = (String)request.getParameter("lessonId");
		int lessonId = Integer.parseInt(lessonId1);
		ILessonService service = new LessonService();
		IStudentService studentservice = new StudentService();
		Lesson lesson = new Lesson();
		Student student = new Student();
		try {
			lesson = service.findById(lessonId);
			student = studentservice.findByEmail(studentEmail);
		} catch (FindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<>();
		//강좌이름, 토탈가격, 퍼센트, 남은기간, 학생이름, 연락처, 이메일
		map.put("lessonName", lesson.getLessonName());
		map.put("lessonTotalFee", lesson.getLessonTotalFee());
		map.put("targetPercent", lesson.getTargetPercent());
		map.put("diffDays", lesson.getDiffDays());
		map.put("studentName", student.getStudentName());
		map.put("studentPhone", student.getStudentPhone());
		map.put("studentEmail", student.getStudentEmail());
		String jsonStr = mapper.writeValueAsString(map);
		System.out.println(jsonStr);
		out.print(jsonStr);
	}
}
