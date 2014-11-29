package com.nsmss.scuol.login;

import com.nsmss.scuol.R;
import com.nsmss.scuol.bean.GlobalInfo;
import com.nsmss.scuol.bean.UserData;
import com.nsmss.scuol.common.NetHelper;
import com.nsmss.scuol.dao.GlobalInfoDao;
import com.nsmss.scuol.dao.UserDataDao;
import com.nsmss.scuol.main.MainActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @version 1.1
 * @author LMD
 * @modify LT
 * 用于登录
 */
public class LoginActivity extends Activity {
	
	/**
	 *  静态成员变量
	 */
	private static Context context;
	
	/**
	 * UI相关成员变量
	 */
	private ProgressDialog loadDialog;
	
	/**
	 * View相关成员变量
	 */
	private EditText numText;
	private EditText passwdText;
	private Button loginButton;
	private CheckBox autoBox;
	private CheckBox passwdBox;
	
	/**
	 * Dao成员变量
	 */
	private GlobalInfoDao gInfoDao;
	private UserDataDao uDataDao;
	
	/**
	 * 数据模型变量
	 */
	private GlobalInfo gInfo;
	private UserData uData;

	/**
	 * 数据存储变量
	 */
	private String numStr;
	private String passwdStr;

	/**
	 * 状态变量
	 */
	

	/**
	 * 临时变量
	 */
	private String errCodeStr;
	
	
	/**
	 * Activity回调函数
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// 继承父类方法，绑定View
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// 初始化context
		context = getApplicationContext(); 
		
		// 初始化View成员变量
		initView();
		initListener();
		
		// 初始化Dao成员变量
		gInfoDao = new GlobalInfoDao(context);
		uDataDao = new UserDataDao(context);
		
		// 初始化数据模型变量
		gInfo = gInfoDao.query();
		
		// 初始化存储变量
		numStr = new String();
		passwdStr = new String();
		
		// 初始化状态变量
		
		// 初始化临时变量
		
		// 自定义函数
		initInput();
		
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
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 线程对象
	 */
	private Runnable loginRunnable = new Runnable() {
		@Override
		public void run() {
			NetHelper netHelper = new NetHelper();
			errCodeStr = netHelper.login(numStr, passwdStr);
			if (loadDialog.isShowing()) {
				loadDialog.dismiss();
				if (errCodeStr == null || errCodeStr.equals("100") || 
						errCodeStr.equals("103") || errCodeStr.equals("104")) {
					runOnUiThread(connErrRunnable);
				}
				else if (errCodeStr.equals("101")) {
					runOnUiThread(numErrRunnable);
				}
				else if (errCodeStr.equals("102")) {
					runOnUiThread(passwdErrRunnable);
				}
				else {
					int returnCode = initUser(errCodeStr);
					if (returnCode == 0) {
						jumpToMain();
					}
					else {
						errCodeStr = returnCode+"";
						runOnUiThread(dbErrRunnable);
					}
				}
			}
		}
	};
	
