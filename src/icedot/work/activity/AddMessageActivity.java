package icedot.work.activity;

import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangSoap;
import icedot.work.common.GlobalData;
import icedot.work.shengfang.business.R;
import icedot.work.struct.Meal_Type;
import icedot.work.struct.MessageInfo;
import icedot.work.struct.SetMealInfo;
import icedot.work.struct.ShopInfo;
import icedot.work.struct.UserInfo;
import icedot.work.struct.View_Selector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.alipay.android.MobileSecurePayHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.Toast;

public class AddMessageActivity extends Activity
{
	private ShengFangApplication _app = null;
	
	private Spinner   _spin_shopName;
	private EditText  _edit_mealType;
	private int setmealID = -1;
	private EditText  _edit_shopaddress;
	private EditText  _edit_msgTitle;
	private Button   _btn_startTime;
	private DatePickerDialog   _startDateDlg;
	//private EditText   _edit_sendCount;
	private EditText   _edit_msgContent;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int _selectShopID = 0;
	private MessageInfo _msgInfo;
	private List<ShopInfo> _shopInfoList;
	private ArrayAdapter<String> _shopAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmessage);
		//this.tabHost = getTabHost();
		//LayoutInflater.from(this).inflate(R.layout.activity_main, tabHost.getTabContentView(), true);
		//tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("xxx").setContent(R.id.scrollViewTasb));
		//tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("xx23234x").setContent(R.id.tab2));

		
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		mspHelper.detectMobile_sp();
		
		_app = (ShengFangApplication)getApplication();
		if(_app.getSoap().get_shopList().size() == 0)
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"还未添加商铺!", Toast.LENGTH_LONG).show();
			finish();
			return ;
		}

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
	protected void onPause()
	{
		super.onPause();
//		putMsgInfo();
//		_app.getSoap().set_addMsg(_msgInfo);
//		Log.d("onPause","onPause run");
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		//initData();
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
	
	public void btn_startdate_onClick(View v)
	{
		if(_startDateDlg == null)
		{
			_startDateDlg = new DatePickerDialog(
					AddMessageActivity.this,_dateSetListener,
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
			
			updateMealInfo();
		}
	};
	
	private void initView()
	{
		_spin_shopName = (Spinner) findViewById(R.id.addmsg_spinner_shopname);	
		_edit_mealType = (EditText) findViewById(R.id.addmsg_edit_mealtype);
		_edit_shopaddress = (EditText) findViewById(R.id.addmsg_edit_shopaddress);
		_edit_msgTitle = (EditText) findViewById(R.id.addmsg_edit_title);
		//_edit_sendCount = (EditText) findViewById(R.id.addmsg_edit_msgcount);
		_edit_msgContent = (EditText) findViewById(R.id.addmsg_edit_content);
		_btn_startTime = (Button) findViewById(R.id.addmsg_btn_startdate);
		
		_spin_shopName.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			 @Override 
			 public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  long arg3) 
			 { 
				 _edit_shopaddress.setText(_shopInfoList.get(arg2).get_address());
				 _selectShopID = arg2;
				 updateMealInfo();
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
	}
	private void putMsgInfo()
	{
		//String shopName = _spin_shopName.getSelectedItem().toString();
		int itemNum = _spin_shopName.getSelectedItemPosition();

		_msgInfo.set_shopID(String.valueOf(_shopInfoList.get(itemNum).get_ID()));


		if(_btn_startTime.getText().length() > 0)
		{
			_msgInfo.set_startTime(_btn_startTime.getText().toString() + " 09:00:00");
			_msgInfo.set_endTime(_btn_startTime.getText().toString() + " 21:00:00");
		}		
		_msgInfo.set_title(_edit_msgTitle.getText().toString());
		_msgInfo.set_content(_edit_msgContent.getText().toString());
		_msgInfo.set_mealType(_edit_mealType.getText().toString());
		
		//int mid = _spin_mealType.getSelectedItemPosition();
		//_msgInfo.set_price(_mealInfoList.get(mid).get_price());
		//_msgInfo.set_count(_edit_sendCount.getText().toString());
	}
	private void updateMealInfo()
	{
		SetMealInfo mealinfo = _app.getSoap().get_setmealInfo(
				mYear, mMonth, mDay,_shopInfoList.get(_selectShopID).get_areaType());
		if(mealinfo != null)
		{
			_edit_mealType.setText(mealinfo.get_type());
			setmealID = mealinfo.get_ID();
		}
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
				String arrayType = Meal_Type.ShoppingDistrict[allShop.get(i).get_areaType()];
				shopNameList.add(allShop.get(i).get_name() + "(" + arrayType + ")");
				_shopInfoList.add(allShop.get(i));
			}
		}
		_selectShopID = 0;
		if( shopNameList.size() == 0)
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"无通过审核的商铺!", Toast.LENGTH_LONG).show();
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
		
		updateMealInfo();
	}
	
	public void btn_clear_onClick(View v)
	{
		_edit_msgTitle.setText("");
		_edit_msgContent.setText("");
		_app.getSoap().get_addMsg().clear();
	}
	
	public void btn_submit_onClick(View v)
	{
		if(!checkData())
		{
			return;
		}
		
		putMsgInfo();
		
		Builder builder = new Builder(this);
		//对话框事件处理
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() 
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				MsgTask task = new MsgTask();
				task.execute();						
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});
		//检查用户的套餐
		List<SetMealInfo> userMeal = _app.getSoap().get_userMealInfoList();
		if(userMeal.size() <= 0)
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"您还没有购买任何套餐，请先购买", 1).show();
			Intent intent = new Intent(AddMessageActivity.this, SetMealActivity.class);
			startActivity(intent);
			//finish();
			return;
		}
		boolean flag = false;
		for(int i = 0; i < userMeal.size();i++)
		{
			if(userMeal.get(i).get_ID() == setmealID && setmealID != -1)
			{
				if(userMeal.get(i).get_count() <= 0)
				{
					break;
				}
				flag = true;
				//弹出对话框做一次再确认			
				builder.setTitle("发送信息确认");
				builder.setMessage("商铺:" + _msgInfo.get_shopName() + 
						"\r\n套餐类型:" + _msgInfo.get_mealType() +
						"\r\n发布日期:" + _msgInfo.get_startTime() +
						"\r\n标题:" + _msgInfo.get_title() +
						"\r\n内容:" + _msgInfo.get_content());				
				
				AlertDialog ret = builder.create();
				ret.show();	
				return;
			}		
		}
		if(!flag)
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"所选套餐可用次数不足", 0).show();
			Intent intent = new Intent(AddMessageActivity.this, SetMealActivity.class);
			startActivity(intent);
			//finish();
			return;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
