package com.my.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.my.exception.FindException;
import com.my.sql.MyConnection;
import com.my.vo.Notice;

public class NoticeDAOOracle implements NoticeDAO{

	@Override
	public List<Notice> selectAll() throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		
		String selectAllSQL = "select * from(\r\n" + 
				"        select n.notice_title,  row_number() over (order by notice_id desc) as rnum\r\n" + 
				"        from notice n\r\n" + 
				"        )\r\n" + 
				"        where rnum between 1 and 10";
		List<Notice> all = new ArrayList<>();
		try {
			pstmt = con.prepareStatement(selectAllSQL);
			rs = pstmt.executeQuery();
			while(rs.next()) {
//NOTICE_ID			NUMBER(5,0)
//NOTICE_TITLE		VARCHAR2(100 BYTE)
//NOTICE_DATE		DATE
//NOTICE_CONTENT	VARCHAR2(2000 BYTE)
				
				String noticeTitle = rs.getString("notice_title");
				Notice notice = new Notice( 0, noticeTitle, null, null);
				all.add(notice);
			}
			if(all.size() == 0) {
				throw new FindException("공지사항이  하나도 없습니다");
			}
			return all;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(con, pstmt, rs);
		}
	}

	@Override
	public Notice selectById(int NoticeId) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		
		}
		
		String selectByIdSQL = "select * from notice where notice_id=?";
		try {
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setInt(1, NoticeId);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int noticeId = rs.getInt("notice_id");
				String noticeTitle = rs.getString("notice_title");
				Date noticeDate = rs.getDate("notice_date");
				String noticeContent = rs.getString("notice_content");
				
				Notice notice = new Notice(noticeId, noticeTitle, noticeDate, noticeContent);
				return notice;
			}else {
				throw new FindException("상세페이지가 존재하지 않습니다.");
			}
			
		} catch (SQLException e) {
			
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
	
	@Override
	public int selectAllCount() throws FindException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		String selectAllCountSQL = "SELECT COUNT (*) FROM notice";
		try {
			pstmt = con.prepareStatement(selectAllCountSQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int selectAllCount = rs.getInt("COUNT(*)");
				return selectAllCount;
			}else {
				throw new FindException("공지사항이 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con,pstmt,rs);
		}
	}

	@Override
	public List<Notice> selectByPage(int currPage, int dataPerPage) throws FindException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		String selectByPageSQL = "SELECT * FROM (SELECT notice_id, notice_title, notice_date, notice_content, row_number() OVER (ORDER BY notice_id DESC) AS rnum FROM notice) WHERE rnum BETWEEN fun_start_row(?,?) AND fun_end_row(?,?)";
		List<Notice> currPageList = new ArrayList<>();
		try {
			pstmt = con.prepareStatement(selectByPageSQL);
			pstmt.setInt(1, currPage);
			pstmt.setInt(2, dataPerPage);
			pstmt.setInt(3, currPage);
			pstmt.setInt(4, dataPerPage);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int notice_id = rs.getInt("notice_id");
				String notice_title = rs.getString("notice_title");
				Date notice_date = rs.getDate("notice_date");
				String notice_content = rs.getString("notice_content");
				Notice notice = new Notice(notice_id, notice_title, notice_date, notice_content);
				currPageList.add(notice);
			}
			if(currPageList.size()==0) {
				throw new FindException("공지사항이 존재하지 않습니다.");
			}
			return currPageList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
	};
	
	@Override
	public Notice selectByName(String NoticeName) throws FindException {
		return null;
	}
	
	
  //공지사항리스트 테스트	
//	public static void main(String[]args) {
//
//		NoticeDAOOracle dao = new NoticeDAOOracle();
//		try {
//	         List<Notice> list = dao.selectAll();
//	         for(Notice n: list) {
//	            System.out.println( n.getNoticeTitle());
//	         }
//	      } catch (FindException e) {
//	         e.printStackTrace();
//		}
		
		//게시물 개수 세기
//		int s;
//		try {
//			s = dao.selectAllCount();
//			System.out.println(s);
//		} catch (FindException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

//  상세공지사항 테스트 
//	public static void main(String[]args) {
//		NoticeDAOOracle dao = new NoticeDAOOracle();
//		int NoticeID = 2;
//		Notice notice = new Notice();
//		
//		try {
//			notice = dao.selectById(NoticeID);
//			System.out.println(notice);
//		} catch (FindException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}