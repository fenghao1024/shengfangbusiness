package icedot.work.activity;

import icedot.work.application.ShengFangAppConfig;
import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangSoap;
import icedot.work.common.GlobalData;
import icedot.work.common.UpdateAPK;
import icedot.work.shengfang.business.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity
{
	private ShengFangApplication _app;
	private ShengFangAppConfig _appConfig;
	private AutoCompleteTextView  _edit_account;
	private EditText  _edit_password;
	private Button _btn_login;
	private Button _btn_register;
	private AlertDialog _backDlg;
	private CheckBox _check_saveUser;
	private Dialog _taskDlg = null;	
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		_app = (ShengFangApplication)getApplication();
		_appConfig = _app.getAppConfig();

		initView();
		initData();
		
		if(_app.is_updateApp())
		{
			UpdateAPK updateApk = new UpdateAPK(LoginActivity.this);
			updateApk.set_updateTitle("黔商通更新");
			updateApk.set_updateMsg("最新的版本为 " + _app.getSoap().get_appVer() +
					",是否更新?");
			updateApk.set_saveFileName("ShengFangBusiness.apk");
			updateApk.setApkUrl(_app.getSoap().get_downUrl());
			updateApk.checkUpdateInfo();
		}
	}

	@Override
	protected void onRestart() 
	{
		super.onRestart();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		//super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (_backDlg == null )
			{
				_backDlg = GlobalData.exitNoticeDialog(this);
			}
			else
			{
				if(!_backDlg.isShowing())
					_backDlg.show();
			}
			
			 return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void btn_login_OnClick(View v)
	{
		String account = _edit_account.getText().toString().trim();
		String password = _edit_password.getText().toString().trim();
		
		if(account.length() <= 0)
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"用户名不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if(password.length() <= 0)
		{
			GlobalData.getCustomToast(this,
					this.getResources().getDrawable(R.drawable.custom_toast_icon), 
					"密码不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		
		_btn_login.setVisibility(View.GONE);
		_btn_register.setVisibility(View.GONE);
		
		if(_taskDlg == null)
		{
			_taskDlg = ProgressDialog.show(this, "登录", "正在登录中. 请稍候...", true);
		}
		else
		{
			_taskDlg.show();
		}
		LoginTask task = new LoginTask();
		task.execute(account,password);
	}
	
	public void btn_register_OnClick(View v)
	{		
		Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
		startActivity(i);
	}
	
	private void initView() 
	{
		_edit_account = (AutoCompleteTextView)findViewById(R.id.login_edit_account);
		_edit_password = (EditText)findViewById(R.id.login_edit_password);
		_btn_login = (Button)findViewById(R.id.login_btn_login);
		_btn_register = (Button)findViewById(R.id.login_btn_register);
		_check_saveUser = (CheckBox)findViewById(R.id.login_check_saveuser);
	}
	
	private void initData() 
	{
		if(_appConfig.get_username().length() > 0)
		{
			_edit_account.setText(_appConfig.get_username());
			_edit_password.setText(_appConfig.getPassword());
		}
		
		_check_saveUser.setChecked(_appConfig.get_isAutoCheck());		
	}

	private class LoginTask extends AsyncTask<String,String,Integer>
	{
		@Override
		protected Integer doInBackground(String... params) 
		{
			ShengFangSoap soap = _app.getSoap();
			int ret = soap.loginSoap(params[0], params[1]);
			return ret;
		}
		
		@Override
		protected void onPostExecute(Integer result) 
		{
			if(_taskDlg != null && _taskDlg.isShowing())
			{
				_taskDlg.dismiss();
			}
			
			switch(result)
			{
			case 1:  //成功
			{
				if(_check_saveUser.isChecked())
				{
					_appConfig.set_username(_edit_account.getText().toString());
					_appConfig.setPassword(_edit_password.getText().toString());
				}
				else
				{
					_appConfig.set_username("");
					_appConfig.setPassword("");
				}
				_appConfig.set_isAutoCheck(_check_saveUser.isChecked());
				_appConfig.commit();
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
				break;
			case 2:	//自动更新的结果
				break;
			case -1:  //帐号或者密码错误
			{
				GlobalData.getCustomToast(LoginActivity.this,
						LoginActivity.this.getResources().getDrawable(R.drawable.custom_toast_icon), 
						"帐号或者密码错误!", Toast.LENGTH_LONG).show();
				

				_btn_login.setVisibility(View.VISIBLE);
				_btn_register.setVisibility(View.VISIBLE);

			}
				break;
			default: //无法连接服务器
			{
				GlobalData.getCustomToast(LoginActivity.this,
						LoginActivity.this.getResources().getDrawable(R.drawable.custom_toast_icon), 
						"无法连接服务器!", Toast.LENGTH_LONG).show();
				_btn_login.setVisibility(View.VISIBLE);
				_btn_register.setVisibility(View.VISIBLE);
			}
				break;
			}
			super.onPostExecute(result);
		}
	}
}
