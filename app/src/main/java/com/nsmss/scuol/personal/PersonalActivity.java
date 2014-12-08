package com.nsmss.scuol.personal;

import java.text.DecimalFormat;
import java.util.Calendar;

import com.nsmss.scuol.R;
import com.nsmss.scuol.bean.GlobalInfo;
import com.nsmss.scuol.bean.PersonalInfo;
import com.nsmss.scuol.bean.UserData;
import com.nsmss.scuol.common.NetHelper;
import com.nsmss.scuol.dao.GlobalInfoDao;
import com.nsmss.scuol.dao.PersonalInfoDao;
import com.nsmss.scuol.dao.UserDataDao;
import com.nsmss.scuol.main.MainActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalActivity extends Activity {

	/**
	 *  静态成员变量
	 */
	private static Context context;
	
	/**
	 * UI相关成员变量
	 */
	private ProgressDialog progressDialog;
	
	/**
	 * View相关成员变量
	 */
	private View backView;
	private View refreshView;
	private View bannerView;
	private TextView nameTextView;
	private TextView numberTextView;
	private TextView daysTextView;
	private TextView percentTextView;
	private TextView avarageTextView;
	private TextView GPATextView;
	private View rollView;
	private View planView;
	private View passwdView;
	private View checkView;
	
	/**
	 * Dao成员变量
	 */
	private GlobalInfoDao gDao;
	private UserDataDao uDao;
	private PersonalInfoDao pDao;
	
	/**
	 * 数据模型变量
	 */
	private GlobalInfo gInfo;
	private UserData uData;
	private PersonalInfo pInfo;

	/**
	 * 数据存储变量
	 */
	

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
		setContentView(R.layout.activity_personal);
		
		// 初始化context
		context = getApplicationContext(); 
		
		// 初始化View成员变量
		
		// 初始化Dao成员变量
		gDao = new GlobalInfoDao(context);
		uDao = new UserDataDao(context);
		pDao = new PersonalInfoDao(context);
		
		// 初始化数据模型变量
		gInfo = gDao.query();
		uid = gInfo.getActiveUserUid();
		uData = uDao.query(uid);
		pInfo = pDao.query(uid);
		
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
    // 连接线程
    private Runnable connRunnable = new Runnable() {
    	@Override  
		public void run() {
			NetHelper nHelper = new NetHelper();
			pInfo = nHelper.getPersonalInfo(uData);
    		// 如果连接成功，返回了更新数据
    		if (pInfo != null) {
    			// 判断状态对话框是否显示
				if (progressDialog.isShowing()) {
					if (pDao.update(pInfo)) {
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
			updatePInfo();
	    }
	};
	
	// 连接错误线程
	private Runnable errRunnable = new Runnable() {
		@Override  
	    public void run() {
			Toast.makeText(PersonalActivity.this, "连接错误！请检查网络连接！", Toast.LENGTH_SHORT).show();
	    }
	};
	// 更新错误线程
	private Runnable errURunnable = new Runnable() {
		@Override  
	    public void run() {
			Toast.makeText(PersonalActivity.this, "更新错误！", Toast.LENGTH_SHORT).show();
	    }
	};
	
	/**
	 * 自定义成员对象
	 */
	
	
	/**
	 * 自定义方法
	 */
	private void initView() {
		backView = findViewById(R.id.Btn_Personal_Back);
		refreshView = findViewById(R.id.Btn_Personal_Refresh);
		
		bannerView = findViewById(R.id.View_Personal_Banner);
    	Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		if (month==0||month==1||month==11) {
			bannerView.setBackgroundResource(R.drawable.bg_personal_banner4);
		}
		else if (month==2||month==3||month==4) {
			bannerView.setBackgroundResource(R.drawable.bg_personal_banner1);
		}
		else if (month==5||month==6||month==7) {
			bannerView.setBackgroundResource(R.drawable.bg_personal_banner2);
		}
		else {
			bannerView.setBackgroundResource(R.drawable.bg_personal_banner3);
		}
		
    	nameTextView = (TextView) this.findViewById(R.id.Text_Personal_Name);
    	numberTextView = (TextView) this.findViewById(R.id.Text_Personal_Number);
    	daysTextView = (TextView) this.findViewById(R.id.Text_Personal_Days);
    	percentTextView = (TextView) this.findViewById(R.id.Text_Personal_Percent);
    	avarageTextView = (TextView) this.findViewById(R.id.Text_Personal_Score);
    	GPATextView = (TextView) this.findViewById(R.id.Text_Personal_Credit);
    	
    	rollView = findViewById(R.id.Btn_Personal_Roll);
    	planView = findViewById(R.id.Btn_Personal_Plan);
    	passwdView = findViewById(R.id.Btn_Personal_Passwd);
    	checkView = findViewById(R.id.Btn_Personal_Check);
    	
    	if (uData != null) {
    		numberTextView.setText("学号："+uData.getNum());
    		updatePInfo();
		}

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
				refreshPInfo();
			}
		});
		rollView.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpToRoll();
			}
		});
		passwdView.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpToPassword();
			}
		});
	}
	
	private void jumpToMain() {
		Intent intent=new Intent();
		intent.setClass(PersonalActivity.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		finish();
	}
	
	private void jumpToRoll() {
		Intent intent=new Intent();
		intent.setClass(PersonalActivity.this, RollActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		finish();
	}
	
	private void jumpToPassword() {
		Intent intent=new Intent();
		intent.setClass(PersonalActivity.this, ChangePasswordActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		finish();
	}
	
	private void refreshPInfo() {
    	// 显示状态对话框
		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage(getResources().getString(R.string.loading_tip));
		progressDialog.setCancelable(true);
		progressDialog.show();
		
		// 开启连接线程
		new Thread(connRunnable).start();
	}
	
	private void updatePInfo() {
		if (pInfo == null) {
			return;
		}
		DecimalFormat dFormat = new DecimalFormat("##0.00"); 
    	nameTextView.setText(pInfo.getName()+"，你好");
    	daysTextView.setText(pInfo.getDays()+"");
    	percentTextView.setText((int)(pInfo.getPercent())+"%");
    	avarageTextView.setText(dFormat.format(pInfo.getAvarage()));
    	GPATextView.setText(dFormat.format(pInfo.getGpa()));
	}
}
