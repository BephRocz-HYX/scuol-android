package com.nsmss.scuol.personal;

import com.nsmss.scuol.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

public class PlanActivity extends Activity {

	/**
	 *  静态成员变量
	 */
	private static Context context;
	
	/**
	 * UI相关成员变量
	 */
	
	
	/**
	 * View相关成员变量
	 */
	
	
	/**
	 * Dao成员变量
	 */
	
	
	/**
	 * 数据模型变量
	 */
	

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
		setContentView(R.layout.activity_login);
		
		// 初始化context
		context = getApplicationContext(); 
		
		// 初始化View成员变量
		
		// 初始化Dao成员变量
		
		// 初始化数据模型变量
		
		// 初始化状态变量
		
		// 初始化临时变量
		
		// 自定义函数
		
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
    

}