	private Runnable numErrRunnable = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(LoginActivity.this, "你输入的学号不存在，请重新输入", Toast.LENGTH_SHORT).show();
		}
	};
	
	private Runnable passwdErrRunnable = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(LoginActivity.this, "您输入的密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
		}
	};
	
	private Runnable connErrRunnable = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(LoginActivity.this, "网络连接错误，错误代码"+errCodeStr, Toast.LENGTH_SHORT).show();
		}
	};
	
	private Runnable dbErrRunnable = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(LoginActivity.this, "信息存储错误，错误代码"+errCodeStr, Toast.LENGTH_SHORT).show();
		}
	};
	
	
	/**
	 * 自定义成员对象
	 */
	
	
	/**
	 * 自定义方法
	 */
    protected void initView() {
    	numText = (EditText)findViewById(R.id.EditText_Login_Num);
    	numText.addTextChangedListener(watcher);
		passwdText = (EditText)findViewById(R.id.EditText_Login_Passwd);
		passwdText.addTextChangedListener(watcher);
		loginButton = (Button)findViewById(R.id.Button_Login_Login);
		autoBox = (CheckBox)findViewById(R.id.CheckBox_Login_Auto);
		passwdBox = (CheckBox)findViewById(R.id.CheckBox_Login_Passwd);
		numText.setFocusable(true);
		numText.setFocusableInTouchMode(true);
		passwdText.setFocusable(true);
		passwdText.setFocusableInTouchMode(true);
    }
    
    protected void initListener() {
    	autoBox.setOnClickListener(new OnClickListener() {
    		@Override
			public void onClick(View v) {
				if (autoBox.isChecked()) {
					passwdBox.setChecked(true);
				}
			}
		});
    	passwdBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!passwdBox.isChecked()) {
					autoBox.setChecked(false);
				}
			}
		});
    	loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				numStr = numText.getText().toString();
				passwdStr = passwdText.getText().toString();
				if (numStr.length()<6 ) {
					Toast.makeText(getApplicationContext(), "请输入正确的学号！", Toast.LENGTH_SHORT).show();
					loginButton.setEnabled(false);  //使登录按钮不可用
					passwdText.clearFocus();
					numText.requestFocus();
				}
				else if (passwdStr.length()<1) {
					Toast.makeText(getApplicationContext(), "请输入密码！", Toast.LENGTH_SHORT).show();
					loginButton.setEnabled(false);  //使登录按钮不可用
					numText.clearFocus();
					passwdText.requestFocus();
				}
				else {
					loginConn();
				}
			}
		});
    }
    
    /**
     * 定义文本编辑框监视器：watcher
     */
    private TextWatcher watcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
			// TODO Auto-generated method stub
		}
		
		@Override
		public void afterTextChanged(Editable arg0)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			// TODO Auto-generated method stub
			loginButton.setEnabled(true); //编辑后登录按钮可用
		}  
  };  
  
    /**
     * 判断如果最后登录的用户选择了自动登录
     * 则跳转到主界面
     */
    protected void initInput() {
		if (gInfo != null) {
			int uid = gInfo.getActiveUserUid();
			if (uid != 0) {
				uData = uDataDao.query(uid);
				if (uData != null) {
					if (uData.getAutologin() == 1) {
						// TODO 跳转到主界面
						jumpToMain();
					}
					if (uData.getSavepasswd() == 1) {
						numText.setText(uData.getNum());
					}
				}
			}
		}
	}
    
    /**
     * 显示登录状态图标
     * 开启连接线程
     */
    protected void loginConn() {
    	loadDialog = new ProgressDialog(this);
    	loadDialog.setIndeterminate(true);
    	loadDialog.setMessage("登录中...");
    	loadDialog.setCancelable(true);
    	loadDialog.show();
    	new Thread(loginRunnable).start();
	}
    
    /**
     * 初始化UserData对象，存入数据库，并更新global_info
     * @param session 从NetHelper返回的JSESSIONID的值
     * @return 成功返回0，否则返回错误代码
     */
    protected int initUser(String session) {
    	uData = new UserData();
    	uData.setNum(numStr);
    	uData.setPasswd(passwdStr);
    	uData.setSession(session);
    	uData.setLastlogin( (int)((System.currentTimeMillis())/1000) );
    	uData.setLastlogout(0);
    	if (autoBox.isChecked()) {
			uData.setAutologin(1);
    	}
    	else {
			uData.setAutologin(0);
		}
    	if (passwdBox.isChecked()) {
			uData.setSavepasswd(1);
		}
    	else {
    		uData.setSavepasswd(0);
    	}
    	uData.setHeadshot("");
    	int uid = uDataDao.insert(uData);
    	if (uid == 0) {
			return 201;
		}
    	
    	gInfo.setActiveUserUid(uid);
    	if(gInfoDao.insert(gInfo)) {
    		return 0;
    	}
    	else {
    		return 202;
    	}
	}
    
    /**
     * 跳转到MainActivity
     */
    protected void jumpToMain() {
   	 	Intent intent = new Intent(LoginActivity.this, MainActivity.class); 
        startActivity(intent); 
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        finish(); 
	}
    
}
