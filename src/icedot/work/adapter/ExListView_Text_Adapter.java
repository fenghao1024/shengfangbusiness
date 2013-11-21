package icedot.work.adapter;

import icedot.work.common.GlobalData;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExListView_Text_Adapter extends BaseExpandableListAdapter  {
	private Activity _activity;
	private  List<List<String>> _childArray;//子列表
	private  List<String> _groupArray;
	
	public ExListView_Text_Adapter(Activity activity,List<String> group,
			List<List<String>> child)
	{
		_activity = activity;
		_groupArray = group;
		_childArray = child;
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return _childArray.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String string =_childArray.get(groupPosition).get(childPosition);
		return getGenericView(string);
		//return null;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return _childArray.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		 return _groupArray.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return _groupArray.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		String   string=_groupArray.get(groupPosition); 
         return getGenericView(string); 

	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private TextView getGenericView(String string )
	{
		TextView  textView =new TextView(_activity);
		textView.setGravity(Gravity.CENTER_VERTICAL |Gravity.LEFT);
		textView.setPadding(50, 0, 0, 0);
		textView.setText(string);
		textView.setTextSize(16);
		textView.setTextColor(Color.BLACK);
		
		int scale= GlobalData.dipTopix(_activity,35);;
		AbsListView.LayoutParams params=new AbsListView.LayoutParams
				(LayoutParams.MATCH_PARENT, scale);

		textView.setLayoutParams(params);
		return textView;
	}

}
