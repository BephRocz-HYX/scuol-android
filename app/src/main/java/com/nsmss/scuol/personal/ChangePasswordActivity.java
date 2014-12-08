package com.nsmss.scuol.personal;

import com.nsmss.scuol.R;
import com.nsmss.scuol.bean.GlobalInfo;
import com.nsmss.scuol.bean.UserData;
import com.nsmss.scuol.common.NetHelper;
import com.nsmss.scuol.dao.GlobalInfoDao;
import com.nsmss.scuol.dao.UserDataDao;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity
{
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
	View backView;
	View refreshView;
	private EditText oldPasswdEditT;
	private EditText newPasswdEditT1;
	private EditText newPasswdEditT2;
	private Button submitButton;
	
	/**
	 * Dao成员变量
	 */
	GlobalInfoDao gDao;
	private UserDataDao uDao;
	
	/**
	 * 数据模型变量
	 */
	GlobalInfo gInfo;
	private UserData uData;

	/**
	 * 数据存储变量
	 */
	private String oldPasswdStr;
	private String newPasswdStr1;
	private String newPasswdStr2;

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
		setContentView(R.layout.activity_change_password);
		
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
			jumpToPersonal();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initView()
	{
		backView = findViewById(R.id.Btn_Change_Password_Back);
		refreshView = findViewById(R.id.Btn_Change_Password_Refresh);
		
		oldPasswdEditT = (EditText) findViewById(R.id.edittext_change_password_oldpasswd);
		oldPasswdEditT.addTextChangedListener(watcher);
		newPasswdEditT1 = (EditText) findViewById(R.id.edittext_change_password_newpasswd1);
		newPasswdEditT1.addTextChangedListener(watcher);
		newPasswdEditT2 = (EditText)findViewById(R.id.edittext_change_password_newpasswd2);
		newPasswdEditT2.addTextChangedListener(watcher);
		submitButton = (Button) findViewById(R.id.button_change_password_submit);
	}
	private void initListener()
	{
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
	    submitButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				oldPasswdStr = oldPasswdEditT.getText().toString();
				newPasswdStr1 = newPasswdEditT1.getText().toString();
				newPasswdStr2 = newPasswdEditT2.getText().toString();
				if (oldPasswdStr.length()<1) {
					Toast.makeText(getApplicationContext(), "请输入原始密码！", Toast.LENGTH_SHORT).show();
					submitButton.setEnabled(false);  //使登录按钮不可用
					newPasswdEditT1.clearFocus();
					newPasswdEditT2.clearFocus();
					oldPasswdEditT.requestFocus();
				}
				else if (newPasswdStr1.length()<1||newPasswdStr2.length()<1) {
					Toast.makeText(getApplicationContext(), "请输入新设密码！", Toast.LENGTH_SHORT).show();
					submitButton.setEnabled(false);  //使登录按钮不可用
					if (newPasswdStr1.length()<1)
					{
						oldPasswdEditT.clearFocus();
						newPasswdEditT2.clearFocus();
						newPasswdEditT1.requestFocus();
					}
					else {
						oldPasswdEditT.clearFocus();
						newPasswdEditT1.clearFocus();
						newPasswdEditT2.requestFocus();
					}
				}
				else if (!newPasswdStr1.equals(newPasswdStr2)) {
					Toast.makeText(getApplicationContext(), "新设密码输入不一致，新重新输入！", Toast.LENGTH_SHORT).show();
					submitButton.setEnabled(false);  //使登录按钮不可用
					newPasswdEditT1.setText("");
					newPasswdEditT2.setText("");
					
					oldPasswdEditT.clearFocus();
					newPasswdEditT2.clearFocus();
					newPasswdEditT1.requestFocus();
				}
				else {
					submitConn();
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
			submitButton.setEnabled(true); //编辑后登录按钮可用
		}  
  };  
  
	private void jumpToPersonal() {
		Intent intent=new Intent();
		intent.setClass(ChangePasswordActivity.this, PersonalActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		finish();
	}
	
	private void submitConn()
	{
		loadDialog = new ProgressDialog(this);
    	loadDialog.setIndeterminate(true);
    	loadDialog.setMessage("提交中...");
    	loadDialog.setCancelable(true);
    	loadDialog.show();
    	new Thread(submitRunnable).start();
	}
	
	private void refreshRoll() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * 线程对象
	 */
	
	
	private Runnable submitRunnable = new Runnable() {
		@Override
		public void run() {
			NetHelper netHelper = new NetHelper();
			errCodeStr = netHelper.changePassword(uData,oldPasswdStr, newPasswdStr1,newPasswdStr2);
			if (loadDialog.isShowing()) {
				loadDialog.dismiss();
				if (errCodeStr == null || errCodeStr.equals("100") || 
						errCodeStr.equals("103") || errCodeStr.equals("104")) {
					runOnUiThread(connErrRunnable);
				}
				else if (errCodeStr.equals("101")) {
					//runOnUiThread(numErrRunnable);
				}
				else if (errCodeStr.equals("102")) {
					//runOnUiThread(passwdErrRunnable);
				}
				else {
					int returnCode = correctUser(errCodeStr);
					if (returnCode == 0) {
						runOnUiThread(successRunnable);
						jumpToPersonal();
					}
					else {
						errCodeStr = returnCode+"";
						runOnUiThread(dbErrRunnable);
					}
				}
			}
		}
	};
	
	/**
	 * 输出返回的提示信息
	 */
	private Runnable successRunnable = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(ChangePasswordActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
		}
	};
	private Runnable inputErrRunnable = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(ChangePasswordActivity.this, "请输入相关信息！", Toast.LENGTH_SHORT).show();
		}
	};
	private Runnable oldpasswdErrRunnable = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(ChangePasswordActivity.this, "你输入的原始密码错误，请重新输入", Toast.LENGTH_SHORT).show();
		}
	};
	
	private Runnable passwdErrRunnable = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(ChangePasswordActivity.this, "您输入的密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
		}
	};
	
	private Runnable connErrRunnable = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(ChangePasswordActivity.this, "网络连接错误，错误代码"+errCodeStr, Toast.LENGTH_SHORT).show();
		}
	};
	
	private Runnable dbErrRunnable = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(ChangePasswordActivity.this, "信息存储错误，错误代码"+errCodeStr, Toast.LENGTH_SHORT).show();
		}
	};
	
    /**
     * 修正UserData对象，存入数据库，并更新global_info
     * @param session 从NetHelper返回的JSESSIONID的值
     * @return 成功返回0，否则返回错误代码
     */
	private int correctUser(String session)
	{
    	//uData.setPasswd(newPasswdStr1);
    	uData.setSession(session);
    	
    	if (uDao.update(uData)) {
    		return 0;
    		//gInfo.setActiveUserUid(uid);
        	//if(gInfoDao.insert(gInfo)) {
        	//return 0;
        	//}
        	//else {
			//return 202;
			//}
		}
    	else {
    		return 202;
    	}
	}
	
	/**
	 * 自定义成员对象
	 */
	
	
	/**
	 * 自定义方法
	 */

}
