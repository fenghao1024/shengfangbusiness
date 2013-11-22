package icedot.work.activity;

import java.util.List;

import icedot.work.shengfang.business.R;
import icedot.work.shengfang.business.R.layout;
import icedot.work.shengfang.business.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;

public class UsersSetmealActivity extends Activity {
	
	ExpandableListView exListView = null;
	
	private List<String> setmealNameList = null;
	private List<String> setmealCountList = null;
	private List<List<String>> setMealCountList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users_setmeal);
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
		
		
	}
	
	public void initData()
	{
		
	}
	
	

}
