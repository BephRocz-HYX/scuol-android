package com.nsmss.scuol.dao;

import java.util.LinkedList;

import com.nsmss.scuol.bean.CourseData;
import com.nsmss.scuol.bean.CourseInfo;
import com.nsmss.scuol.common.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CourseDataDao {
	private DBHelper globalDBHelper;
	private SQLiteDatabase db;
	
	public CourseDataDao(Context context) {
		globalDBHelper = new DBHelper(context);
		db = globalDBHelper.getWritableDatabase();
	}
	
	public boolean insert(CourseData courseData) {
		try {
			String sql = "INSERT INTO course_data (cid, uid) VALUES (?, ?)";
			db.execSQL(sql, new Object[] {
					courseData.getCid(),
					courseData.getUid()
					});
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean delete(int cid) {
		try {
			String sql = "DELETE FROM course_data WHERE cid=?";
			db.execSQL(sql, new Object[] {cid});
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean clear(int uid) {
		try {
			String sql = "DELETE FROM course_data WHERE uid=?";
			db.execSQL(sql, new Object[] {uid});
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	// 查询某用户的所有课程
	public LinkedList<CourseInfo> query(int uid) {
		try {
			LinkedList<CourseInfo> cInfos = new LinkedList<CourseInfo>();
			
			String sql = "SELECT * FROM course_info WHERE cid IN (SELECT cid FROM course_data WHERE uid=?)";
			Cursor c = db.rawQuery(sql, new String[]{String.valueOf(uid)});
			
			while (c.moveToNext()) {
				CourseInfo cInfo = new CourseInfo();
				cInfo.setWeekfrom(c.getInt(c.getColumnIndex("weekfrom")));
				cInfo.setWeekto(c.getInt(c.getColumnIndex("weekto")));
				cInfo.setWeektype(c.getInt(c.getColumnIndex("weektype")));
				cInfo.setDay(c.getInt(c.getColumnIndex("day")));
				cInfo.setLessonfrom(c.getInt(c.getColumnIndex("lesson_from")));
				cInfo.setLessonto(c.getInt(c.getColumnIndex("lesson_to")));
				cInfo.setCourseid(c.getString(c.getColumnIndex("courseid")));
				cInfo.setNum(c.getString(c.getColumnIndex("num")));
				cInfo.setName(c.getString(c.getColumnIndex("name")));
				cInfo.setCredit(c.getString(c.getColumnIndex("credit")));
				cInfo.setAttr(c.getString(c.getColumnIndex("attr")));
				cInfo.setExam(c.getString(c.getColumnIndex("exam")));
				cInfo.setTeacher(c.getString(c.getColumnIndex("teacher")));
				cInfo.setCampus(c.getString(c.getColumnIndex("campus")));
				cInfo.setBld(c.getString(c.getColumnIndex("bld")));
				cInfo.setPlace(c.getString(c.getColumnIndex("place")));
				cInfos.add(cInfo);
			}
			
			return cInfos;
		} catch (Exception e) {
			return null;
		}
		
	}
}
