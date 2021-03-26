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
import com.my.exception.FindException;
import com.my.service.CategoryService;
import com.my.service.ICategoryService;
import com.my.vo.Category;
import com.my.vo.Lesson;

public class CategoryListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		ICategoryService service = new CategoryService();
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Category> list = service.findAll();
//			for(Category c: list) {
//				map.put(c, 1);
//			}
			request.setAttribute("categorylist", list);
//			List<Map<String, Object>> list2 = new ArrayList<>();
//			for(Category c: list) {
////			for(Category category: map.keySet()) {
//				Map<String, Object> map1 = new HashMap<>();
//				map1.put("category_id", c.getCategoryId());
//				map1.put("category_name", c.getCategoryName());
//				map1.put("category_parent_id", c.getParentCategoryId());
//				list2.add(map1);
//			}
			
			String jsonStr = mapper.writeValueAsString(list); // list2);
			out.print(jsonStr);
		} catch (FindException e) {
			e.printStackTrace();
		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		ICategoryService service = new CategoryService();
		ObjectMapper mapper = new ObjectMapper();
		int categoryId = Integer.parseInt(request.getParameter("categoryId"));
		List<Map<String, Object>> list2 = new ArrayList<>();
		try {
			List<Lesson> list = service.findById(categoryId);
			request.setAttribute("categorylessonlist", list);
			for(Lesson lesson: list) {
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
		} catch (FindException e) {
			e.printStackTrace();
		}
		String jsonStr = mapper.writeValueAsString(list2);
		if(list2.size() != 0) {
//			out.print("{\"status\":1}"); //[{status: 0, length: 3
			System.out.println(jsonStr);
			out.print(jsonStr);
		}else {
			out.print("{\"status\":-1}");
		}
	}
}
