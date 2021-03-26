package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.ILPSService;
import com.my.service.IStudentService;
import com.my.service.LPSService;
import com.my.service.StudentService;
import com.my.vo.Lesson;
import com.my.vo.Student;

public class MyPageLessonListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		LPSService lps = new LPSService();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		HttpSession session = request.getSession();
		String studentEmail = (String)session.getAttribute("loginInfo");
//		String studentEmail = "e13";
		StudentService service = new StudentService();
		Student student;
		try {
			student = service.findByEmail(studentEmail);
			
			int currPage = 1; //현재페이지
			int dataPerPage = 3; //1페이지 당 데이터 수
			int studentId = student.getStudentId();
			int totalData = lps.findAllCount(studentId);
			int startPage = 0;
			int endPage = 0;
			
			String cp = (String)request.getParameter("mylessonlist_current_page");
			if(cp != null && cp !="") {
				currPage = Integer.parseInt(cp);
			}
			
			List<Lesson>lessonList = lps.findByPage(currPage, dataPerPage, studentId);
			
			ObjectMapper mapper = new ObjectMapper();
			List<Map<String,Object>> list = new ArrayList<>();
			for(Lesson l: lessonList) {
				int totalPage = (int)(Math.ceil((double)totalData/dataPerPage)); //총페이지수
				System.out.println("서블릿totalpage:"+totalPage);
				System.out.println("서블릿totalData:"+totalData);
				System.out.println("서블릿dataperPage:"+dataPerPage);
				
				//페이지 구하기
				//시작 페이지 구하기
				startPage = ((currPage-1)/dataPerPage)*(dataPerPage)+1;
				//끝페이지구하기
				endPage = startPage+(dataPerPage - 1);
				if(endPage > totalPage) {
					endPage = totalPage;
				}
				Map<String, Object> map = new HashMap<>();
				map.put("lesson_id", l.getLessonId());
				map.put("lesson_name", l.getLessonName());
				map.put("lesson_status", l.getLessonStatus());
				map.put("lesson_fee", l.getLessonFee());
				map.put("lesson_end",sdf.format(l.getLessonEnd()));
				map.put("lesson_start",sdf.format(l.getLessonStart()));
				map.put("start_page",Integer.toString(startPage));
				map.put("end_page",Integer.toString(endPage));
				map.put("total_page",Integer.toString(totalPage));
				list.add(map);	
			}
			out.print(mapper.writeValueAsString(list));
		} catch (FindException e) {
			out.print("{\"status\":1}");
//			System.out.println("좋아요한 강좌가 없음");
		}
	}
}