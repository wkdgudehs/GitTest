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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.NoticeService;
import com.my.vo.Notice;


public class NoticeListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset = UTF-8");
		PrintWriter out = response.getWriter();
		NoticeService service = new NoticeService();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			int currPage = 1;//현재페이지
			int dataPerPage = 10; //1페이지 당 데이터 수
			int totalData = service.findAllCount(); //DB에 저장된 공지사항 게시물 총 개수
			int startPage = 0;
			int endPage = 0;
			
			String cp = request.getParameter("current_page");
			if(cp != null && !cp.equals("")) {
				currPage = Integer.parseInt(cp);
			}
			
			List<Notice> notice = service.findByPage(currPage, dataPerPage);//service.findAll();
			request.setAttribute("noticelist", notice);

			ObjectMapper mapper = new ObjectMapper();
			List<Map<String,Object>> list = new ArrayList<>();
			for(Notice notices:notice) {
				int totalPage = (int)(Math.ceil((double)totalData/dataPerPage)); //총페이지 수
				
				//페이지 구하기
				//시작 페이지 구하기
				startPage = ((currPage-1)/dataPerPage)*(dataPerPage)+1;
				//끝페이지구하기
				endPage = startPage+(dataPerPage - 1);
				if(endPage > totalPage) {
					endPage = totalPage;
				}
				Map<String,Object> map = new HashMap<>();
				map.put("notice_id", notices.getNoticeId());
				map.put("notice_title",notices.getNoticeTitle());
				map.put("notice_date", sdf.format(notices.getNoticeDate()));
				map.put("notice_content", notices.getNoticeContent());
				map.put("start_page",Integer.toString(startPage));
				map.put("end_page",Integer.toString(endPage));
				map.put("total_page",Integer.toString(totalPage));
				System.out.println(currPage);
				System.out.println("startPage"+startPage);
				System.out.println("endPage"+endPage);	
				System.out.println("totalPage"+totalPage);
				list.add(map);
			}
			if(notice == null) {
				out.print("{\"status\":0}");
			}else {
				 out.print(mapper.writeValueAsString(list));
			}
		} catch (FindException e) {
			e.printStackTrace();
			out.print("{\"status\":-1}");
		}
	}

}