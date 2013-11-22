package icedot.work.activity;

import icedot.work.adapter.ExListView_Main_Adapter;
import icedot.work.application.ShengFangAppConfig;
import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangSoap;
import icedot.work.common.GlobalData;
import icedot.work.shengfang.business.R;
import icedot.work.struct.View_Selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private ShengFangApplication _app = null;
	private AlertDialog _backDlg = null;			//退出对话框
	private ImageView _image_headPic = null;
	private ExpandableListView _listView_main = null;
	private ExListView_Main_Adapter _listView_Adapter = null;
	private List<Map<String, Object>> _listView_group = null;
	private List<List<Map<String, Object>>> _listView_child = null;
	
	private MainTask _mainTask = null;
	private Dialog _taskDlg = null;
	private int _activityID = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_app = (ShengFangApplication)getApplication();
		
		initView();
		
		_mainTask = new MainTask();
		_mainTask.execute();
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
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		Log.d("MainActivity", "onDestroy run");
		_app.getSoap().clearData();
		
		//int pid = android.os.Process.myPid();
		//android.os.Process.killProcess(pid);
		
	}
	
	private void initView() 
	{
		_image_headPic = (ImageView) findViewById(R.id.main_top_avatar);
		//_image_headPic.setImageResource(R.drawable.qst_logo);
		_listView_main = (ExpandableListView) findViewById(R.id.main_list);
		
		_listView_group = new ArrayList<Map<String, Object>>();
		_listView_child = new ArrayList<List<Map<String, Object>>>();
		getListViewData();
		_listView_Adapter = new ExListView_Main_Adapter(this, _listView_group, _listView_child);
		_listView_main.setAdapter(_listView_Adapter);
		//展开所有的项
		for(int i = 0; i < _listView_group.size();i++)
		{
			_listView_main.expandGroup(i);
		}
		
		initViewListener();
	}

	private void initViewListener() 
	{
		_listView_main.setOnGroupClickListener(new OnGroupClickListener() 
		{			
			//避免组折叠
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) 
			{
				return true;
			}
		});
		
		_listView_main.setOnChildClickListener(new OnChildClickListener() 
		{			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
//				//这里是如何设置子选项被选中
//				Log.d("onChildClick", "groupPosition=" + groupPosition + 
//						" childPosition=" + childPosition + " id=" + id);
//				_listView_Adapter.updateClicked(groupPosition,childPosition);
//				_listView_Adapter.notifyDataSetChanged();
				int ret = (groupPosition + 1) * 100 + childPosition  + 1;
				if(_mainTask.getStatus() == Status.RUNNING)
				{
					if(_taskDlg == null)
					{
						_taskDlg = ProgressDialog.show(MainActivity.this, "", "更新数据中. 请稍候...", true);
					}
					else
					{
						_taskDlg.show();
					}
					_activityID = ret;
				}
				else
				{
					movetoActivity(ret);
				}
				
				return true;
			}
		});
	}
	
	private void movetoActivity(int id)
	{
		switch(id)
		{
		case View_Selector.AddMsg:
		{
			Intent intent = new Intent(MainActivity.this,ChosePostModeActivity.class);
			startActivity(intent);
		}
			break;
		case View_Selector.ShopPage:
		{
			Intent intent = new Intent(MainActivity.this,ShopInfoListActivity.class);
			startActivity(intent);
		}
			break;				
		case View_Selector.Vidio:
		{
			Intent intent = new Intent(MainActivity.this,VidioMgrActivity.class);
			startActivity(intent);
		}
			break;
		case View_Selector.ShowMsg:
		{
			Intent intent = new Intent(MainActivity.this,ShowMsgListActivity.class);
			startActivity(intent);
		}
			break;
		case View_Selector.Personal:
		{
			Intent intent = new Intent(MainActivity.this,PersonalActivity.class);
			startActivity(intent);
		}
			break;
		case View_Selector.PayMgr:
		{
			Intent intent = new Intent(MainActivity.this,SetMealActivity.class);
			startActivity(intent);
		}
			break;
		case View_Selector.LeftSetmeal:
		{
			Intent intent = new Intent(MainActivity.this, UsersSetmealActivity.class);
			startActivity(intent);
		}
			break;
		case View_Selector.PayHistory:
		{
			Intent intent = new Intent(MainActivity.this,PayRecordActivity.class);
			startActivity(intent);
		}
			break;
		case View_Selector.App_Share:
		{
			final AlertDialog dlg = new AlertDialog.Builder
					(MainActivity.this).create();
			dlg.show();
			Window win = dlg.getWindow();
			win.setContentView(R.layout.main_share_dlg);
			
			ImageView image = (ImageView) win.findViewById(R.id.share_dlg_image);
			Bitmap map = GlobalData.getDimemsionCode(_app.getSoap().get_downUrl(),250,250);
			image.setImageBitmap(map);
			
			image.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					dlg.dismiss();
				}
			});
			
			TextView appVersionText = (TextView) win.findViewById(R.id.app_version_name);
			appVersionText.setText("版本：        " + _app.getAppVersion());
			
		}
			break;
		case View_Selector.AppExit:
			if (_backDlg == null )
			{
				_backDlg = GlobalData.exitNoticeDialog(MainActivity.this);
			}
			else
			{
				if(!_backDlg.isShowing())
					_backDlg.show();
			}				
			break;
		default:
			break;
		}
	}

	private void getListViewData() 
	{
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("name", "常用");
		_listView_group.add(map1);		
		
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("name", "钱包");
		_listView_group.add(map2);
		
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("name", "系统");
		_listView_group.add(map3);
		
		List<Map<String, Object>> top_list1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> c_map1 = new HashMap<String, Object>();
		c_map1.put("icon", R.drawable.icon_msg);
		c_map1.put("name", "发布信息");
		c_map1.put("click", false);
		top_list1.add(c_map1);
		
		Map<String, Object> c_map2 = new HashMap<String, Object>();
		c_map2.put("icon", R.drawable.icon_shop);
		c_map2.put("name", "商铺信息");
		c_map2.put("click", false);
		top_list1.add(c_map2);		
		
		Map<String, Object> c_map4 = new HashMap<String, Object>();
		c_map4.put("icon", R.drawable.icon_vidio);
		c_map4.put("name", "实时视频");
		c_map4.put("click", false);
		top_list1.add(c_map4);
		
		Map<String, Object> c_map5 = new HashMap<String, Object>();
		c_map5.put("icon", R.drawable.icon_msg);
		c_map5.put("name", "历史信息");
		c_map5.put("click", false);
		top_list1.add(c_map5);
		
		_listView_child.add(top_list1);		
				
		List<Map<String, Object>> top_list2 = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> c_map3 = new HashMap<String, Object>();
		c_map3.put("icon", R.drawable.icon_person);
		c_map3.put("name", "个人信息");
		c_map3.put("click", false);
		top_list2.add(c_map3);
		
		Map<String, Object> t_map1 = new HashMap<String, Object>();
		t_map1.put("icon", R.drawable.icon_pay);
		t_map1.put("name", "套餐购买");
		t_map1.put("click", false);
		top_list2.add(t_map1);
		
		Map<String, Object> t_map2 = new HashMap<String, Object>();
		t_map2.put("icon", R.drawable.icon_box);
		t_map2.put("name", "剩余套餐");
		t_map2.put("click", false);
		top_list2.add(t_map2);

		Map<String, Object> t_map3 = new HashMap<String, Object>();
		t_map3.put("icon", R.drawable.icon_record);
		t_map3.put("name", "购买记录");
		t_map3.put("click", false);
		top_list2.add(t_map3);
		
		_listView_child.add(top_list2);
		
		List<Map<String, Object>> top_list3 = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> s_map1 = new HashMap<String, Object>();
		s_map1.put("icon", R.drawable.icon_msg);
		s_map1.put("name", "分享");
		s_map1.put("click", false);
		top_list3.add(s_map1);
		
		Map<String, Object> s_map2 = new HashMap<String, Object>();
		s_map2.put("icon", R.drawable.icon_exit);
		s_map2.put("name", "退出");
		s_map2.put("click", false);
		top_list3.add(s_map2);
		
		_listView_child.add(top_list3);
	}
	
	public void btn_update_onClick(View v)
	{
		

		if(_taskDlg == null)
		{
			_taskDlg = ProgressDialog.show(MainActivity.this, "", "更新数据中. 请稍候...", true);
		}
		else
		{
			_taskDlg.show();
		}
		_mainTask = new MainTask();
		_mainTask.execute();		
	}
	
	
	private class MainTask extends AsyncTask<Void,Void,Integer>
	{		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Void... params) 
		{
			ShengFangAppConfig _appConfig;
			_appConfig = _app.getAppConfig();
			try
			{
				ShengFangSoap soap = _app.getSoap();
				int ret = soap.shopListSoap();
				int repeat = 2;
				while(repeat > 0 && (ret == -1 || ret == -2))
				{
					Thread.sleep(1000);
					ret = soap.shopListSoap();
					repeat--;
				}
				
				ret = soap.setmealListSoap();
				repeat = 2;
				while(repeat > 0 && (ret == -1 || ret == -2))
				{
					Thread.sleep(1000);
					ret = soap.setmealListSoap();
					repeat--;
				}
				
				ret = soap.getHolidayList();
				repeat = 2;
				while(repeat > 0 && (ret == -1 || ret == -2))
				{
					Thread.sleep(1000);
					ret = soap.getHolidayList();
					repeat--;
				}
				
				ret = soap.updateUserInfo_InSoap();
				repeat = 2;
				while(repeat > 0 && (ret == -1 || ret == -2))
				{
					Thread.sleep(1000);
					ret = soap.updateUserInfo_InSoap();
					repeat--;
				}
				
				ret = soap.getUserSetMeal();
				repeat = 2;
				while(repeat > 0 && (ret == -1 || ret == -2))
				{
					Thread.sleep(1000);
					ret = soap.getUserSetMeal();
					repeat--;
				}
				return ret;
			}
			catch (Exception e)
			{
				Log.d("MainTask", e.getMessage());
			}
			
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result) 
		{
			if(_taskDlg != null && _taskDlg.isShowing())
			{
				_taskDlg.dismiss();
				movetoActivity(_activityID);
			}
		}
	}
}
