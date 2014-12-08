package com.nsmss.scuol.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nsmss.scuol.bean.PersonalInfo;
import com.nsmss.scuol.common.DBHelper;

public class PersonalInfoDao {
	private DBHelper dbHelper;
	private SQLiteDatabase db;

	public PersonalInfoDao(Context context) {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	private boolean insert(PersonalInfo pInfo) {
		try {
			String sql = "INSERT INTO personal_info (uid, sid, name, days, percent, avarage, gpa) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?)";
			db.execSQL(sql, new Object[] {
				pInfo.getUid(),
				pInfo.getSid(),
				pInfo.getName(),
				pInfo.getDays(),
				pInfo.getPercent(),
				pInfo.getAvarage(),
				pInfo.getGpa()
			});
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean delete(int uid) {
		try {
			String sql = "DELETE FROM personal_info WHERE uid = ?";
			db.execSQL(sql, new Object[] {uid});
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean update(PersonalInfo pInfo) {
		if (pInfo == null) {
			return false;
		}
		if (pInfo.getUid() == 0) {
			return false;
		}
		try {
			if (query(pInfo.getUid()) == null) {
				return insert(pInfo);
			}
			String sql = "UPDATE personal_info SET sid=?, name=?, days=?, percent=?, avarage=?, gpa=? WHERE uid=?";
			db.execSQL(sql, new Object[] {
				pInfo.getSid(),
				pInfo.getName(),
				pInfo.getDays(),
				pInfo.getPercent(),
				pInfo.getAvarage(),
				pInfo.getGpa(),
				pInfo.getUid()
			});
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public PersonalInfo query(int uid) {
		try {
			PersonalInfo pInfo = new PersonalInfo();
			String sql = "SELECT * FROM personal_info WHERE uid="+uid;
			Cursor c = db.rawQuery(sql, null);
			if (c.getCount() == 0) {
				return null;
			}
			else {
				c.moveToFirst();
				pInfo.setUid(uid);
				pInfo.setSid(c.getInt(c.getColumnIndex("sid")));
				pInfo.setName(c.getString(c.getColumnIndex("name")));
				pInfo.setDays(c.getInt(c.getColumnIndex("days")));
				pInfo.setPercent(c.getFloat(c.getColumnIndex("percent")));
				pInfo.setAvarage(c.getFloat(c.getColumnIndex("avarage")));
				pInfo.setGpa(c.getFloat(c.getColumnIndex("gpa")));
				return pInfo;
			}
		} catch (Exception e) {
			return null;
		}
	}
}
