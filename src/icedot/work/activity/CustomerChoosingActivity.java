package icedot.work.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangApplication.MyDataCache;
import icedot.work.shengfang.business.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;




public class CustomerChoosingActivity extends Activity {
	
	private Button btnCancel = null;
	private Button btnComplete = null;
	
	private ExpandableListView Customer_ExpListView = null;
	
	private List<String> customerGroupList = null;
	private List<List<String>> customerList = null;
	
	private List<Map<String, Boolean>> groupCheckBoxList;
	private List<List<Map<String, Boolean>>> childCheckBoxListList;
	
	private List<String> selectedCustomerList = null;
	
	private static final String GROUP_ITEM = "group_name";
	private static final String CHILD_ITEM = "child_name";
	
	private MyExpListAdapter expListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_choosing);
		
		this.initView();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.customer_choosing, menu);
		return true;
	}
	
	
	public void initView()
	{
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnComplete = (Button)findViewById(R.id.btnComplete);
		
		this.initCustomerList();
	}
	

	private void initCustomerList()
	{
		CustomerListDataReader();
	}
	
	public void CustomerListStringInit()
	{

	}
	
	public void btnCompleteOnClick(View view)
	{
		ShengFangApplication context = (ShengFangApplication) getApplicationContext();
		MyDataCache dc = context.getDataCache();
		dc.setData(this.getSelectedCustomerList());
		finish();
	}
	
	public String getSelectedCustomerList()
	{
		String selectedCustomerStr = "";
		selectedCustomerList = new ArrayList<String>();
		for(int i = 0; i < groupCheckBoxList.size(); i++)
		{
			for(int j = 0; j < childCheckBoxListList.get(i).size(); j++)
			{
				if(childCheckBoxListList.get(i).get(j).get(CHILD_ITEM))
				{
					String strTemp = customerList.get(i).get(j);
					selectedCustomerStr += strTemp;
					selectedCustomerStr += "    ";
				}
			}
		}
		
		
		return selectedCustomerStr;
	}
	
	public void btnCancelOnClick(View view)
	{
		finish();
	}
	
	private void CustomerListDataReader()
	{
		Customer_ExpListView = (ExpandableListView)findViewById(R.id.customer_list);
		
		customerGroupList = new ArrayList<String>();
		customerList = new ArrayList<List<String>>();
		
    	groupCheckBoxList = new ArrayList<Map<String,Boolean>>();
    	childCheckBoxListList = new ArrayList<List<Map<String,Boolean>>>();
		
		String strGroup[] = {"大十字商圈组", "超级VIP", "城管大队", "煤炭局", "爱吃鱼"};
		String strChild[][] = {{"张老板", "王店长", "贵阳人才网", "李行长"}, {"Mick", "JackBill", "Bob"}, {"军哥", "勇哥"}, {"局长", "煤炭秘书"}, {"张老头", "李老二"}};

		
		for(int i = 0; i < strGroup.length; i++)
		{
			customerGroupList.add(strGroup[i]);
    		Map<String, Boolean> curGB = new HashMap<String, Boolean>();
			curGB.put(GROUP_ITEM, false);
			groupCheckBoxList.add(curGB);
		}
		
		for(int i = 0; i < strChild.length; i++)
		{
			List<String> tempList = new ArrayList<String>();
			List<Map<String, Boolean>> childCB = new ArrayList<Map<String,Boolean>>();
			for(int j = 0; j < strChild[i].length; j++)
			{
				Map<String, Boolean> curCB = new HashMap<String, Boolean>();
				curCB.put(CHILD_ITEM, false);
				childCB.add(curCB);
				tempList.add(strChild[i][j]);
			}
			customerList.add(tempList);
			childCheckBoxListList.add(childCB);
		}
		
		expListAdapter = new MyExpListAdapter(CustomerChoosingActivity.this);
		this.Customer_ExpListView.setAdapter(expListAdapter);
		

		
		Customer_ExpListView.setOnGroupClickListener(new OnGroupClickListener() {  
            @Override  
            public boolean onGroupClick(ExpandableListView parent, 
            							View clickedView, 
            							int groupPosition, 
            							long groupId
            							) 
            {  
            	//expListView_Height = 0;
            	//expListAdapter.ExpListView_HeightChange();
                return false;
            }  
        }); 
		Customer_ExpListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, 
										View v,
										int groupPosition, 
										int childPosition, 
										long id
										) 
			{

				//CheckBox checkBox = (CheckBox) v.findViewById(R.id.childCheckBox);
				if((CheckBox)v.findViewById(R.id.groupCheckBox) == null)
				{
					Log.e("xxxxxxxxxxxxxxxxxxxxxxxxxxx", "yyyyyyyyyyyyyyyyyyyyyyyyy");
				}
				//Log.e("balala" + groupPosition, "xxoo" + childPosition);
				
				Boolean curState = childCheckBoxListList.get(groupPosition).get(childPosition).get(CHILD_ITEM);
				childCheckBoxListList.get(groupPosition).get(childPosition).put(CHILD_ITEM, !curState);
				//checkBox.setSelected(!curState);
				
				groupCheckBoxList.get(groupPosition).put(GROUP_ITEM, true);
				for(int i = 0; i < childCheckBoxListList.get(groupPosition).size(); i++)
				{
					if(!childCheckBoxListList.get(groupPosition).get(i).get(CHILD_ITEM))
					{
						groupCheckBoxList.get(groupPosition).put(GROUP_ITEM, false);
						break;
					}
				}
				
				expListAdapter.notifyDataSetChanged();
				return false;
			}
		});
		
		Customer_ExpListView.setOnGroupExpandListener(new OnGroupExpandListener() { 

			            @Override 
			            public void onGroupExpand(int groupPosition) {
			            	expListAdapter.ExpListView_HeightChange();
			            } 
		});
		
		
		Customer_ExpListView.setOnGroupCollapseListener(new OnGroupCollapseListener(){

			@Override
			public void onGroupCollapse(int groupPosition) {
				// TODO Auto-generated method stub
				
			}
			
		});

		//int x = this._custmoer_list_view.getChildCount();
