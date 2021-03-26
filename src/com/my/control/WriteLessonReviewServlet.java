package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.service.ILPSService;
import com.my.service.ILessonReviewService;
import com.my.service.IStudentService;
import com.my.service.LPSService;
import com.my.service.LessonReviewService;
import com.my.service.StudentService;
import com.my.vo.LPS;
import com.my.vo.LessonReview;
import com.my.vo.Student;

public class WriteLessonReviewServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		Date reviewDate = new Date();
		IStudentService sservice = new StudentService();
		String reviewContent = request.getParameter("reviewcontent");
		//추천,비추천 안눌렀을때 스테이터스 필요
		int recommend = Integer.parseInt(request.getParameter("reviewrecommend"));
		System.out.println("인트타입 recommend" + recommend);
		System.out.println("스트링타입 recommend" + request.getParameter("reviewrecommend"));
		HttpSession session = request.getSession();
		int lessonId = Integer.parseInt(request.getParameter("lessonId"));
//		System.out.println("강좌아이디 받나?" + lessonId);
		String studentEmail = (String) session.getAttribute("loginInfo");
		if (studentEmail != null) {
			int studentId = 0;
			try {
				Student s = sservice.findByEmail(studentEmail);
				studentId = s.getStudentId();
			} catch (FindException e1) {
				e1.printStackTrace();
			}
	//		int lessonId = 7; //나중에 주석처리해야함
	//		int studentId = 9;
			ILessonReviewService service = new LessonReviewService();
			ILPSService lservice = new LPSService();
			LessonReview review = new LessonReview();
			try {                   
				LPS lps = lservice.findByStudentLessonId(lessonId, studentId);
				review.setReviewDate(reviewDate);
				review.setLps(lps);
				System.out.println(lps.getLPSId());
				review.setReviewContent(reviewContent);
				review.setRecommend(recommend);
				if(lps.getLPSId() != 0) {
					if(service.findByLPSId(lps.getLPSId()).getReviewId() == 0) {
//						if() {
							try { //성공적으로 리뷰가 등록된경우
								service.add(review);
								out.print("{\"status\" : 1}");
							} catch (AddException e) { //리뷰등록중 예외가 발생한경우
								out.print("{\"status\" : -1, \"msg\": " + e.getMessage() + "}");
							}
//						}else {
//							out.print("{\"status\" : -4}");
//						}
					} else { //수강등록 했지만 이미 리뷰를 작성한경우
						out.print("{\"status\" : -3}");
					}
				}else { //수강등록을 안한경우
					out.print("{\"status\" : -2}");
				}
			} catch (FindException e) { //lessonId와 studentId에 해당하는 LPS가 없을경우
				e.printStackTrace();
			}
		}else { //로그인이 안된 경우
			out.print("{\"status\" : 0}");
		}
	}
}