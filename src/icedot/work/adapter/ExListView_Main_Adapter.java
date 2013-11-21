package icedot.work.adapter;

import icedot.work.shengfang.business.R;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExListView_Main_Adapter extends BaseExpandableListAdapter
{
	private Context _context = null;
	private List<Map<String, Object>> _group = null;
	private List<List<Map<String, Object>>> _child = null;
	private LayoutInflater _inflater = null;
	private int _groupID;
	private int _childID;

	public ExListView_Main_Adapter(Context context, List<Map<String, Object>> group,
			List<List<Map<String, Object>>> child) 
	{
		_context = context;
		_group = group;
		_child = child;
		_inflater = LayoutInflater.from(_context);
	}
	
	public void updateClicked(int groupid,int childid)
	{
		if(_group == null || _group.size() <= 0)
			return;
		if(_child == null || _child.size() <= 0)
			return;
		_child.get(_groupID).get(_childID).put("click",false);
		_child.get(groupid).get(childid).put("click",true);
		_groupID = groupid;
		_childID = childid;
	}

	public Object getChild(int groupPosition, int childPosition) 
	{
		return _child.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) 
	{
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) 
	{
		ChildItem item = null;
		if (convertView == null) 
		{
			convertView = _inflater.inflate(R.layout.exlistview_main_child, null);
			item = new ChildItem();
			item._image = (ImageView) convertView
					.findViewById(R.id.exlistview_main_child_icon);
			item._text = (TextView) convertView
					.findViewById(R.id.exlistview_main_child_name);
			convertView.setTag(item);
		} 
		else 
		{
			item = (ChildItem) convertView.getTag();
		}
		item._image.setImageDrawable(_context.getResources().getDrawable(
				Integer.parseInt(_child.get(groupPosition).get(childPosition)
						.get("icon").toString())));
		item._text.setText(_child.get(groupPosition).get(childPosition)
				.get("name").toString());
		convertView.setBackgroundResource(R.drawable.main_exlistview_childselector);
		
//		boolean bl = (Boolean)_child.get(groupPosition).get(childPosition).get("click");
////		Log.d("getChildView","childPosition=" + childPosition + 
////				" groupPosition=" + groupPosition + "clicked=" + bl);
//		if(bl)
//		{
//			convertView.setBackgroundResource(R.drawable.desktop_list_item_pressed);
//		}
//		else
//		{
//			convertView.setBackgroundResource(R.drawable.desktop_list_item_bg);
//		}
		return convertView;
	}

	public int getChildrenCount(int groupPosition) 
	{
		return _child.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) 
	{
		return _group.get(groupPosition);
	}

	public int getGroupCount() 
	{
		return _group.size();
	}

	public long getGroupId(int groupPosition) 
	{
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) 
	{
		GroupItem item = null;
		if (convertView == null) 
		{
			convertView = _inflater.inflate(R.layout.exlistview_main_group, null);
			item = new GroupItem();
			item._text = (TextView) convertView
					.findViewById(R.id.desktop_list_group_name);
			convertView.setTag(item);
		} 
		else 
		{
			item = (GroupItem) convertView.getTag();
		}
		item._text.setText(_group.get(groupPosition).get("name")
				.toString());
		return convertView;
	}

	public boolean hasStableIds() 
	{
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) 
	{
		return true;
	}
	
	private class ChildItem
	{
		ImageView			_image;
		TextView			_text;
	}
	
	private class GroupItem
	{
		TextView		_text;
	}
}
