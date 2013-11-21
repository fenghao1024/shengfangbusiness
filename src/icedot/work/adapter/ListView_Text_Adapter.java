package icedot.work.adapter;

import icedot.work.shengfang.business.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListView_Text_Adapter extends BaseAdapter
{
	private  final class ListViewItem
	{
		TextView 	_textView;
	}
	private List<String>  _list;
	private Context _context;
	private LayoutInflater _layout;
	
	public ListView_Text_Adapter()
	{
		_list = new ArrayList<String>();
		_context = null;
		_layout = null;
	}
	
	public ListView_Text_Adapter(Context context)
	{
		_list = new ArrayList<String>();
		_context = context;
		_layout = LayoutInflater.from(_context);
	}
	
	public ListView_Text_Adapter(Context context,List<String> list)
	{
		_list = list;
		_context = context;
		_layout = LayoutInflater.from(_context);
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return _list.size();
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return _list.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		if(_layout == null || _list.size()== 0)
			return null;
		ListViewItem item = null;
		if(convertView == null)
		{
			item = new ListViewItem();
			convertView = _layout.inflate(R.layout.common_lv_textview, null);
			item._textView = (TextView)convertView.findViewById(R.id.common_lv_textview);
			convertView.setTag(item);
		}
		else
		{
			item = (ListViewItem)convertView.getTag();		
		}
		item._textView.setText(_list.get(position));
		return convertView;
	}

	public List<String> get_list() {
		return _list;
	}

	public void set_list(List<String> _list) {
		this._list = _list;
	}
}
