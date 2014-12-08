package com.nsmss.scuol.grade;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nsmss.scuol.R;
import com.nsmss.scuol.bean.GlobalInfo;
import com.nsmss.scuol.bean.GradeData;
import com.nsmss.scuol.bean.UserData;
import com.nsmss.scuol.common.NetHelper;
import com.nsmss.scuol.dao.GlobalInfoDao;
import com.nsmss.scuol.dao.UserDataDao;
import com.nsmss.scuol.main.MainActivity;
import com.nsmss.scuol.personal.PersonalActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @version 1
 * @author LMD
 * 示例类
 */

public class GradeActivity extends Activity {
	
	/**
	 *  静态成员变量
	 */
	private static Context context;
	
	/**
	 * UI相关成员变量
	 */
	private View backView;
	private View refreshView;
	private TextView avarageTextView;
	private TextView GPATextView;
	
	/**
	 * View相关成员变量
	 */
	private ProgressDialog progressDialog;
	ListView gradeListView;
	private SimpleAdapter sAdapter;
	
	/**
	 * Dao成员变量
	 */
	private GlobalInfoDao gDao;
	private UserDataDao uDao;
	
	/**
	 * 数据模型变量
	 */
	private GlobalInfo gInfo;
	private UserData uData;

	/**
	 * 数据存储变量
	 */
	List<Map<String, String>> gradeList;
	
	/**
	 * 状态变量
	 */


	/**
	 * 临时变量
	 */
	int uid;
	float AvaGpa[];
	
	/**
	 * Activity回调函数
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// 继承父类方法，绑定View
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grade);
		
		// 初始化context
		context = getApplicationContext(); 
		
		// 初始化View成员变量
		
		// 初始化Dao成员变量
		gDao = new GlobalInfoDao(context);
		uDao = new UserDataDao(context);
		
		// 初始化数据模型变量
		gInfo = gDao.query();
		uid = gInfo.getActiveUserUid();
		uData = uDao.query(uid);
		
		// 初始化状态变量
		
		// 初始化临时变量
		
		// 自定义函数
		initView();
		initListener();
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			jumpToMain();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	/**
	 * 线程对象
	 */
    private Runnable connRunnable = new Runnable() {
    	@Override  
		public void run() {
			NetHelper nHelper = new NetHelper();
			gradeList = nHelper.getGrades(uData);
			AvaGpa = nHelper.getAvaGpa(gradeList);
    		// 如果连接成功，返回了更新数据
    		if (gradeList != null) {
    			// 判断状态对话框是否显示
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					runOnUiThread(succRunnable);
				}
			}
    		// 连接错误
    		else {
    			// 判断状态对话框是否显示
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					runOnUiThread(errRunnable);
				}
			}
    	}
    };
    // 连接成功线程
	private Runnable succRunnable = new Runnable() {
		@Override  
	    public void run() {
			updateGData();
	    }
	};
	
	// 连接错误线程
	private Runnable errRunnable = new Runnable() {
		@Override  
	    public void run() {
			Toast.makeText(GradeActivity.this, "连接错误！请检查网络连接！", Toast.LENGTH_SHORT).show();
	    }
	};
	
	/**
	 * 自定义成员对象
	 */
	
	
	/**
	 * 自定义方法
	 */
	private void initView() {
		backView = findViewById(R.id.Btn_Grade_Back);
		refreshView = findViewById(R.id.Btn_Grade_Refresh);
		gradeListView = (ListView) findViewById(R.id.List_Grade);
    	avarageTextView = (TextView) this.findViewById(R.id.Text_Grade_Score);
    	GPATextView = (TextView) this.findViewById(R.id.Text_Grade_GPA);
	}
	
	private void initListener() {
		backView.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpToMain();
			}
		});
		refreshView.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshGData();
			}
		});
	}
	
	private void refreshGData() {
    	// 显示状态对话框
		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage(getResources().getString(R.string.loading_tip));
		progressDialog.setCancelable(true);
		progressDialog.show();
		
		// 开启连接线程
		new Thread(connRunnable).start();
	}
	
	private void updateGData() {
		
		DecimalFormat dFormat = new DecimalFormat("##0.00"); 
    	avarageTextView.setText(dFormat.format(AvaGpa[0]));
    	GPATextView.setText(dFormat.format(AvaGpa[1]));

		sAdapter = new SimpleAdapter(this, gradeList, R.layout.list_item_grade, 
				new String[] { "attr", "subject", "credit", "grade", "time"}, 
				new int[] { R.id.list_grade_attr, R.id.list_grade_subject, R.id.list_grade_credit,
					R.id.list_grade_grade, R.id.list_grade_time});
		
		gradeListView.setAdapter(sAdapter);
		gradeListView.setDivider(null);
	}
	
	private void jumpToMain() {
		Intent intent=new Intent();
		intent.setClass(GradeActivity.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		finish();
	}
}
