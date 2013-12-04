package icedot.work.activity;

import java.util.ArrayList;
import java.util.List;

//import icedot.work.activity.CustomerChoosingActivity.ViewHolder;
import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangSoap;
import icedot.work.shengfang.business.R;
import icedot.work.struct.Meal_Type;
import icedot.work.struct.SetMealInfo;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.os.AsyncTask.Status;
import android.app.Activity;
import android.app.Dialog;
//import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
//import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
//import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;

public class UsersSetmealActivity extends Activity {
	
	ExpandableListView exListView = null;
	
	private List<String> setmealNameList = null;
	private List<String> setmealCountList = null;
	private List<List<String>> setmealContentList = null;
	
	private UpdateSetmealTask task = null;
	private Dialog dialog = null;
	
	private ShengFangApplication app = null;
	private ShengFangSoap soap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users_setmeal);
		
		this.initView();
		this.initData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.users_setmeal, menu);
		return true;
	}
	
	public void btn_back_onClick(View view)
	{
		finish();
	}
	
	public void initView()
	{
		exListView = (ExpandableListView)findViewById(R.id.show_users_setmeal_list);
		
		
	}
	
	public void initData()
	{

		initExList();
	}
	
	public void initExList()
	{
		setmealNameList = new ArrayList<String>();
		setmealCountList = new ArrayList<String>();
		setmealContentList = new ArrayList<List<String>>();
		
		app = (ShengFangApplication)this.getApplication();
		soap = app.getSoap();
		

		

		List<SetMealInfo> setmealInfoList = soap.get_mealInfoList();
		List<SetMealInfo> userSetmealList = soap.get_userMealInfoList();
		
		setmealNameList.add("套餐名称");
		setmealCountList.add("剩余量");
		
		for(int i = 0; i < setmealInfoList.size(); i++)
		{
			
			if(setmealInfoList.get(i).get_ID() == Meal_Type.GROUP_SMS)
			{
				setmealNameList.add("短信群发");
			}
			else
			{
				setmealNameList.add(setmealInfoList.get(i).get_type());
			}
			
			
			int count = 0;
			for(int  j = 0; j < userSetmealList.size(); j++)
			{
				if(setmealInfoList.get(i).get_ID() == userSetmealList.get(j).get_ID())
				{
					if(setmealInfoList.get(i).get_ID() == Meal_Type.GROUP_SMS)
					{
						count = soap.get_userInfo().get_count();
					}
					else
					{
						count = userSetmealList.get(j).get_count();
					}
					
				}
			}
			
			setmealCountList.add(String.valueOf(count));
		}
		
		exListView.setOnGroupClickListener(new OnGroupClickListener() 
		{			
			//避免组折叠
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) 
			{
				return true;
			}
		});
		
		SetmealExpListAdapter adapter = new SetmealExpListAdapter(UsersSetmealActivity.this);
		exListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		
	}
	
	
	 public class SetmealExpListAdapter extends BaseExpandableListAdapter{
	    	private Context context;
	    	//private List<String> customerGroupList;
	    	//private List<List<String>> customerList;
	    	//private List<Map<String, Boolean>> groupCheckBoxList;
	    	//private List<List<Map<String, Boolean>>> childCheckBoxListList;
	    	
	    	//private static final String GROUP_NAME = "group_name";
	    	//private static final String CHILD_NAME = "child_name";
	    	
	    	public int groupHeight;
	    	public int childHeight;
	    	
	    	public TextView textview;
	    	
	    	ViewHolder holder;
	    	
	    	public SetmealExpListAdapter(Context context)
	    	{
	    		this.context = context;
//	    		customerGroupList = group_array;
//	    		customerList = child_array;
//	    		groupCheckBoxList = group_checkbox;
//	    		childCheckBoxListList = child_checkbox;
	    		//this.childCheckBox = new List<List<Map<String, Boolean>>>(child_checkbox);
	    	}

			@Override
			public int getGroupCount() {
				return setmealNameList.size();
			}

			@Override
			public int getChildrenCount(int groupPosition) {
				return setmealContentList.get(groupPosition).size();
			}

			@Override
			public Object getGroup(int groupPosition) {
				return setmealNameList.get(groupPosition);
			}

			@Override
			public Object getChild(int groupPosition, int childPosition) {
				return setmealContentList.get(groupPosition).get(childPosition);
			}

			@Override
			public long getGroupId(int groupPosition) {
				return groupPosition;
			}

			@Override
			public long getChildId(int groupPosition, int childPosition) {
				return childPosition;
			}

			@Override
			public boolean hasStableIds() {
				return false;
			}
			
			@Override
			public void notifyDataSetChanged() {
				super.notifyDataSetChanged();
			}

			@Override
			public View getGroupView(final int groupPosition, 
									 boolean isExpanded,
									 View convertView, 
									 ViewGroup parent) 
			{
				View v = convertView;
				if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = inflater.inflate(R.layout.setmeal_groupitem, null);
				}
				TextView nameText = (TextView) v.findViewById(R.id.groupText_setmeal);
				TextView countText = (TextView) v.findViewById(R.id.groupText_setmeal_count);
				//View childItemView = (View)v.findViewById(R.id.child_item_head);
				final String gcount = setmealCountList.get(groupPosition);
				final String gname = setmealNameList.get(groupPosition);
				nameText.setText(gname);
				countText.setText(gcount);
				
				return v;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.setmeal_childitem, null);
					//holder = new ViewHolder();
					//View childItemView = (View)convertView.findViewById(R.id.child_item_head);
					//holder.nameText = (TextView) convertView.findViewById(R.id.childText);
					//holder.contentText = (TextView) convertView.findViewById(R.id.groupText_setmeal);
					TextView textview = (TextView) convertView.findViewById(R.id.groupText_setmeal);
					convertView.setTag(textview);
				} else {
					textview = (TextView) convertView.getTag();
				}
				textview.setText(setmealContentList.get(groupPosition).get(childPosition));
				
				//holder.checkBox.setChecked(childCheckBoxListList.get(groupPosition).get(childPosition).get(CHILD_ITEM));

				
				
				return convertView;
			}

			@Override
			public boolean isChildSelectable(int groupPosition, int childPosition) {
				return true;
			}		
	    	
	    }
	 
		public static class ViewHolder {
			TextView nameText;
			TextView countText;
		}
	
	private class UpdateSetmealTask extends AsyncTask<Void,Void,Integer>
	{		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Void... params) 
		{
			int ret = 0;
			try
			{

				return ret;
			}
			catch (Exception e)
			{
				Log.d("UpdateSetmealTask", e.getMessage());
			}
			
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result) 
		{
			

		}
	}
	

}
