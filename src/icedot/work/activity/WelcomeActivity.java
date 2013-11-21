package icedot.work.activity;


import icedot.work.application.ShengFangAppConfig;
import icedot.work.application.ShengFangApplication;
import icedot.work.application.ShengFangSoap;
import icedot.work.shengfang.business.R;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class WelcomeActivity extends Activity 
{
	private ViewPager _viewpager = null;
	private List<View> _list = null;
	private ShengFangAppConfig _appConfig = null;
	private ShengFangApplication _app;
	private Button _btn_finish = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
//		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//		String phoneId = tm.getLine1Number();
		
		_app = (ShengFangApplication)getApplication();
		
		initView();
		initData();	
		
		AppUpdateTask task = new AppUpdateTask();
		task.execute();
//		GlobalData.getCustomToast(this, 
//				this.getResources().getDrawable(R.drawable.custom_toast_icon),
//				GlobalData.getCurrentVersionName(this), Toast.LENGTH_LONG).show();
	}

	private void initView() 
	{
		_viewpager = (ViewPager) findViewById(R.id.welcome_viewpager);
	}
	private void initData() 
	{
		_appConfig = _app.getAppConfig();
		_list = new ArrayList<View>();
		
		
		if(_appConfig.get_isFirstRun())
		{		
			View view1 = getLayoutInflater().inflate(R.layout.welcome_viewpager_1, null);
			RelativeLayout layout1 = (RelativeLayout) view1.findViewById(R.id.welcome_viewpager_layout_src);
			layout1.setBackgroundResource(R.drawable.viewpage_1);
			ImageView selectView1 = (ImageView) view1.findViewById(R.id.welcome_viewpager_image_1);
			selectView1.setBackgroundResource(R.drawable.guide_focus);
			
			View view2 = getLayoutInflater().inflate(R.layout.welcome_viewpager_1, null);
			RelativeLayout layout2 = (RelativeLayout) view2.findViewById(R.id.welcome_viewpager_layout_src);
			layout2.setBackgroundResource(R.drawable.viewpage_2);
			ImageView selectView2 = (ImageView) view2.findViewById(R.id.welcome_viewpager_image_2);
			selectView2.setBackgroundResource(R.drawable.guide_focus);
			
			View view3 = getLayoutInflater().inflate(R.layout.welcome_viewpager_1, null);
			RelativeLayout layout3 = (RelativeLayout) view3.findViewById(R.id.welcome_viewpager_layout_src);
			layout3.setBackgroundResource(R.drawable.viewpage_3);
			ImageView selectView3 = (ImageView) view3.findViewById(R.id.welcome_viewpager_image_3);
			selectView3.setBackgroundResource(R.drawable.guide_focus);
			
			_list.add(view1);
					
			_list.add(view2);

			_list.add(view3);
			
			_btn_finish = (Button) view3.findViewById(R.id.welcome_viewpager_btn_finish);
			_btn_finish.setVisibility(View.INVISIBLE);
			_btn_finish.setOnClickListener(new OnClickListener()
	    	{						
				@Override
				public void onClick(View v) 
				{
					_appConfig.set_isFirstRun(false);
					_appConfig.commit();
					startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
					finish();
				}
			});
		}
		else
		{
			View lastView = getLayoutInflater().inflate(R.layout.welcome_viewpager_1, null);
			
			RelativeLayout layout_main = (RelativeLayout) lastView.findViewById(R.id.welcome_viewpager_layout_src);
			Button btn_finish = (Button) lastView.findViewById(R.id.welcome_viewpager_btn_finish);
			btn_finish.setVisibility(View.INVISIBLE);
			layout_main.setBackgroundResource(R.drawable.qst_welcome);
			btn_finish.setOnClickListener(new OnClickListener()
	    	{						
				@Override
				public void onClick(View v) 
				{
					startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
					finish();
				}
			});
			_list.add(lastView);
		}		
		_viewpager.setAdapter(new ViewPagerAdapter(_list));
	}

	private class AppUpdateTask extends AsyncTask<Void,Void,Integer>
	{
		@Override
		protected Integer doInBackground(Void... params) 
		{
			ShengFangSoap soap = _app.getSoap();
			int ret = soap.appVersionSoap();
			int repeat = 2;
			try
			{				
				while(repeat > 0 && (ret == -1 || ret == -2))
				{
					Thread.sleep(1000);
					ret = soap.appVersionSoap();
					repeat--;
				}
				if(ret == 1)
				{
					if(_app.getAppVersionCode()<soap.get_appVersionCode())
					{
						_app.set_updateApp(true);
						
					}
				}
				if(_btn_finish == null)
				{
					Thread.sleep(repeat * 1000);
				}
			}
			catch (Exception e)
			{
				Log.d("AppUpdateTask", e.getMessage());
			}
			return ret;
		}
		
		@Override
		protected void onPostExecute(Integer result) 
		{
			if(_btn_finish != null)
			{
				_btn_finish.setVisibility(View.VISIBLE);
			}
			else
			{
				startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
				WelcomeActivity.this.finish();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	 class ViewPagerAdapter extends PagerAdapter 
	 {
        private List<View> list = null;

        public ViewPagerAdapter(List<View> list)
        {
            this.list = list;
        }

        @Override
        public int getCount() 
        {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) 
        {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) 
        {
            container.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) 
        {
            return arg0 == arg1;
        }

    }
}
