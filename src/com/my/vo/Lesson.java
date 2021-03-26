package com.my.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Lesson {
	private int lessonId;
	private Student teacher;
	private String lessonName;
	private int lessonTargetFee;
	private int lessonTotalFee;
	private int lessonParticipant;
	private int lessonStatus;
	private int lessonFee;
	private String lessonDescription;
	private int lessonCategory;
	private Date lessonCreate;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date lessonEnd;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date lessonStart;
	private int lessonRecommend;
	private List<LPS> lps;
	int targetPercent;
	int diffDays;
	

	public Lesson(int lessonId, Student teacher, String lessonName, int lessonTargetFee, int lessonTotalFee,
			int lessonParticipant, int lessonStatus, int lessonFee, String lessonDescription, int lessonCategory,
			Date lessonCreate, Date lessonEnd, Date lessonStart,int lessonRecommend) {
		
		this(lessonId, teacher, lessonName, lessonTargetFee, lessonTotalFee,
			lessonParticipant, lessonStatus, lessonFee,lessonDescription, 
			lessonCategory, lessonCreate, lessonEnd, lessonStart, lessonRecommend,null);
	}

	public Lesson(int lessonId, Student teacher, String lessonName, int lessonTargetFee, int lessonTotalFee,
			int lessonParticipant, int lessonStatus, int lessonFee, String lessonDescription, int lessonCategory,
			Date lessonCreate, Date lessonEnd, Date lessonStart, int lessonRecommend,List<LPS> lps) {
		super();
		this.lessonId = lessonId;
		this.teacher = teacher;
		this.lessonName = lessonName;
		this.lessonTargetFee = lessonTargetFee;
		this.lessonTotalFee = lessonTotalFee;
		this.lessonParticipant = lessonParticipant;
		this.lessonStatus = lessonStatus;
		this.lessonFee = lessonFee;
		this.lessonDescription = lessonDescription;
		this.lessonCategory = lessonCategory;
		this.lessonCreate = lessonCreate;
		this.lessonEnd = lessonEnd;
		this.lessonStart = lessonStart;
		this.lps = lps;
		this.lessonRecommend = lessonRecommend;
	}

	public Lesson() {
		super();
	}

	public Lesson(String lessonName, String lessonDescription, int lessonTotalFee, int targetPercent, int diffDays, int lessonCategory) {
		super();
		this.lessonName = lessonName;
		this.lessonDescription = lessonDescription;
		this.lessonTotalFee = lessonTotalFee;
		this.targetPercent = targetPercent;
		this.diffDays = diffDays;
		this.lessonCategory = lessonCategory;
	}
	
	public Lesson(int lessonId, String lessonName, String lessonDescription, int lessonTargetFee, int lessonTotalFee, int targetPercent, int diffDays, int lessonCategory) {
		this.lessonId = lessonId;
		this.lessonName = lessonName;
		this.lessonDescription = lessonDescription;
		this.lessonTargetFee = lessonTargetFee;
		this.lessonTotalFee = lessonTotalFee;
		this.targetPercent = targetPercent;
		this.diffDays = diffDays;
		this.lessonCategory = lessonCategory;
	}

	public Lesson(String lessonName, int lessonTotalFee, int lessonTargetFee, int targetPercent, Date lessonEnd, int diffDays, 
			int lessonParticipant, int lessonStatus, int lessonFee, int lessonRecommend) {
			this.lessonTargetFee = lessonTargetFee;
			this.lessonName = lessonName;
			this.lessonTotalFee = lessonTotalFee;
			this.targetPercent = targetPercent;
			this.lessonEnd = lessonEnd;
			this.diffDays = diffDays;
			this.lessonParticipant = lessonParticipant;
			this.lessonStatus =lessonStatus;
			this.lessonFee = lessonFee;
			this.lessonRecommend = lessonRecommend;
			}
	
	public Lesson(int lessonId, String lessonName, int lessonTotalFee, int lessonTargetFee, int targetPercent, Date lessonEnd, int diffDays, 
			int lessonParticipant, int lessonStatus, int lessonFee, int lessonRecommend) {
			this.lessonId = lessonId;
			this.lessonTargetFee = lessonTargetFee;
			this.lessonName = lessonName;
			this.lessonTotalFee = lessonTotalFee;
			this.targetPercent = targetPercent;
			this.lessonEnd = lessonEnd;
			this.diffDays = diffDays;
			this.lessonParticipant = lessonParticipant;
			this.lessonStatus =lessonStatus;
			this.lessonFee = lessonFee;
			this.lessonRecommend = lessonRecommend;
			}
	
	public Lesson(String lessonName, String lessonDescription, int lessonTotalFee, int targetPercent, int diffDays) {
		this.lessonName = lessonName;
		this.lessonDescription = lessonDescription;
		this.lessonTotalFee = lessonTotalFee;
		this.targetPercent = targetPercent;
		this.diffDays = diffDays;
	}
	
	public Lesson(int lessonId, String lessonName, String lessonDescription, int lessonTotalFee, int targetPercent, int diffDays) {
		this.lessonId = lessonId;
		this.lessonName = lessonName;
		this.lessonDescription = lessonDescription;
		this.lessonTotalFee = lessonTotalFee;
		this.targetPercent = targetPercent;
		this.diffDays = diffDays;
	}

	public Lesson(int lessonId, Student teacher, String lessonName, int lessonStatus, int lessonFee, int lessonCategory,
			Date lessonEnd, Date lessonStart) {
		super();
		this.lessonId = lessonId;
		this.teacher = teacher;
		this.lessonName = lessonName;
		this.lessonStatus = lessonStatus;
		this.lessonFee = lessonFee;
		this.lessonCategory = lessonCategory;
		this.lessonEnd = lessonEnd;
		this.lessonStart = lessonStart;
	}
	
	public Lesson(int lessonId, String lessonName, int lessonStatus, int lessonFee, Date lessonEnd, Date lessonStart) {
		super();
		this.lessonId = lessonId;
		this.lessonName = lessonName;
		this.lessonStatus = lessonStatus;
		this.lessonFee = lessonFee;
		this.lessonEnd = lessonEnd;
		this.lessonStart = lessonStart;	
	}
	
	public int getTargetPercent() {
		return targetPercent;
	}

	public void setTargetPercent(int targetPercent) {
		this.targetPercent = targetPercent;
	}

	public int getDiffDays() {
		return diffDays;
	}

	public void setDiffDays(int diffDays) {
		this.diffDays = diffDays;
	}

	@Override
	public String toString() {
		return "Lesson [lessonId=" + lessonId + ", teacher=" + teacher + ", lessonName=" + lessonName
				+ ", lessonTargetFee=" + lessonTargetFee + ", lessonTotalFee=" + lessonTotalFee + ", lessonParticipant="
				+ lessonParticipant + ", lessonStatus=" + lessonStatus + ", lessonFee=" + lessonFee
				+ ", lessonDescription=" + lessonDescription + ", lessonCategory=" + lessonCategory + ", lessonCreate="
				+ lessonCreate + ", lessonEnd=" + lessonEnd + ", lessonStart=" + lessonStart + ", lessonRecommend="
				+ lessonRecommend + ", lps=" + lps + "]";
	}

	public int getLessonId() {
		return lessonId;
	}

	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

	public Student getTeacher() {
		return teacher;
	}

	public void setTeacher(Student teacher) {
		this.teacher = teacher;
	}

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public int getLessonTargetFee() {
		return lessonTargetFee;
	}

	public void setLessonTargetFee(int lessonTargetFee) {
		this.lessonTargetFee = lessonTargetFee;
	}

	public int getLessonTotalFee() {
		return lessonTotalFee;
	}

	public void setLessonTotalFee(int lessonTotalFee) {
		this.lessonTotalFee = lessonTotalFee;
	}

	public int getLessonParticipant() {
		return lessonParticipant;
	}

	public void setLessonParticipant(int lessonParticipant) {
		this.lessonParticipant = lessonParticipant;
	}

	public int getLessonStatus() {
		return lessonStatus;
	}

	public void setLessonStatus(int lessonStatus) {
		this.lessonStatus = lessonStatus;
	}

	public int getLessonFee() {
		return lessonFee;
	}

	public void setLessonFee(int lessonFee) {
		this.lessonFee = lessonFee;
	}

	public String getLessonDescription() {
		return lessonDescription;
	}

	public void setLessonDescription(String lessonDescription) {
		this.lessonDescription = lessonDescription;
	}

	public int getLessonCategory() {
		return lessonCategory;
	}

	public void setLessonCategory(int lessonCategory) {
		this.lessonCategory = lessonCategory;
	}

	public Date getLessonCreate() {
		return lessonCreate;
	}

	public void setLessonCreate(Date lessonCreate) {
		this.lessonCreate = lessonCreate;
	}

	public Date getLessonEnd() {
		return lessonEnd;
	}

	public void setLessonEnd(Date lessonEnd) {
		this.lessonEnd = lessonEnd;
	}

	public Date getLessonStart() {
		return lessonStart;
	}

	public void setLessonStart(Date lessonStart) {
		this.lessonStart = lessonStart;
	}

	public int getLessonRecommend() {
		return lessonRecommend;
	}

	public void setLessonRecommend(int lessonRecommend) {
		this.lessonRecommend = lessonRecommend;
	}

	public List<LPS> getLps() {
		return lps;
	}

	public void setLps(List<LPS> lps) {
		this.lps = lps;
	}
}