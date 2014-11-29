package com.nsmss.scuol.exam;


import java.util.List;
import java.util.Map;

import com.nsmss.scuol.R;
import com.nsmss.scuol.bean.GlobalInfo;
import com.nsmss.scuol.bean.UserData;
import com.nsmss.scuol.common.NetHelper;
import com.nsmss.scuol.dao.GlobalInfoDao;
import com.nsmss.scuol.dao.UserDataDao;
import com.nsmss.scuol.main.MainActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ExamActivity extends Activity
{
	/**
	 * 静态成员变量
	 */
	private static Context context;

	/**
	 * UI相关成员变量
	 */
	ProgressDialog progressDialog;

	
	/**
	 * View相关成员变量
	 */
	View backView;
	View refreshView;
	private ListView listView;
	private SimpleAdapter simpleAdapter;

	
	/**
	 * Dao成员变量
	 */
	private GlobalInfoDao gDao;
	private UserDataDao uDao;

	
	/**
	 * 数据模型变量
	 */
	GlobalInfo gInfo;
	private UserData uData;

	
	/**
	 * 网络模型对象
	 */
	NetHelper netHelper;

	
	/**
	 * 数据存储变量
	 */
	List<Map<String, String>> data;

	/**
	 * 状态变量
	 */

	/**
	 * 临时变量
	 */
	int uid;
	private String errCodeStr;

	/**
	 * Activity回调函数
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// 继承父类方法，绑定View
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam);

		// 初始化context
		context = getApplicationContext();
		
		//初始化UI相关成员变量
		
		// 初始化网络模型对象
		netHelper = new NetHelper();

		// 初始化Dao成员变量
		gDao = new GlobalInfoDao(context);
		uDao = new UserDataDao(context);

		// 初始化数据模型变量
		gInfo = gDao.query();
		uid = gInfo.getActiveUserUid();
		uData = uDao.query(uid);

		// 初始化View成员变量
		initView();
		initListener();
		// 初始化状态变量

		// 初始化临时变量

		// 自定义函数
		getExamInfo();
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
	private Runnable connection = new Runnable()
	{
		@Override
		public void run()
		{
			data = netHelper.examInfo(uData);

			if (data != null && !data.isEmpty())
			{ // 如果刷新成功，更新列表
				progressDialog.dismiss();
				runOnUiThread(succRunnable);
			} 
			else if (data != null && data.isEmpty()) {
				progressDialog.dismiss();
				runOnUiThread(retRunnable);
			}
			else
			{ // 刷新失败，显示消息
				progressDialog.dismiss();
				errCodeStr = "104";
				runOnUiThread(errRunnable);
			}
		}
	};

	/**
	 * 自定义成员对象
	 */
	private Runnable succRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			updateToListview();
		}
	};
	
	private Runnable retRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			Toast.makeText(ExamActivity.this, "暂时没有考试信息！", Toast.LENGTH_SHORT).show();
		}
	};
	
	private Runnable errRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			Toast.makeText(ExamActivity.this, "连接错误！错误代码："+errCodeStr, Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * 自定义方法
	 */
	private void getExamInfo()
	{
		// TODO Auto-generated method stub
		progressDialog = ProgressDialog.show(ExamActivity.this, null, getResources().getString(R.string.loading_tip));// 显示ProgressBar
		new Thread(connection).start();	// 开启连接线程
	}

	private void updateToListview()
	{
		// TODO Auto-generated method stub
		simpleAdapter = new SimpleAdapter(context, data, R.layout.list_exam, new String[]
		{ "subject", "time", "place" }, new int[]
		{ R.id.list_subject, R.id.list_time, R.id.list_place });

		listView.setAdapter(simpleAdapter);
		listView.setDivider(null);
	}

	private void initListener()
	{
		// TODO Auto-generated method stub
		backView.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				jumpToMain();
			}

		});
		refreshView.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				refreshList();
			}

		});
	}

	private void initView()
	{
		// TODO Auto-generated method stub
		backView = findViewById(R.id.Btn_Change_Password_Back);
		refreshView = findViewById(R.id.Btn_Change_Password_Refresh);
		listView = (ListView) findViewById(R.id.listView_Exam);
	}

	private void jumpToMain()
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(ExamActivity.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		finish();
	}

	private void refreshList()
	{
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage(getResources().getString(R.string.loading_tip));
		progressDialog.setCancelable(true);
		progressDialog.show();
		new Thread(connection).start(); // 开启连接线程
	}

}
