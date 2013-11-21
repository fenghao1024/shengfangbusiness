package icedot.work.activity;

import icedot.work.application.ShengFangApplication;
import icedot.work.shengfang.business.R;
import icedot.work.struct.PayRecordInfo;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alipay.android.MobileSecurePayHelper;

public class PayRecordActivity extends Activity
{
	private ShengFangApplication _app = null;
	private TableLayout _table_recordinfo;		//套餐信息表
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payrecord);
		
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
	
	private void initData() 
	{
		PayRecordInfo info = new PayRecordInfo();
		info.set_date("2012-10-10");
		info.set_mealName("套餐类型");
		info.set_price("300");
		addPayRow(0,info);
		
	}

	private void initView() 
	{
		_table_recordinfo = (TableLayout) findViewById(R.id.payrecord_table_record);
		
	}
	
	private void addPayRow(int id,PayRecordInfo info)
	{
		TableRow tableRow = new TableRow(this);  
		tableRow.setTag(id);
		tableRow.setPadding(0, 3, 0, 3);
		
		TextView mealName = new TextView(this);  
		mealName.setText(info.get_mealName());
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
		rowLayout2.weight = 0.2f;
		rowLayout2.gravity = Gravity.CENTER_HORIZONTAL;
		mealPrice.setLayoutParams(rowLayout2);
		
		TextView mealDate = new TextView(this);  
		mealDate.setText("2012-10-10");
		mealDate.setTextColor(Color.WHITE);
		TableRow.LayoutParams rowLayout3 = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
		rowLayout3.weight = 0.2f;
		rowLayout3.gravity = Gravity.CENTER_HORIZONTAL;
		mealPrice.setLayoutParams(rowLayout3);
		
		tableRow.addView(mealName);  
		tableRow.addView(mealPrice);
		tableRow.addView(mealDate);
		_table_recordinfo.addView(tableRow);    
	}
}
