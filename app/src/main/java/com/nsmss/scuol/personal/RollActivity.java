package com.nsmss.scuol.personal;

import java.util.ArrayList;
import java.util.Map;

import com.nsmss.scuol.R;
import com.nsmss.scuol.bean.GlobalInfo;
import com.nsmss.scuol.bean.UserData;
import com.nsmss.scuol.common.NetHelper;
import com.nsmss.scuol.dao.GlobalInfoDao;
import com.nsmss.scuol.dao.RollInfoDao;
import com.nsmss.scuol.dao.UserDataDao;
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
import android.widget.Toast;

public class RollActivity extends Activity {
	
	/**
	 *  静态成员变量
	 */
	private static Context context;
	
	/**
	 * UI相关成员变量
	 */
	private ProgressDialog progressDialog;
	private SimpleAdapter sAdapter;
	
	/**
	 * View相关成员变量
	 */
	View backView;
	View refreshView;
	ListView rollListView;
	
	/**
	 * Dao成员变量
	 */
	GlobalInfoDao gDao;
	private UserDataDao uDao;
	RollInfoDao prDao;
	
	/**
	 * 数据模型变量
	 */
	GlobalInfo gInfo;
	private UserData uData;

	/**
	 * 数据存储变量
	 */
	ArrayList <Map<String, String>> rollList;

	/**
	 * 状态变量
	 */


	/**
	 * 临时变量
	 */
	int uid;
	
	/**
	 * Activity回调函数
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// 继承父类方法，绑定View
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roll);
		
		// 初始化context
		context = getApplicationContext(); 
		
		// 初始化View成员变量
		
		// 初始化Dao成员变量
		gDao = new GlobalInfoDao(context);
		uDao = new UserDataDao(context);
		prDao = new RollInfoDao(context);
		
		// 初始化数据模型变量
		gInfo = gDao.query();
		uid = gInfo.getActiveUserUid();
		uData = uDao.query(uid);
		rollList = prDao.query(uid);
		
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
			jumpToPersonal();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	/**
	 * 线程对象
	 */
    // 连接线程
    private Runnable connRunnable = new Runnable() {
    	@Override  
		public void run() {
			NetHelper nHelper = new NetHelper();
			rollList = nHelper.getRoll(uData);
    		// 如果连接成功，返回了更新数据
    		if (rollList != null) {
    			// 判断状态对话框是否显示
				if (progressDialog.isShowing()) {
					if (prDao.update(rollList, uid)) {
						progressDialog.dismiss();
						runOnUiThread(succRunnable);
					}
					else {
						progressDialog.dismiss();
						runOnUiThread(errURunnable);
					}
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
			updateRoll();
	    }
	};
	
	// 连接错误线程
	private Runnable errRunnable = new Runnable() {
		@Override  
	    public void run() {
			Toast.makeText(RollActivity.this, "连接错误！请检查网络连接！", Toast.LENGTH_SHORT).show();
	    }
	};
	// 更新错误线程
	private Runnable errURunnable = new Runnable() {
		@Override  
	    public void run() {
			Toast.makeText(RollActivity.this, "更新错误！", Toast.LENGTH_SHORT).show();
	    }
	};
	
	/**
	 * 自定义成员对象
	 */
	
	
	/**
	 * 自定义方法
	 */
	private void initView() {
		backView = findViewById(R.id.Btn_Roll_Back);
		refreshView = findViewById(R.id.Btn_Roll_Refresh);
		rollListView = (ListView) findViewById(R.id.List_Roll_Main);
		if (rollList == null) {
			rollList = new ArrayList<Map<String, String>>();
		}
		updateRoll();
	}
	
	private void initListener() {
		backView.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpToPersonal();
			}
		});
		refreshView.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshRoll();
			}
		});
		
	}
	
	private void jumpToPersonal() {
		Intent intent=new Intent();
		intent.setClass(RollActivity.this, PersonalActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		finish();
	}
	
	private void refreshRoll() {
    	// 显示状态对话框
		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage(getResources().getString(R.string.loading_tip));
		progressDialog.setCancelable(true);
		progressDialog.show();
		
		// 开启连接线程
		new Thread(connRunnable).start();
	}
	
	private void updateRoll() {
		if (rollList == null) {
			return;
		}
		sAdapter = new SimpleAdapter(this, rollList, R.layout.list_item_personal_roll, 
				new String[] { "key", "value" }, 
				new int[] { R.id.Text_Roll_List_Key, R.id.Text_Roll_List_Value});
		
		rollListView.setAdapter(sAdapter);
		rollListView.setDivider(null);
	}
    
}
