package icedot.work.activity;

import org.json.JSONException;

import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangSoap;
import icedot.work.common.GlobalData;
import icedot.work.shengfang.business.R;
import icedot.work.shengfang.business.R.layout;
import icedot.work.shengfang.business.R.menu;
import icedot.work.struct.View_Selector;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RechargeActivity extends Activity {
	
	
	EditText amountEditText;
	Button btnCharge;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);
		
		initView();
		
		Bundle extras = getIntent().getExtras();
		if(extras != null)
		{
			String text = extras.getString("recharge_amount");
			if(text != null)
			{
				this.amountEditText.setText(text);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recharge, menu);
		return true;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		Bundle extras = getIntent().getExtras();
		if(extras == null)
			return;
		
		if(!extras.getBoolean("isAlibabaReturn"))
		{
			return ;
		}
		
		String strResult = extras.getString("msg");
		int intResult = extras.getInt("tradeStatus");
		String retMsg = extras.getString("tradeStatusString");
		
		Drawable drawable = this.getResources().getDrawable(R.drawable.custom_toast_icon);

		//Toast.makeText(getApplicationContext(), retMsg, 4).show();

		ShengFangApplication _app = (ShengFangApplication)getApplication();
		double x = _app.getSoap().get_userInfo().get_money();
		
		if(intResult == 9000)
		{
			double doubleRechargeAmount = extras.getDouble("total_fee"); 
			x += doubleRechargeAmount;
			_app.getSoap().get_userInfo().set_money(x);
			
			drawable = this.getResources().getDrawable(R.drawable.smile);
			
			updateBalance(doubleRechargeAmount);

		}
		
		GlobalData.getCustomToast(this,
				drawable, retMsg, Toast.LENGTH_SHORT).show();
		
		GlobalData.getCustomToast(this, 
				this.getResources().getDrawable(R.drawable.rmbcash),
				"您当前的余额：" + String.valueOf(x), Toast.LENGTH_SHORT).show();
		
	}
	
	private void updateBalance(double value)
	{
		MsgTask mt = new MsgTask();
		mt.execute(value);
	}
	
	public void initView()
	{
		amountEditText = (EditText)findViewById(R.id.edit_recharge_amount);
		btnCharge = (Button)findViewById(R.id.btn_recharge_alipay);
	}
	
	public void btn_back_onClick(View v)
	{
		finish();
	}
	
	public void btn_recharge_in_alipay(View v)
	{
		String amoutStr = amountEditText.getText().toString();
		double x = 0;
		try
		{
			x = Double.valueOf(amoutStr);
		}
		catch(NumberFormatException nfe)
		{
			x = -1;
		}
		
		if(x <= 0)
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"请输入一个可以充值的金额", Toast.LENGTH_LONG).show();

			return ;
		}
		
		Intent intent = new Intent(RechargeActivity.this,AlipayActivity.class);
		intent.putExtra("fromActivity", View_Selector.PayMgr);
		
		ShengFangApplication _app = (ShengFangApplication)getApplication();
	
		intent.putExtra("price", x);
		intent.putExtra("title", "黔商通余额充值" );
		//intent.putExtra("type", "{"count":1,"userid":"fdc36aa2c2aa4696b357e8e1d168421f","list":[{"type":"次商圈B套餐（周末）","id":5,"price":500}]}");
		intent.putExtra("type", "balanceRecharge");
		startActivity(intent);
		finish();
	}
	
	 private class MsgTask extends AsyncTask<Double,Void,Integer>
	 {
		 private double balanceBackup = 0;
		 
		 @Override
		 protected void onPreExecute() 
		 {
			 super.onPreExecute();
			 balanceBackup = 0;
		 }
		 @Override
		 protected Integer doInBackground(Double... arg0) 
		 {
			 balanceBackup = Double.valueOf(arg0[0]);
			 ShengFangApplication _app = (ShengFangApplication)getApplication();
			 ShengFangSoap soap = _app.getSoap();
			 Integer ret = soap.defaultRechargeInterface_inSoap(balanceBackup);
			 return ret;
		 }
		 @Override
		 protected void onPostExecute(Integer result) 
		 {
			 switch(result)
				{
				case 0:
					
					break;
				case 1:
					GlobalData.getCustomToast(RechargeActivity.this, 
							null,
							"黔商通服务器数据库错误，请联系管理员", Toast.LENGTH_LONG).show();

					break;
				case -3:
					GlobalData.getCustomToast(RechargeActivity.this, 
							null,
							"黔商通服务器出错，请联系管理员", Toast.LENGTH_LONG).show();
					break;
				case -2:
					GlobalData.getCustomToast(RechargeActivity.this, 
							null,
							"无法连接黔商通服务器，请联系管理员", Toast.LENGTH_LONG).show();

					break;

				}
				super.onPostExecute(result);
		 }	
	 } 

}
