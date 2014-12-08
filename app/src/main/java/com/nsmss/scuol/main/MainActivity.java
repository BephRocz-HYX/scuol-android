package com.nsmss.scuol.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import com.nsmss.scuol.R;
import com.nsmss.scuol.bean.GlobalInfo;
import com.nsmss.scuol.common.Utility;
import com.nsmss.scuol.course.CourseActivity;
import com.nsmss.scuol.dao.GlobalInfoDao;
import com.nsmss.scuol.dao.PersonalInfoDao;
import com.nsmss.scuol.dao.RollInfoDao;
import com.nsmss.scuol.dao.UserDataDao;
import com.nsmss.scuol.exam.ExamActivity;
import com.nsmss.scuol.grade.GradeActivity;
import com.nsmss.scuol.login.LoginActivity;
import com.nsmss.scuol.personal.PersonalActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{
	
	/**
	 *  静态成员变量
	 */
	private static Context context;
    private static final String STATE_MENUDRAWER = "net.simonvt.menudrawer.samples.WindowSample.menuDrawer";
    private static final String STATE_ACTIVE_VIEW_ID = "net.simonvt.menudrawer.samples.WindowSample.activeViewId";
	
	/**
	 * UI相关成员变量
	 */
    private MenuDrawer mMenuDrawer;
    private int mActiveViewId;
	
	/**
	 * View相关成员变量
	 */
	View menuView;
	View refreshView;
	View newsView;
	View examView;
	View courseView;
	View gradeView;
	TextView dateTextView;
	TextView weekTextView;
	
	/**
	 * Dao成员变量
	 */
	private GlobalInfoDao gInfoDao;
    private UserDataDao uDao;
    private PersonalInfoDao pDao;
    private RollInfoDao prDao;
	
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
	
	/**
	 * Activity回调函数
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// 继承父类方法，绑定View
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 初始化context 
		context = getApplicationContext(); 
		
		// 初始化View成员变量
		
		// 初始化Dao成员变量
		gInfoDao = new GlobalInfoDao(context);
		uDao = new UserDataDao(context);
		pDao = new PersonalInfoDao(context);
		prDao = new RollInfoDao(context);
		
		// 初始化数据模型变量
		gInfo = gInfoDao.query();
		uDao.query(gInfo.getActiveUserUid());
		
		// 初始化状态变量
		
		// 初始化临时变量
		
		// 自定义函数
		initMenu(savedInstanceState);
		initView();
		initListener();
		initDate();
		initWeek();
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
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_MENUDRAWER, mMenuDrawer.saveState());
        outState.putInt(STATE_ACTIVE_VIEW_ID, mActiveViewId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mMenuDrawer.toggleMenu();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
    }
    
    @Override
    public void onClick(View v) {
        mActiveViewId = v.getId();
        switch (mActiveViewId) {
        case R.id.Menu_main_personal:
        	jumpToPersonal();
			break;
		case R.id.Menu_main_logout:
			userLogout();
			break;
		case R.id.Menu_main_exit:
			finish();
			break;
		}
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (mMenuDrawer.isMenuVisible() == false) {
				mMenuDrawer.openMenu();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_MENU) {
			mMenuDrawer.toggleMenu();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
	private void initMenu(Bundle inState) {
    	
        if (inState != null) {
            mActiveViewId = inState.getInt(STATE_ACTIVE_VIEW_ID);
        }

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
        mMenuDrawer.setContentView(R.layout.activity_main);
        mMenuDrawer.setMenuView(R.layout.menu_scrollview);

        findViewById(R.id.Menu_main_home).setOnClickListener(this);
        findViewById(R.id.Menu_main_personal).setOnClickListener(this);
        findViewById(R.id.Menu_main_message).setOnClickListener(this);
        findViewById(R.id.Menu_main_options).setOnClickListener(this);
        findViewById(R.id.Menu_main_feedback).setOnClickListener(this);
        findViewById(R.id.Menu_main_update).setOnClickListener(this);
        findViewById(R.id.Menu_main_about).setOnClickListener(this);
        findViewById(R.id.Menu_main_logout).setOnClickListener(this);
        findViewById(R.id.Menu_main_exit).setOnClickListener(this);

        TextView activeView = (TextView) findViewById(mActiveViewId);
        if (activeView != null) {
            mMenuDrawer.setActiveView(activeView);
        }
	}
	
	private void initView() {
		menuView = findViewById(R.id.Btn_Main_Menu);
		refreshView = findViewById(R.id.Btn_Main_Refresh);
		newsView = findViewById(R.id.Btn_Main_Func_1);
		courseView = findViewById(R.id.Btn_Main_Func_2);
		examView = findViewById(R.id.Btn_Main_Func_3);
		gradeView = findViewById(R.id.Btn_Main_Func_4);
		dateTextView = (TextView) findViewById(R.id.scu_main_TextDate);
		weekTextView = (TextView) findViewById(R.id.scu_main_TextWeeks);
	}
	
	private void initListener() {
		menuView.setOnClickListener(new Button.OnClickListener(){
    		public void onClick(View v) {
    			mMenuDrawer.toggleMenu();
            }    
    	});
		refreshView.setOnClickListener(new Button.OnClickListener(){
    		public void onClick(View v) {

            }    
    	});
		newsView.setOnClickListener(new Button.OnClickListener(){
    		public void onClick(View v) {
    			/*
    			Intent intent=new Intent();
    			intent.setClass(MainActivity.this, NewsActivity.class);
    			startActivity(intent);
    			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    			finish();
    			*/
            }    
    	});
		courseView.setOnClickListener(new Button.OnClickListener(){
    		public void onClick(View v) {
    			Intent intent=new Intent();
    			intent.setClass(MainActivity.this, CourseActivity.class);
    			startActivity(intent);
    			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    			finish();
            }    
    	});
		examView.setOnClickListener(new Button.OnClickListener(){
    		public void onClick(View v) {
    			Intent intent=new Intent();
    			intent.setClass(MainActivity.this, ExamActivity.class);
    			startActivity(intent);
    			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    			finish();
            }    
    	});
		gradeView.setOnClickListener(new Button.OnClickListener(){
    		public void onClick(View v) {
    			Intent intent=new Intent();
    			intent.setClass(MainActivity.this, GradeActivity.class);
    			startActivity(intent);
    			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    			finish();
            }    
    	});
	}
	
	@SuppressLint("SimpleDateFormat")
	private void initDate() {
		Date currentTime = new Date();
		String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)  w = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  ");
		String dateString = formatter.format(currentTime);
        dateTextView.setText(dateString + "星期" + weekDays[w]);
	}
	
	private void initWeek() {
		weekTextView.setText("第" + Utility.getWeeks(gInfo.getTermBegin()) + "教学周");
	}
	
	private void userLogout() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("您确定要注销登录吗？");
		builder.setMessage("注销登录将清空您的所有个人信息");
		builder.setPositiveButton("注销", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int uid = gInfo.getActiveUserUid();
				
				uDao.delete(uid);
				pDao.delete(uid);
				prDao.delete(uid);
				//TODO 删除其他用户相关信息
				
				gInfo.setActiveUserUid(0);
				gInfoDao.insert(gInfo);
				
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, LoginActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
				finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.create().show();
	}
	
	private void jumpToPersonal() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, PersonalActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		finish();
	}
}
