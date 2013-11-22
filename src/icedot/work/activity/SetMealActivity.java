package icedot.work.activity;

import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangSoap;
import icedot.work.common.GlobalData;
import icedot.work.shengfang.business.R;
import icedot.work.struct.SetMealInfo;
import icedot.work.struct.View_Selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.MobileSecurePayHelper;

public class SetMealActivity extends Activity
{
	private ShengFangApplication _app = null;
	private List<SetMealInfo>  _mealInfoList;
	private List<SetMealInfo>  _payInfoList;
	private Double _allPrice;
	//private AlertDialog _backDlg = null;
	
	
	private TableLayout _table_setmealinfo;		//套餐信息表
	private TableLayout _table_pay;				//购买套餐表
	private TableLayout _table_payhead;			//购买套餐表
	private TextView _text_allPrice;			//总价
	
	boolean isBuySucceed = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setmeal);
		
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		mspHelper.detectMobile_sp();
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
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		RefrashTask task = new RefrashTask();
		task.execute();
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
	
	private void initView() 
	{
		_table_setmealinfo = (TableLayout) findViewById(R.id.setmeal_table_setmeal);
		_table_pay = (TableLayout)findViewById(R.id.setmeal_table_pay);
		_table_pay.setVisibility(View.GONE);
		_table_payhead = (TableLayout)findViewById(R.id.setmeal_table_payhead);
		_table_payhead.setVisibility(View.GONE);
		_text_allPrice = (TextView)findViewById(R.id.setmeal_text_allprice);
	}
	
	private void initData() 
	{
		_mealInfoList = _app.getSoap().get_mealInfoList();
		_payInfoList = new ArrayList<SetMealInfo>();
		_allPrice = 0.0;
		
		for(int i = 0; i < _mealInfoList.size();i++)
		{
			addMealRow(i,_mealInfoList.get(i));
		}
		updatePrice();
	}
	
	public void btn_back_onClick(View v)
	{
		finish();
	}
	
	public void btn_clear_onClick(View v)
	{
		_allPrice = 0.0;
		_table_pay.removeAllViews();
		_payInfoList.clear();
		updatePrice();
	}
	
	private void buy(double balance)
	{
		_app.getSoap().get_userInfo().set_money(balance);
		String strOrder[] = getOrder(_payInfoList);
		if(strOrder[0] == "")
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.smile),
					"我擦嘞，出错了，请重启客户端！", 0).show();
			
			return ;
		}
		BuyTask task = new BuyTask();
		task.execute(strOrder);
	}
	
	private String[] getOrder(List<SetMealInfo> list)
	{
		String order[] = {"", ""};
		
		//orderList
		
		for(int i = 0; i < list.size(); i++)
		{
			SetMealInfo sm = list.get(i);
			for(int j = i + 1; j < list.size(); j++)
			{
				if(sm.get_ID() == list.get(j).get_ID())
				{
					sm.set_count(sm.get_count() + 1);
					list.remove(j);
					j--;
				}
			}
		}
		
		for(int i = 0; i < list.size(); i++)
		{
			order[0] += list.get(i).get_ID();
			order[0] += "_";
			order[0] += list.get(i).get_count();
			order[0] += ",";
			
			order[1] += list.get(i).get_ID();
			order[1] += ",";
		}

		return order;
	}

	public void btn_submit_onClick(View v)
	{
		//balance : 余额 ; 牛津词典中: [countable, usually singular] the amount that is left after taking numbers or money away from a total
		double balance = _app.getSoap().get_userInfo().get_money();
		balance = balance - _allPrice;
		
		updatePrice();
		if(balance >= 0)
		{
			this.buy(balance);
		}
		else
		{
			Double d = Math.abs(balance);
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"余额不够，请先充值。", 0).show();
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.smile),
					"建议充值金额：" + String.valueOf(d.intValue()), 0).show();
			
			Intent intent = new Intent(SetMealActivity.this,RechargeActivity.class);
			intent.putExtra("recharge_amount", String.valueOf(d.intValue()));
			intent.putExtra("isAlibabaReturn", false);
			startActivity(intent);
		}
	}
	
	private void updatePrice() 
	{
		double balance = _app.getSoap().get_userInfo().get_money();
		balance = balance - _allPrice;
		String str = "余额:" + String.valueOf(balance) + "元.  已选套餐总额:"
				   	 + _allPrice.toString() + "元."; 
		if(balance > 0)
		{
			_text_allPrice.setTextColor(Color.GREEN);
		}
		else
		{
			_text_allPrice.setTextColor(Color.RED);
		}
		_text_allPrice.setText(str);
	}
	
	private void addMealRow(int id,SetMealInfo info)
	{
		TableRow tableRow = new TableRow(this);  
		tableRow.setTag(id);
		tableRow.setPadding(0, 3, 0, 3);
		
		TextView mealName = new TextView(this);  
		mealName.setText(info.get_type());
		mealName.setGravity(Gravity.CENTER);
		mealName.setTextColor(Color.WHITE);
		//mealName.setBackgroundColor(Color.YELLOW);
		TableRow.LayoutParams rowLayout1 = new TableRow.LayoutParams(
				0,TableRow.LayoutParams.WRAP_CONTENT);
		rowLayout1.weight = 0.5f;
		rowLayout1.gravity = Gravity.CENTER_HORIZONTAL;
		mealName.setLayoutParams(rowLayout1);
		
		TextView mealPrice = new TextView(this);  
		mealPrice.setText(String.valueOf(info.get_price()));
		mealPrice.setGravity(Gravity.CENTER);
		mealPrice.setTextColor(Color.WHITE);
		//mealPrice.setBackgroundColor(Color.LTGRAY);
		TableRow.LayoutParams rowLayout2 = new TableRow.LayoutParams(
				0,TableRow.LayoutParams.WRAP_CONTENT);
		rowLayout2.weight = 0.3f;
		rowLayout2.gravity = Gravity.CENTER_HORIZONTAL;
		mealPrice.setLayoutParams(rowLayout2);
		
		Button mealPay = new Button(this);
		mealPay.setText("购买");
		mealPay.setBackgroundResource(R.drawable.button_click);
		mealPay.setTextColor(Color.WHITE);
		TableRow.LayoutParams rowLayout3 = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
		rowLayout3.weight = 0.2f;
		rowLayout3.gravity = Gravity.CENTER_HORIZONTAL;
		mealPrice.setLayoutParams(rowLayout3);
		
		tableRow.addView(mealName);  
		tableRow.addView(mealPrice);
		tableRow.addView(mealPay);
		_table_setmealinfo.addView(tableRow);    
		
		mealPay.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				TableRow tableRow = (TableRow) v.getParent();
				TextView tv = (TextView)tableRow.getChildAt(1);
				_allPrice += Double.valueOf(tv.getText().toString());
				Integer idx = (Integer)tableRow.getTag();
				_payInfoList.add(new SetMealInfo(_mealInfoList.get(idx)));
				addPayRow(_payInfoList.size(),_payInfoList.get(_payInfoList.size() - 1));
				updatePrice();
			}
		});
	}
	
	private void addPayRow(int id,SetMealInfo info)
	{
		TableRow tableRow = new TableRow(this);  
		tableRow.setTag(id);
		tableRow.setPadding(0, 3, 0, 3);
		
		TextView mealName = new TextView(this);  
		mealName.setText(info.get_type());
		mealName.setGravity(Gravity.CENTER);
		mealName.setTextColor(Color.WHITE);
		//mealName.setBackgroundColor(Color.YELLOW);
		TableRow.LayoutParams rowLayout1 = new TableRow.LayoutParams(
				0,TableRow.LayoutParams.WRAP_CONTENT);
		rowLayout1.weight = 0.5f;
		rowLayout1.gravity = Gravity.CENTER_HORIZONTAL;
		mealName.setLayoutParams(rowLayout1);
		
		TextView mealPrice = new TextView(this);  
		mealPrice.setText(String.valueOf(info.get_price()));
		mealPrice.setGravity(Gravity.CENTER);
		mealPrice.setTextColor(Color.WHITE);
		//mealPrice.setBackgroundColor(Color.LTGRAY);
		TableRow.LayoutParams rowLayout2 = new TableRow.LayoutParams(
				0,TableRow.LayoutParams.WRAP_CONTENT);
		rowLayout2.weight = 0.3f;
		rowLayout2.gravity = Gravity.CENTER_HORIZONTAL;
		mealPrice.setLayoutParams(rowLayout2);
		
		Button mealPay = new Button(this);
		mealPay.setText("取消");
		mealPay.setBackgroundResource(R.drawable.button_click);
		mealPay.setTextColor(Color.WHITE);
		TableRow.LayoutParams rowLayout3 = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
		rowLayout3.weight = 0.2f;
		rowLayout3.gravity = Gravity.CENTER_HORIZONTAL;
		mealPrice.setLayoutParams(rowLayout3);
		
		tableRow.addView(mealName);  
		tableRow.addView(mealPrice);
		tableRow.addView(mealPay);
		_table_pay.addView(tableRow);  
		if(_table_pay.getVisibility() != View.VISIBLE)
		{
			_table_pay.setVisibility(View.VISIBLE);
			_table_payhead.setVisibility(View.VISIBLE);
		}
		
		mealPay.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				TableRow tableRow = (TableRow) v.getParent();  
				if(tableRow != null)
				{
					TextView tv = (TextView)tableRow.getChildAt(1);
					_allPrice -= Double.valueOf(tv.getText().toString());
					_table_pay.removeView(tableRow);	
					updatePrice();
				}
			}
		});
	}
	
	private class BuyTask extends AsyncTask<String[], Void, Integer>
	{
		String strTotalPrice;
		
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			strTotalPrice = String.valueOf(_allPrice);
		}
		
		@Override
		protected Integer doInBackground(String[]...params)
		{
			int result = _app.getSoap().buySetmeal_inSoap(params[0], strTotalPrice);

			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result)
		{
			switch(result)
			{
			case -1:
				break;
			case -2:
				Toast.makeText(getApplicationContext(), "内部错误：订单参数出错", 0).show();
				break;
			case -3:
				Toast.makeText(getApplicationContext(), "内部错误：用户ID出错", 0).show();
				break;
			case 0:
				Toast.makeText(getApplicationContext(), "购买成功", 0).show();
				isBuySucceed = true;
				break;
			
			//	Toast.makeText(getApplicationContext(), "数据库操作失败", 0).show();
			//	break;
			case 2:
				Toast.makeText(getApplicationContext(), "内部错误：订单参数有空", 0).show();
				break;
			case 1:
			case 3:
				Toast.makeText(getApplicationContext(), "内部错误：数据库操作失败", 0).show();
				break;
			case 4:
				Toast.makeText(getApplicationContext(), "余额缓存出错， 请重启客户端", 0).show();
				break;
			}
			
		}
	}
	
	
	private class RefrashTask extends AsyncTask<Void,Void,Integer>
	{		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			//Thread.sleep(1000);
		}
		
		@SuppressLint("ShowToast")
		@Override
		protected Integer doInBackground(Void... params) 
		{
			if(isBuySucceed)
			{
				//_app.getSoap().getUserSetMeal();
			}
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result) 
		{
			
		}
	}
	
	
	
	
}
