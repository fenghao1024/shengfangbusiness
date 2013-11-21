package icedot.work.activity;

import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangSoap;
import icedot.work.common.GlobalData;
import icedot.work.shengfang.business.R;
import icedot.work.struct.UserInfo;
import icedot.work.struct.View_Selector;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalActivity extends Activity
{
	private ShengFangApplication _app;
	
	private TextView _text_userID;
	private TextView _text_userName;
	//private TextView _text_sex;
	private TextView _text_address;
	private TextView _text_msgCount;
	private TextView _text_money;
	
	private Button btnRecharge;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalinfo);
		
		_app = (ShengFangApplication)getApplication();

		initView();
		initData();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		initData();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void btn_back_onClick(View v)
	{
		finish();
	}
	
	public void btn_recharge_onclick(View v)
	{
		Intent intent = new Intent(PersonalActivity.this,RechargeActivity.class);
		startActivity(intent);
	}

	private void initView() 
	{
		_text_userID = (TextView) findViewById(R.id.personal_info_account);
		_text_userName = (TextView) findViewById(R.id.personal_info_name);
		//_text_sex = (TextView) findViewById(R.id.personal_info_sex);
		_text_address = (TextView) findViewById(R.id.personal_info_address);
		_text_msgCount = (TextView) findViewById(R.id.personal_info_msgcount);
		_text_money = (TextView) findViewById(R.id.personal_info_money);
		btnRecharge = (Button)findViewById(R.id.recharge_btn);
	}
	private void initData() 
	{
		ShengFangSoap soap = _app.getSoap();
		UserInfo userInfo = soap.get_userInfo();
		if(userInfo != null && userInfo.get_account().length() > 0)
		{
			_text_userID.setText(soap.get_userInfo().get_account());
			_text_userName.setText(soap.get_userInfo().get_name());
			//_text_sex.setText(soap.get_userInfo().get_sex());
			_text_address.setText(soap.get_userInfo().get_address());
			_text_msgCount.setText(String.valueOf(soap.get_userInfo().get_count()));
			_text_money.setText(String.valueOf(soap.get_userInfo().get_money()));
		}
		else
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"无法获取个人信息,请与管理员联系!", Toast.LENGTH_LONG).show();
		}
	}
}
