package icedot.work.activity;

import icedot.work.adapter.ExListView_Text_Adapter;
import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangSoap;
import icedot.work.common.GlobalData;
import icedot.work.shengfang.business.R;
import icedot.work.struct.Meal_Type;
import icedot.work.struct.ShopInfo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class ShopInfoListActivity extends Activity
{
	private ShengFangApplication _app;
	private List<ShopInfo> 	_shopInfoList;
	
	private ExpandableListView _expandableList;
	private ExListView_Text_Adapter _adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopinfolist);
		
		_app = (ShengFangApplication)getApplication();
		ShengFangSoap soap = _app.getSoap();
		_shopInfoList = soap.get_shopList();	
		
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
	
	private void initView() 
	{
		_expandableList = (ExpandableListView) findViewById(R.id.shop_info_list);
	}
	
	private void initData()
	{
		List<String> shopNameList = new ArrayList<String>();
		List<List<String>> infoList = new ArrayList<List<String>>();
		
		if(_shopInfoList.size() == 0)
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"暂无商铺信息,请先添加!", Toast.LENGTH_LONG).show();
		}
		
		for(int i = 0; i < _shopInfoList.size();i++)
		{
			shopNameList.add(_shopInfoList.get(i).get_name());
			List<String> info = new ArrayList<String>();
			info.add("审核状态:" + _shopInfoList.get(i).getCheckString());
			info.add("地址:" + _shopInfoList.get(i).get_address());
			info.add("电话:" + _shopInfoList.get(i).get_phone());
			info.add("描述:" + _shopInfoList.get(i).get_description());
			info.add("所在商圈：" + Meal_Type.ShoppingDistrict[_shopInfoList.get(i).get_areaType()]);
			infoList.add(info);
		}
		
		_adapter = new ExListView_Text_Adapter(this,shopNameList,infoList);
		_expandableList.setAdapter(_adapter);
		
		//展开所有的项
		for(int i = 0; i < _shopInfoList.size();i++)
		{
			_expandableList.expandGroup(i);
		}
	}
}
