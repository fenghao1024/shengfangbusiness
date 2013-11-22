package icedot.work.activity;

import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangSoap;
import icedot.work.common.GlobalData;
import icedot.work.shengfang.business.R;
import icedot.work.struct.UserInfo;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class RegisterActivity extends Activity 
{
	private ShengFangApplication _app;
	private UserInfo _userInfo;
	
	private EditText	_edit_account;
	private EditText	_edit_pwd;
	private EditText	_edit_repPwd;
	private EditText	_edit_name;
	private EditText	_edit_address;
	private RadioButton _radio_man;
	private RadioButton _radio_woman;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		_app = (ShengFangApplication)getApplication();		
		initView();
		initData();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void initData() 
	{
		_userInfo = new UserInfo();
	}
	
	private void initView() 
	{
		_edit_account = (EditText) findViewById(R.id.register_edit_phone);
		_edit_pwd = (EditText) findViewById(R.id.register_edit_pwd);
		_edit_repPwd = (EditText) findViewById(R.id.register_edit_reppwd);
		_edit_name = (EditText) findViewById(R.id.register_edit_name);
		_edit_address = (EditText) findViewById(R.id.register_edit_address);
		_radio_man = (RadioButton) findViewById(R.id.register_radiogroup_man);
		_radio_woman = (RadioButton) findViewById(R.id.register_radiogroup_woman);
	}
	
	public void btn_submit_OnClick(View v)
	{
		if(!checkData())
		{
			return;
		}
		getData();
		
		RegisterTask task = new RegisterTask();
		task.execute();
	}
	
	public void btn_back_onClick(View v)
	{
		finish();
	}

	private void getData()
	{
		_userInfo.set_account(_edit_account.getText().toString());
		_userInfo.set_pwd(_edit_pwd.getText().toString());
		if(_edit_name.getText().length() > 0)
		{
			_userInfo.set_name(_edit_name.getText().toString());
		}
		if(_edit_address.getText().length() > 0)
		{
			_userInfo.set_address(_edit_address.getText().toString());
		}
		
		if(_radio_man.isChecked())
		{
			_userInfo.set_sex(_radio_man.getText().toString());
		}
		else
		{
			_userInfo.set_sex(_radio_woman.getText().toString());
		}
	}

	private boolean checkData() 
	{
		if(_edit_account.getText().length() == 0 )
		{
			GlobalData.getCustomToast(this, this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"请输入您的电话号码", Toast.LENGTH_LONG).show();
			_edit_account.requestFocus();
			return false;
		}
		if(_edit_pwd.getText().length() == 0 ||
				_edit_repPwd.getText().length() == 0)
		{
			GlobalData.getCustomToast(this, this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"密码不能为空", Toast.LENGTH_LONG).show();
			_edit_pwd.requestFocus();
			return false;
		}
		if(_edit_repPwd.getText().length() == 0)
		{
			GlobalData.getCustomToast(this, this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"请再次输入您的密码", Toast.LENGTH_LONG).show();
			_edit_pwd.requestFocus();
			return false;
		}
		
		if(_edit_name.getText().toString().length() == 0)
		{
			GlobalData.getCustomToast(this, this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"请再次输入您的姓名", Toast.LENGTH_LONG).show();
			_edit_name.requestFocus();
			return false;
		}
		
		if(!_edit_pwd.getText().toString().equals(_edit_repPwd.getText().toString()))
		{
			GlobalData.getCustomToast(this, this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"密码验证不正确", Toast.LENGTH_LONG).show();
			_edit_repPwd.setText("");
			_edit_repPwd.requestFocus();
			return false;
		}
		
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) 
	{
		switch (id) 
		{
		case PROGRESS_DIALOG:
			return ProgressDialog.show(this, "", "正在注册. 请稍候...", true);

		default:
			return null;
		}
	}
	
	private static final int PROGRESS_DIALOG = 1;		
	
	private class RegisterTask extends AsyncTask<Void,Void,Integer>
	{
		private boolean showDlg = false;
		@Override
		protected void onPreExecute() 
		{
			if(!showDlg)
			{
				showDlg = true;
				showDialog(PROGRESS_DIALOG);
			}
		}
		@Override
		protected Integer doInBackground(Void... params)
		{
			ShengFangSoap soap = _app.getSoap();
			return soap.registerSoap(_userInfo);
		}
		
		@Override
		protected void onPostExecute(Integer result) 
		{
			if(showDlg)
			{
				dismissDialog(PROGRESS_DIALOG);
				showDlg = false;
			}
			switch(result)
			{
			case -1:
				GlobalData.getCustomToast(RegisterActivity.this,
						RegisterActivity.this.getResources().getDrawable(R.drawable.custom_toast_icon), 
						"内部错误,请与管理员联系!", Toast.LENGTH_LONG).show();
				break;
			case -2:
				GlobalData.getCustomToast(RegisterActivity.this,
						RegisterActivity.this.getResources().getDrawable(R.drawable.custom_toast_icon), 
						"无法连接服务器!", Toast.LENGTH_LONG).show();
				break;
			case 0:
				GlobalData.getCustomToast(RegisterActivity.this,
						RegisterActivity.this.getResources().getDrawable(R.drawable.custom_toast_icon), 
						"注册信息不完整，注册失败！", Toast.LENGTH_LONG).show();
				break;
			case 1:
				GlobalData.getCustomToast(RegisterActivity.this,
						RegisterActivity.this.getResources().getDrawable(R.drawable.custom_toast_icon), 
						"用户名已存在，注册失败！", Toast.LENGTH_LONG).show();
				break;
			case 2:
				GlobalData.getCustomToast(RegisterActivity.this,
						RegisterActivity.this.getResources().getDrawable(R.drawable.custom_toast_icon), 
						"注册成功", Toast.LENGTH_LONG).show();
				RegisterActivity.this.finish();
				break;
			case 3:
				GlobalData.getCustomToast(RegisterActivity.this,
						RegisterActivity.this.getResources().getDrawable(R.drawable.custom_toast_icon), 
						"注册失败！", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			super.onPostExecute(result);
		}
	}
}
