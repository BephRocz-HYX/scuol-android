package com.nsmss.scuol.start;

import java.util.Calendar;

import com.nsmss.scuol.R;
import com.nsmss.scuol.bean.GlobalInfo;
import com.nsmss.scuol.dao.GlobalInfoDao;
import com.nsmss.scuol.login.LoginActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends Activity {
	
	/**
	 *  静态成员变量
	 */
	private static Context context;
	private final int SPLASH_DISPLAY_LENGHT = 0; // TODO 改为延迟的毫秒数2000
	
	/**
	 * UI相关成员变量
	 */
	
	
	/**
	 * View相关成员变量
	 */	
	
	
	/**
	 * Dao成员变量
	 */
	private GlobalInfoDao gInfoDao;
	
	
	/**
	 * 数据模型变量
	 */
	private GlobalInfo gInfo;
	
	
	/**
	 * 数据存储变量
	 */
	

	/**
	 * 状态变量
	 */

	/**
	 * 临时变量
	 */
	private long timeStart;
	
	/**
	 * Activity回调函数
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		timeStart = System.currentTimeMillis();
		
		// 继承父类方法，绑定View
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		// 初始化context
		context = getApplicationContext(); 
		
		// 初始化View成员变量
		
		// 初始化Dao成员变量
		gInfoDao = new GlobalInfoDao(context);
		
		// 初始化数据模型变量
		gInfo = gInfoDao.query();
		
		// 自定义函数
		initGInfo(context);
		
		// 如果初始化消耗的时间小于预定时间
		long timeInit = System.currentTimeMillis()-timeStart;
		if (timeInit < SPLASH_DISPLAY_LENGHT) {
			new Handler().postDelayed(new Runnable(){ 
		         @Override 
		         public void run() {
		        	 jumpToLogin();
		         }
		    }, SPLASH_DISPLAY_LENGHT-timeInit);
		}
		else {
			jumpToLogin();
		}
	}

    @Override
    protected void onPause() {
       super.onPause(); 
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    }
    
    @Override
    protected void onResume() {
        super.onResume(); 
    }
    
	/**
	 * 线程对象
	 */
	
	
	/**
	 * 自定义成员对象
	 */
	
	
	/**
	 * 自定义方法
	 */

	private void initGInfo(Context context) {
    	if (gInfo == null) {
	    	int version = 0;
	    	String vsersionStr = "";
			try {
				PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
				version = pi.versionCode;
				vsersionStr = pi.versionName;
			} catch (Exception e) {
				version = 1;
				vsersionStr = "1.0";
			}
			
			Calendar calendar = Calendar.getInstance();
			int month = calendar.get(Calendar.MONTH)+1;
			int year = calendar.get(Calendar.YEAR);

			gInfo = new GlobalInfo();
			gInfo.setVersion(version);
			gInfo.setVersionStr(vsersionStr);
			
			// TODO 改为相应学期开学时间
			gInfo.setTermBegin("2013-09-08");

			// 下半学期
			if (month < 8) {
				gInfo.setYearFrom(year-1);
				gInfo.setYearTo(year);
				gInfo.setTerm(2);
			}
			// 上半学期
			else {
				gInfo.setYearFrom(year);
				gInfo.setYearTo(year+1);
				gInfo.setTerm(1);
			}
			
			gInfo.setFirstUse(1);
			gInfo.setActiveUserUid(0);
			
			gInfoDao.insert(gInfo);
		}
    }
	
	private void jumpToLogin() {
   	 	Intent intent = new Intent(StartActivity.this, LoginActivity.class); 
        startActivity(intent); 
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        finish(); 
	}
}
