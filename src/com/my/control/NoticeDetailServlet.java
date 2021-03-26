package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.my.service.NoticeService;
import com.my.vo.Notice;

public class NoticeDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		String nId =  request.getParameter("notice_id");
		int intNoticeId = Integer.parseInt(nId);
		
		NoticeService service = new NoticeService();
		try {
			Notice notice = service.findById(intNoticeId);
			session.setAttribute("noticedetail", notice);
			ObjectMapper mapper = new ObjectMapper();
			String jsonStr = mapper.writeValueAsString(notice);
			out.print(jsonStr);
			System.out.println(jsonStr);
		} catch (FindException e) {
			e.printStackTrace();
			out.print("{\"status\":-1}");
		}
	}
}