//		if(resultCode == 1)
//		{
//			MsgTask task = new MsgTask();
//			task.execute();					
//		}
//		else
//		{
//			GlobalData.getCustomToast(this, 
//					this.getResources().getDrawable(R.drawable.custom_toast_icon),
//					"信息添加失败！", Toast.LENGTH_LONG).show();
//		}
	}
	
	private boolean checkData() 
	{
		if(_edit_msgTitle.getText().length() == 0)
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"信息的标题不能为空!", Toast.LENGTH_LONG).show();
			return false;
		}
		if(_edit_msgContent.getText().length() == 0)
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"信息的内容不能为空!", Toast.LENGTH_LONG).show();
			return false;
		}
//		if(_edit_sendCount.getText().length() == 0)
//		{
//			GlobalData.getCustomToast(this, 
//					this.getResources().getDrawable(R.drawable.custom_toast_icon),
//					"发送的条数不能为空!", Toast.LENGTH_LONG).show();
//			return false;
//		}
		return true;
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
//	// 隐藏手机键盘
//	 private void hideIM(View edt)
//	 {  
//		 try
//		 {
//			 InputMethodManager im = (InputMethodManager) 
//					 getSystemService(Activity.INPUT_METHOD_SERVICE); 
//			 IBinder  windowToken = edt.getWindowToken();  
//			 if(windowToken != null) 
//			 {  
//				 im.hideSoftInputFromWindow(windowToken, 0);  
//			 }
//		 }
//		 catch (Exception e){
//			 
//		 }
//	 }
	 
	 private class MsgTask extends AsyncTask<Void,Void,Integer>
	 {
		 @Override
		 protected void onPreExecute() 
		 {

		 }
		 @Override
		 protected Integer doInBackground(Void... arg0) 
		 {
				ShengFangSoap soap = _app.getSoap();
				int ret = soap.addMsgInfoSoap(_msgInfo, setmealID);
				if(ret == 2)
				{

				}
				if(ret != 2)
				{
					soap.get_addMsg().clear();
				}
				return ret;
		 }
		 
		 @Override
		 protected void onPostExecute(Integer result) 
		 {
			 switch(result)
				{
				case -1:
					GlobalData.getCustomToast(AddMessageActivity.this,
							AddMessageActivity.this.getResources().
							getDrawable(R.drawable.custom_toast_icon), 
							"内部错误,请与管理员联系!", Toast.LENGTH_LONG).show();
					break;
				case -2:
					GlobalData.getCustomToast(AddMessageActivity.this,
							AddMessageActivity.this.getResources().
							getDrawable(R.drawable.custom_toast_icon), 
							"无法连接服务器!", Toast.LENGTH_LONG).show();
					break;
				case 0:
					GlobalData.getCustomToast(AddMessageActivity.this,
							AddMessageActivity.this.getResources().
							getDrawable(R.drawable.custom_toast_icon), 
							"商铺信息重复，添加失败！", Toast.LENGTH_LONG).show();
					break;
				case 1:
					GlobalData.getCustomToast(AddMessageActivity.this,
							AddMessageActivity.this.getResources().
							getDrawable(R.drawable.custom_toast_icon), 
							"添加信息不完整，添加失败！", Toast.LENGTH_LONG).show();
					break;
				case 2:
					GlobalData.getCustomToast(AddMessageActivity.this,
							AddMessageActivity.this.getResources().
							getDrawable(R.drawable.smile), 
							"信息添加成功", Toast.LENGTH_LONG).show();
					AddMessageActivity.this.finish();
					break;
				case 3:
					GlobalData.getCustomToast(AddMessageActivity.this,
							AddMessageActivity.this.getResources().
							getDrawable(R.drawable.custom_toast_icon), 
							"信息添加失败！", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
				super.onPostExecute(result);
		 }
	 }
}
