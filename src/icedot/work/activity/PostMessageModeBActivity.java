
package icedot.work.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alipay.android.MobileSecurePayHelper;


import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangApplication.MyDataCache;
import icedot.work.common.GlobalData;
import icedot.work.shengfang.business.R;
import icedot.work.shengfang.business.R.layout;
import icedot.work.shengfang.business.R.menu;
import icedot.work.struct.MessageInfo;
import icedot.work.struct.ShopInfo;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import icedot.work.adapter.*;

public class PostMessageModeBActivity extends Activity {
	
	
	private ShengFangApplication _app = null;
	private Spinner   _spin_shopName = null;
	
	private EditText  _edit_shopaddress = null;
	private EditText  _edit_msgTitle = null;
	private Button   _btn_startTime = null;
	private DatePickerDialog   _startDateDlg = null;
	//private EditText   _edit_sendCount;
	private EditText   _edit_msgContent = null;
	private EditText CustomerChooseEditText = null;

	private int mYear = 0;
	private int mMonth = 0;
	private int mDay = 0;
	private int _selectShopID = 0;
	private MessageInfo _msgInfo = null;
	private List<ShopInfo> _shopInfoList = null;
	private ArrayAdapter<String> _shopAdapter = null;
	

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pust_message_mode_b);
		
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		mspHelper.detectMobile_sp();
		
		_app = (ShengFangApplication)getApplication();
		if(_app.getSoap().get_shopList().size() == 0)
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"无商铺信息!", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		initView();
		initData();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pust_message_mode_b, menu);
		return true;
	}
	
	
	private void initView()
	{
		_spin_shopName = (Spinner) findViewById(R.id.addmsg_spinner_shopname);	
		//_edit_mealType = (EditText) findViewById(R.id.addmsg_edit_mealtype);
		_edit_shopaddress = (EditText) findViewById(R.id.addmsg_edit_shopaddress);
		_edit_msgTitle = (EditText) findViewById(R.id.addmsg_edit_title);
		//_edit_sendCount = (EditText) findViewById(R.id.addmsg_edit_msgcount);
		_edit_msgContent = (EditText) findViewById(R.id.addmsg_edit_content);
		_btn_startTime = (Button) findViewById(R.id.addmsg_btn_startdate);
		
		
		
		CustomerChooseEditText = (EditText) findViewById(R.id.customer_choosing_editbox);
		
		CustomerChooseEditText.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Log.e("customer_choose_editbox_clicked", "balalabalala......");
				Intent intent = new Intent(PostMessageModeBActivity.this,CustomerChoosingActivity.class);
				startActivity(intent);
				
			}
			
		});
		
		_spin_shopName.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			 @Override 
			 public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  long arg3) 
			 { 
				 _edit_shopaddress.setText(_shopInfoList.get(arg2).get_address());
				 //updateMealInfo();
			 } 
			 
			 @Override 
			 public void onNothingSelected(AdapterView<?> arg0)
			 { 
				 _edit_shopaddress.setText(""); 
			 }       

		});
		
		Calendar cal = Calendar.getInstance();
		mYear = cal.get(Calendar.YEAR);
		mMonth = cal.get(Calendar.MONTH) + 1;
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		
		_btn_startTime.setText(getNowDate());
		
		//initCustomerList();
		
	}
	
	
	private void initData() 
	{
		Log.d("onCreate","initData run");
		_msgInfo = _app.getSoap().get_addMsg();
		List<ShopInfo> allShop = _app.getSoap().get_shopList();
		_shopInfoList = new ArrayList<ShopInfo>();
		
		List<String> shopNameList = new ArrayList<String>();		
		
		for(int i = 0; i < allShop.size();i++)
		{
			if(allShop.get(i).get_check() == 1)
			{
				shopNameList.add(allShop.get(i).get_name());
				_shopInfoList.add(allShop.get(i));
				if(_msgInfo.get_shopID().length() > 0)
				{
					if(_shopInfoList.get(i).get_ID() == Integer.valueOf(_msgInfo.get_shopID()))
					{
						_selectShopID = shopNameList.size() - 1;						
					}
				}
			}
		}
		if( shopNameList.size() == 0)
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"无商铺通过审核!", Toast.LENGTH_LONG).show();
			finish();
			return ;
		}
		_shopAdapter =  new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, shopNameList);  
		_shopAdapter .setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
		_spin_shopName.setAdapter(_shopAdapter);	
		
		_spin_shopName.setSelection(_selectShopID);
		_edit_msgTitle.setText(_msgInfo.get_title());
		_edit_msgContent.setText(_msgInfo.get_content());
		if(_msgInfo.get_startTime().length() != 0)
		{
			_btn_startTime.setText(_msgInfo.get_startTime().subSequence(0, 10));
		}
		
		//updateMealInfo();

	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		ShengFangApplication context = (ShengFangApplication) getApplicationContext();
		MyDataCache dc = context.getDataCache();
		String s = dc.getData();
		CustomerChooseEditText.setText(s);
		
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		//putMsgInfo();
		_app.getSoap().set_addMsg(_msgInfo);
		Log.d("onPause","onPause run");
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

	public void btn_startdate_onClick(View v)
	{
		if(_startDateDlg == null)
		{
			_startDateDlg = new DatePickerDialog(
					PostMessageModeBActivity.this,_dateSetListener,
					mYear,mMonth - 1,mDay);
			_startDateDlg.show();
		}
		else
		{
			_startDateDlg.show();
		}
	}
	private DatePickerDialog.OnDateSetListener _dateSetListener =
			new DatePickerDialog.OnDateSetListener() 
	{
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{		
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;
			  
			_btn_startTime.setText(getNowDate());  
			
			//updateMealInfo();
		}
	};
	
	public void btn_back_onClick(View v)
	{
		finish();
	}
	
	public void btn_clear_onClick(View v)
	{
		Log.e("btn_clear_clear_clear_clicked", "PostMessageModeBActivity");
	}
	
	public void btn_submit_onClick(View v)
	{
		Log.e("btn_submit_clicked", "!@#$%^&*()_");
	}
	


	private String getNowDate()
	{		
		String theDate = String.valueOf(mYear);
		if(mMonth <= 9)
		{
			theDate += "-0" + mMonth;
		}
		else
		{
			theDate += "-" + mMonth;
		}
		
		if(mDay <= 9)
		{
			theDate += "-0" + mDay;
		}
		else
		{
			theDate += "-" + mDay;
		}
		return theDate;
	}

	

}
