package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.service.StudentService;
import com.my.vo.Student;

/**
 * 마이페이지 - 내정보 조회 및 내정보 수정 메소드 
 * @author 김재경
 *
 */

public class MyPageMyInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** 마이페이지- 내정보 리스트 불러오기
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		//1. 세션 객체 얻기
		HttpSession session = request.getSession();
		
		//2. HttpSession 객체의 속성으로 상태 정보값 가져오기
		String curStudentEmail = (String)session.getAttribute("loginInfo");
		
		//3. loginInfo속성의 이메일값을 이용하여, 학생 객체를 찾아서 출력
		if(curStudentEmail != null) {			
			StudentService service = new StudentService();
			try {
				Student s = service.findByEmail(curStudentEmail); //intstudentId);
				out.print("{\"student_email\": \""+s.getStudentEmail()+"\", \"student_name\": \""+s.getStudentName()+"\", \"student_phone\": \""+s.getStudentPhone()+"\"}");
//				out.print("{\"status\":1}");
			} catch (FindException e) {
				e.printStackTrace();
//				out.print("{\"status\":-1}");
			}
		}else {
//			out.print("\"status\":-1");
		}
	}

	/**마이페이지 - 내 정보 수정
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String inputPwd = request.getParameter("student_inputpwd");
		String modifyPwd = request.getParameter("student_pwd");
		String modifyPhone = request.getParameter("student_phone");
		String opt = request.getParameter("opt");
		
		StudentService service = new StudentService();
		HttpSession session = request.getSession();
		String curStudentEmail = (String)session.getAttribute("loginInfo");
		
//		System.out.println("요청시작");
//		System.out.println(opt);
//		System.out.println(inputPwd);
		if(opt !=null) {
			//처음으로비밀번호 확인
//			System.out.println("비밀번호확인");
			try {
				Student s = service.findByPwd(inputPwd);
				out.print("{\"status\": 1}");
				System.out.println(s);
			} catch (FindException e) {
				out.print("{\"status\": -1}");
			}
		}else { //비밀번호 및 연락처 변경
			try {
				Student s = service.findByEmail(curStudentEmail);
				Student s1 = new Student(curStudentEmail,modifyPwd, modifyPhone);
				service.modify(s1,inputPwd);
				out.print("{\"status\": 2}");
				session.removeAttribute("loginInfo");
			} catch (FindException e) {
				out.print("{\"status\": -2}");
			} catch (ModifyException e) {
				out.print("{\"status\": -2}");
			}
		}
	}

}