/*		View v = (View)expListAdapter.getChild(1, 1);
		if(v == null)
		{
			Log.e("		View v = (View)expListAdapter.getChild(1, 1);", " null null null null");
		}
		else
		{
			Log.e(".....  " + v.getHeight(), "bababababa");
		}*/

		
	}

	
    public class MyExpListAdapter extends BaseExpandableListAdapter{
    	private Context context;
    	//private List<String> customerGroupList;
    	//private List<List<String>> customerList;
    	//private List<Map<String, Boolean>> groupCheckBoxList;
    	//private List<List<Map<String, Boolean>>> childCheckBoxListList;
    	
    	//private static final String GROUP_NAME = "group_name";
    	//private static final String CHILD_NAME = "child_name";
    	
    	public int groupHeight;
    	public int childHeight;
    	
    	ViewHolder holder;
    	public MyExpListAdapter(Context context)
    	{
    		this.context = context;
//    		customerGroupList = group_array;
//    		customerList = child_array;
//    		groupCheckBoxList = group_checkbox;
//    		childCheckBoxListList = child_checkbox;
    		//this.childCheckBox = new List<List<Map<String, Boolean>>>(child_checkbox);
    	}

		@Override
		public int getGroupCount() {
			return customerGroupList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return customerList.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return customerGroupList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return customerList.get(groupPosition).get(childPosition);
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
				v = inflater.inflate(R.layout.group_item, null);
			}
			TextView groupText = (TextView) v.findViewById(R.id.groupText);
			//View childItemView = (View)v.findViewById(R.id.child_item_head);
			final CheckBox gCheckBox = (CheckBox) v.findViewById(R.id.groupCheckBox);
			final String gname = customerGroupList.get(groupPosition);
			groupText.setText(gname);
			gCheckBox.setChecked(groupCheckBoxList.get(groupPosition).get(GROUP_ITEM));
			gCheckBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Boolean isChecked = gCheckBox.isChecked();

					groupCheckBoxList.get(groupPosition).put(GROUP_ITEM, isChecked);
					setCheckStateToChilds(groupPosition, isChecked);
					notifyDataSetChanged();
					
				}
			});
			
			

			return v;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.child_item, null);
				holder = new ViewHolder();
				//View childItemView = (View)convertView.findViewById(R.id.child_item_head);
				holder.cText = (TextView) convertView.findViewById(R.id.childText);
				holder.checkBox = (CheckBox) convertView.findViewById(R.id.childCheckBox);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.cText.setText(customerList.get(groupPosition).get(childPosition));
			holder.checkBox.setChecked(childCheckBoxListList.get(groupPosition).get(childPosition).get(CHILD_ITEM));

			
			
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
		public void setCheckStateToChilds(int groupPosition, Boolean isChecked)
		{
			for(int i = 0; i < childCheckBoxListList.get(groupPosition).size(); i++)
			{
				childCheckBoxListList.get(groupPosition).get(i).put(CHILD_ITEM, isChecked);
			}
		}
		
		public void ExpListView_HeightChange()
		{

		}
		
		
    	
    }

	public static class ViewHolder {
		TextView cText;
		CheckBox checkBox;
	}
	

}
