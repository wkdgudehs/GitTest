package com.my.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.my.exception.FindException;
import com.my.sql.MyConnection;
import com.my.vo.Category;
import com.my.vo.Lesson;

public class CategoryDAOOracle implements CategoryDAO {

	@Override
	public List<Category> selectAll() throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String selectAllSQL = "SELECT *\r\n" + 
				"FROM category ORDER BY category_id";
		List<Category> all = new ArrayList<>();
		try {
			pstmt = con.prepareStatement(selectAllSQL);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int categoryId = rs.getInt("category_id");
				String categoryName = rs.getString("category_name");
				int categoryParentId = rs.getInt("category_parent_id");
				Category c = new Category(categoryId, categoryName, categoryParentId);
				all.add(c);
			}
			if(all.size() == 0) {
				throw new FindException("카테고리가 없습니다");
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
	public List<Lesson> selectById(int categoryId) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String selectLessonByCategorySQL = "SELECT * FROM lesson WHERE lesson_status = 0 AND (lesson_category_id = (SELECT category_id FROM category WHERE category_id = ?) OR lesson_category_id in (SELECT category_id FROM category WHERE category_parent_id = ?)) ORDER BY lesson_end_date";
		List<Lesson> all = new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		Calendar enddate = Calendar.getInstance();
		Calendar sysdate = Calendar.getInstance();
		sysdate.setTime(new Date());
		try {
			pstmt = con.prepareStatement(selectLessonByCategorySQL);
			pstmt.setInt(1, categoryId);
			pstmt.setInt(2, categoryId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int lessonId = rs.getInt("lesson_id");
				String lessonName = rs.getString("lesson_name");
				int lessonTargetFee = rs.getInt("lesson_target_amount");
				int lessonTotalFee = rs.getInt("lesson_total_amount");;
				String lessonDescription = rs.getString("lesson_description");
				Date lessonEnd = rs.getDate("lesson_end_date");
				int lessonCategoryId = rs.getInt("lesson_category_id");
				enddate.setTime(lessonEnd);
				long diffDays = (enddate.getTimeInMillis() - sysdate.getTimeInMillis()) / 1000 / (24*60*60);
				if(diffDays < 0) {
					diffDays = 0;
				}
				Lesson l = new Lesson();
				l.setLessonId(lessonId);
				l.setLessonName(lessonName);
				l.setLessonDescription(lessonDescription);
				l.setLessonTotalFee(lessonTotalFee);
				l.setTargetPercent(lessonTotalFee*100/lessonTargetFee);
				l.setDiffDays((int)diffDays); 
				l.setLessonCategory(lessonCategoryId);
				all.add(l);
			}
			return all;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public static void main(String[]args) {
//		CategoryDAO dao = new CategoryDAOOracle();
//		try {
//			List<Lesson> list = dao.selectById(101);
//			for(Lesson l : list) {
//				System.out.println(l.getLessonName() +":"+ l.getLessonDescription() +":"+ l.getLessonTotalFee() +":"+ l.getTargetPercent() +":"+ l.getDiffDays());
//			}
//		} catch (FindException e) {
//			e.printStackTrace();
//		}
//		try {
//			List<Category> list = dao.selectAll();
//			for(Category c : list) {
//				System.out.println(c.getCategoryId() +":"+ c.getCategoryName() +":"+ c.getParentCategoryId());
//			}
//		} catch (FindException e) {
//			e.printStackTrace();
//		}
//	}
}
