package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.ILPSService;
import com.my.service.IStudentService;
import com.my.service.LPSService;
import com.my.service.LessonReviewService;
import com.my.service.LessonService;
import com.my.service.StudentService;
import com.my.vo.LPS;
import com.my.vo.Lesson;
import com.my.vo.LessonReview;
import com.my.vo.Notice;
import com.my.vo.Student;
/**
 * 리뷰리스트 서블릿
 */
public class LessonReviewListServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
       
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("application/json;charset=utf-8");
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
      //작성자 띄우기
      //세션
      HttpSession session = request.getSession();
      PrintWriter out = response.getWriter();
      LessonReviewService service = new LessonReviewService();
      ILPSService lpsservice = new LPSService();
      String lessonId1 = request.getParameter("lessonId");
      int lessonId = Integer.parseInt(lessonId1);
      System.out.println(lessonId);
//      int lessonId = 7;
     try {
//    	List<LPS> lpslist = lpsservice.findByLessonId(lessonId);
    	/*페이지 관련 추가 시작*/
    	int currPage = 1;//현재페이지
		int dataPerPage = 10; //1페이지 당 데이터 수
		int totalData = service.findAllCount(lessonId); //DB에 저장된 공지사항 게시물 총 개수
		int startPage = 0;
		int endPage = 0;
		
		String cp = request.getParameter("current_page");
		if(cp != null && !cp.equals("")) {
			currPage = Integer.parseInt(cp);
		}
		/*페이지 관련 추가 끝*/
        
		//1.비지니스로직 호출
		List<LessonReview> reviewlist = service.findByPage(currPage, dataPerPage, lessonId);//service.findAll();
        //List<LessonReview> reviewlist = service.findAll();
        //2.요청속성추가
        request.setAttribute("reviewlist", reviewlist);
        
        
        
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String,Object>> list = new ArrayList<>();
		for(LessonReview rlist:reviewlist) {
			int totalPage = (int)(Math.ceil(totalData/dataPerPage)); //총페이지 수
			
			//페이지 구하기
			//시작 페이지 구하기
			startPage = ((currPage-1)/dataPerPage)*(dataPerPage)+1;
			//끝페이지구하기
			endPage = startPage+(dataPerPage - 1);
			if(endPage > totalPage) {
				endPage = totalPage;
			}
			
			Map<String,Object> map = new HashMap<>();
			
			map.put("review_content", rlist.getReviewContent());
			map.put("review_date", sdf.format(rlist.getReviewDate()));
			map.put("review_commend", rlist.getRecommend());
			map.put("review_writer", rlist.getLps().getStudent().getStudentName());//^형돈추가
			map.put("totalPage", totalPage);
			map.put("startPage",startPage );
			map.put("endPage", endPage);				
			
			list.add(map);
		}
		
//		System.out.println(mapper.writeValueAsString(list));
		
		if(reviewlist == null) {
			out.print("{\"status\":0}");
		}else {
			 out.print(mapper.writeValueAsString(list));
		}
	
        
     } catch (FindException e) {
        System.out.print("{\"status\":-1}");//
        e.printStackTrace();
     }
  }
}