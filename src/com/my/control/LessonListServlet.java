package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.ILessonService;
import com.my.service.LessonService;
import com.my.vo.Lesson;

public class LessonListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		ILessonService service = new LessonService();
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> list2 = new ArrayList<>();
		try {
			int currPage = 1;//현재페이지
			int dataPerPage = 9; //1페이지 당 데이터 수
			int totalData = service.findAllCount();
			int startPage = 0;
			int endPage = 0;
			
			String cp = request.getParameter("current_page");
			if(cp != null && !cp.equals("")) {
				currPage = Integer.parseInt(cp);
			}
			if(session.getAttribute("categorylessonlist") == null) {
				List<Lesson> list = service.findByPage(currPage, dataPerPage);
				for(Lesson lesson: list) {
					int totalPage = (int)(Math.ceil(totalData/dataPerPage)); //총페이지 수
					
					//페이지 구하기
					//시작 페이지 구하기
					startPage = ((currPage-1)/dataPerPage)*(dataPerPage)+1;
					//끝페이지구하기
					endPage = startPage+(dataPerPage - 1);
					if(endPage > totalPage) {
						endPage = totalPage;
					}
					Map<String, Object> map1 = new HashMap<>();
					map1.put("lesson_id", lesson.getLessonId());
					map1.put("lesson_name", lesson.getLessonName());
					map1.put("targetpercent", lesson.getTargetPercent());
					map1.put("lesson_total_amount", lesson.getLessonTotalFee());
					map1.put("lesson_description", lesson.getLessonDescription());
					map1.put("lesson_diffdays", lesson.getDiffDays());
					map1.put("lesson_category_id", lesson.getLessonCategory());
					map1.put("start_page",Integer.toString(startPage));
					map1.put("end_page",Integer.toString(endPage));
					map1.put("total_page",Integer.toString(totalPage));
					list2.add(map1);
				}
				request.setAttribute("lessonlist", list);
				String jsonStr = mapper.writeValueAsString(list2);
				System.out.println(jsonStr);
				out.print(jsonStr);
			}else {
				System.out.println("카테고리리스트 유무");
				List<Lesson> categorylist = (List<Lesson>) session.getAttribute("categorylessonlist");
				request.setAttribute("categorylessonlist", categorylist);
				for(Lesson lesson: categorylist) {
					int totalPage = (int)(Math.ceil(totalData/dataPerPage)); //총페이지 수
					
					//페이지 구하기
					//시작 페이지 구하기
					startPage = ((currPage-1)/dataPerPage)*(dataPerPage)+1;
					//끝페이지구하기
					endPage = startPage+(dataPerPage - 1);
					if(endPage > totalPage) {
						endPage = totalPage;
					}
					Map<String, Object> map1 = new HashMap<>();
					map1.put("lesson_id", lesson.getLessonId());
					map1.put("lesson_name", lesson.getLessonName());
					map1.put("targetpercent", lesson.getTargetPercent());
					map1.put("lesson_total_amount", lesson.getLessonTotalFee());
					map1.put("lesson_description", lesson.getLessonDescription());
					map1.put("lesson_diffdays", lesson.getDiffDays());
					map1.put("lesson_category_id", lesson.getLessonCategory());
					map1.put("start_page",Integer.toString(startPage));
					map1.put("end_page",Integer.toString(endPage));
					map1.put("total_page",Integer.toString(totalPage));
					list2.add(map1);
				}
				String jsonStr = mapper.writeValueAsString(list2);
				out.print(jsonStr);
			}
		} catch (FindException e) {
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		ILessonService service = new LessonService();
		PrintWriter out = response.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		String union = request.getParameter("union");
		List<Map<String, Object>> list2 = new ArrayList<>();
		try {
			List<Lesson> searchlist = service.findUnion(union);
			for(Lesson lesson: searchlist) {
				Map<String, Object> map1 = new HashMap<>();
				map1.put("lesson_id", lesson.getLessonId());
				map1.put("lesson_name", lesson.getLessonName());
				map1.put("targetpercent", lesson.getTargetPercent());
				map1.put("lesson_total_amount", lesson.getLessonTotalFee());
				map1.put("lesson_description", lesson.getLessonDescription());
				map1.put("lesson_diffdays", lesson.getDiffDays());
				map1.put("lesson_category_id", lesson.getLessonCategory());
				list2.add(map1);
			}
			if(list2.size() != 0) {
				request.setAttribute("searchlist", searchlist);
				String jsonStr = mapper.writeValueAsString(list2);
				System.out.println(jsonStr);
				out.print(jsonStr);
			}else {
				out.print("{\"status\" : -1}");
			}
		} catch (FindException e) {
			e.printStackTrace();
		}
		
	}
}
