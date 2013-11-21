package icedot.work.activity;

import icedot.work.adapter.ExListView_Text_Adapter;
import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangSoap;
import icedot.work.common.GlobalData;
import icedot.work.shengfang.business.R;
import icedot.work.struct.GlobleDefault;
import icedot.work.struct.MessageInfo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ShowMsgListActivity extends Activity
{
	private ShengFangApplication _app;
	private List<MessageInfo> 	_msgList;
	private ExpandableListView _expandableList;
	private ExListView_Text_Adapter _adapter;
	private RelativeLayout _layout;
	
	//private int _pageID = 0;
	private MsgListTask _task = null;
	List<String> _titleList;
	List<List<String>> _infoList;
	private int _lvItem_footer = 0;		//显示的最后一项
	private int _lvItem_total = 0;		//显示的总项数
	
	private int orentation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showmsglist);
		
		_app = (ShengFangApplication)getApplication();
		initView();		
		
		///initData();
		
		_task = new MsgListTask();
		_task.execute(GlobleDefault.FRONT_ORNT);

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
	
	public void btn_updatemsglist_onclick(View v)
	{
//		if(_task.getStatus() == Status.RUNNING)
//			return;

//		_task = new MsgListTask();
//		_task.execute();		
		
		
	}
	
	private void initView() 
	{
		_layout = (RelativeLayout) findViewById(R.id.showmsglist_list_update);
		_layout.setVisibility(View.INVISIBLE);
		_expandableList = (ExpandableListView) findViewById(R.id.showmsglist_list);
		_titleList = new ArrayList<String>();
		_infoList = new ArrayList<List<String>>();
		_adapter = new ExListView_Text_Adapter(this,_titleList,_infoList);
		_expandableList.setAdapter(_adapter);
		
		_expandableList.setOnScrollListener(new OnScrollListener() 
		{			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
//				ShengFangSoap soap = _app.getSoap();
//				//int ret = soap.getMsgCount_inSoap();
//				//Log.d("OnScroll_getMsgCount_inSoap return value : ",  String.valueOf(ret));
//				int count = soap.get_msgTotal();
//				Log.d("UpdateItem",String.valueOf(scrollState));
				
		        int first = view.getFirstVisiblePosition();
//		        int visibleItemCount = view.getChildCount();
//		        int groupCount = _adapter.getGroupCount();
		        
		        boolean showHead = false;
		        boolean showComplete = false;
		        boolean showRear = false;
		        
		        boolean show = false;
		        
		        int orentation = GlobleDefault.FRONT_ORNT;
//		        Log.d("view.getFirstVisiblePosition()   : ",  String.valueOf(first));
//		        Log.d("view.getChildCount()   : ",  String.valueOf(visibleItemCount));
//		        Log.d("_adapter.getGroupCount()   : ",  String.valueOf(groupCount));
				if(scrollState == SCROLL_STATE_IDLE && first == 0)
				{
					showHead = true;
					show = true;
					orentation = GlobleDefault.FRONT_ORNT;
				}
				
				
				if(scrollState == SCROLL_STATE_IDLE && _lvItem_footer == _lvItem_total)
				{
					showRear = true;
					show = true;
					orentation = GlobleDefault.BACK_ORNT;
//					if(_titleList.size() < count)
//					{
//						_layout.setVisibility(View.VISIBLE);
//					}
				}
				
				if(showHead && showRear)
				{
					showComplete = true;
					orentation = GlobleDefault.BACK_ORNT;
				}
				
				if(show)
				{
					_task = new MsgListTask();
					_task.execute(orentation);
					return ;
				}
				
//		        if(groupCount < count)
//		        {
//		    		_task = new MsgListTask();
//		    		_task.execute();
//		    		return ;
//		        }
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) 
			{
				Log.d("firstVisibleItem   : ",  String.valueOf(firstVisibleItem));
				Log.d("visibleItemCount   : ",  String.valueOf(visibleItemCount));
				Log.d("totalItemCount   : ",  String.valueOf(totalItemCount));
				
				_lvItem_footer = firstVisibleItem + visibleItemCount;
				_lvItem_total = totalItemCount;
			}
		});
	}
		
	private void initData(int orentation)
	{
		_msgList = _app.getSoap().get_msgList();
		
		if(_msgList.size() == 0)
		{
			GlobalData.getCustomToast(this, 
					this.getResources().getDrawable(R.drawable.custom_toast_icon),
					"无历史信息!", Toast.LENGTH_LONG).show();
			
			return;
		}
		int newMsgCount = _msgList.size() - _titleList.size();
		int start = 0;
		int end = 0;
		switch(orentation)
		{
		case GlobleDefault.FRONT_ORNT:
			start = 0;
			end = newMsgCount;
			break;
		case GlobleDefault.BACK_ORNT:
			start = _titleList.size();
			end = _msgList.size();
			break;
			default:
				return;
		}
		List<String> tempTitleList = new ArrayList<String>();
		List<List<String>> tempInfoList = new ArrayList<List<String>>();
		
		for(int i = start; i < end;i++)
		{
			tempTitleList.add(_msgList.get(i).get_title());
			List<String> info = new ArrayList<String>();
			info.add("商铺名:" + _msgList.get(i).get_shopName());
			info.add("发送状态:" + _msgList.get(i).getSendStateString());
			info.add("开始时间:" + _msgList.get(i).get_startTime());
			info.add("结束时间:" + _msgList.get(i).get_endTime());
			info.add("发送条数:" + _msgList.get(i).get_count());
			info.add("内容:" + _msgList.get(i).get_content());
			tempInfoList.add(info);
		}
		
		switch(orentation)
		{
		case GlobleDefault.FRONT_ORNT:
			_titleList.addAll(0, tempTitleList);
			_infoList.addAll(0, tempInfoList);
			break;
		case GlobleDefault.BACK_ORNT:
			_titleList.addAll(tempTitleList);
			_infoList.addAll(tempInfoList);
			break;
			default:
				return; 
		}
		
		_adapter.notifyDataSetChanged();
	}
	
	boolean downLoadComplete = false;
	
	boolean showRearComplete = false;
	boolean showHeadComplete = false;
	
	@Override 
	protected Dialog onCreateDialog(int id)
	{
		String[] msg = {"下载数据中. 请稍候...", "已经显示出完整数据", "网络出错", "惊天BUG，请联系我"};
		return ProgressDialog.show(this, "", msg[id], true);
	}
	
	private class MsgListTask extends AsyncTask<Integer, Void, Integer>
	{
		private boolean showDlg = false;
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			showDlg = true;
			if(downLoadComplete)
			{
				//Toast t = new Toast(_app);
				//Toast.makeText(getApplicationContext(), "已经显示出完整的记录", 0).show();
			}
			else
			{
				if(showHeadComplete)
				{
					//Toast.makeText(getApplicationContext(), "已经显示出最新的记录", 0).show();
				}
				if(showRearComplete)
				{
					
				}
					
			}
			if(!downLoadComplete)
				showDialog(0);
			
			showHeadComplete = false;
			showRearComplete = false;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) 
		{
			ShengFangSoap soap = _app.getSoap();
			orentation = params[0];
			return soap.msgInfoListSoap(orentation);	
		}
		
		@Override
		protected void onPostExecute(Integer result) 
		{
			if(showDlg && !downLoadComplete)
			{	
				dismissDialog(0);
				showDlg = false;
			}
			
			if(result == 10)
			{
				downLoadComplete = true;
				Toast.makeText(getApplicationContext(), "已经显示出完整的记录", 0).show();
			}
			if(result == 11)
			{
				showHeadComplete = false;
				showRearComplete = true;
				Toast.makeText(getApplicationContext(), "已经显示出最早的记录", 0).show();
			}
			if(result == 12)
			{
				showHeadComplete = true;
				showRearComplete = false;
				Toast.makeText(getApplicationContext(), "已经显示出最新的记录", 0).show();
			}

			initData(orentation);

			//_layout.setVisibility(View.GONE);
		}
	}
}
