package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.dao.CartDAO;
import com.my.dao.CartDAOOracle;
import com.my.exception.AddException;
import com.my.exception.DeleteException;
import com.my.exception.FindException;
import com.my.service.CartService;
import com.my.service.ICartService;
import com.my.service.ILessonService;
import com.my.service.StudentService;
import com.my.vo.Cart;
import com.my.vo.Lesson;
import com.my.vo.Student;

//import sun.security.action.GetIntegerAction;


public class MyPageHeartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession();
		String studentEmail = (String) session.getAttribute("loginInfo");
//		String studentEmail ="afautly7@slashdot.org"; 
//		String studentEmail = "uvongrollmann8@sohu.com";
		StudentService studentservice = new StudentService();
		CartService service = new CartService();
		
		int totalData = 0;
		int totalPage = 0;
		int startPage = 0;
		int endPage = 0;
		int  dataPerPage = 3;                            //dataPerPage: 페이지 당 데이터 수량
		int currPage = 1;                                //currpage : 현재 페이지
		String cp = request.getParameter("current_page");  
		if (cp != null && cp != " " ) {
			currPage = Integer.parseInt(cp);
		}
			
		try {
			Student student = studentservice.findByEmail(studentEmail);
			
			int studentId = student.getStudentId();
			totalData = service.findAllCount(studentId);
			List<Lesson> lesson = service.findByPage(currPage, dataPerPage, studentId);
			request.setAttribute("mypageheartlist", lesson);
			
			ObjectMapper mapper = new ObjectMapper();
			List<Map<String,Object>> list = new ArrayList<>();
			for(Lesson lessons: lesson ) {
				totalPage  = (int) Math.ceil((double)totalData/dataPerPage);
				startPage = ((currPage-1)/dataPerPage)*dataPerPage+1;
				endPage = startPage + (dataPerPage -1);
				
				if (endPage > totalPage) {
					endPage = totalPage;
				}
				System.out.println(lessons);
				Map<String,Object> map = new HashMap<>();
				map.put("lesson_id", lessons.getLessonId());
				map.put("lesson_name",lessons.getLessonName()); 
				map.put("lesson_end_date", lessons.getLessonEnd());
				map.put("lesson_description", lessons.getLessonDescription());
				map.put("lesson_total_amount", lessons.getLessonTotalFee());
				map.put("lesson_target_amount", lessons.getLessonTargetFee());
				map.put("lesson_target_percent",lessons.getTargetPercent());
				map.put("lesson_diff_days", lessons.getDiffDays());
			
				
				map.put("total_page",Integer.toString(totalPage));
				map.put("start_page",Integer.toString(startPage));
				map.put("end_page", Integer.toString(endPage));

				list.add(map);
			}
			System.out.println(mapper.writeValueAsString("servlet :" + list));
			out.print(mapper.writeValueAsString(list));
		} catch (FindException e) {
	
			out.print("{\"status\":1}");
			e.printStackTrace();
			//out.print("{\"status\":-1}");
		}	
	}
}