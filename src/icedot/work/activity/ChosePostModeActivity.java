package icedot.work.activity;

import icedot.work.shengfang.business.R;
import icedot.work.shengfang.business.R.layout;
import icedot.work.shengfang.business.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ChosePostModeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chose_post_mode);
		
		this.initView();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chose_post_mode, menu);
		return true;
	}
	
	private Button modeA;
	private Button modeB;
	
	public void initView()
	{
		modeA = (Button)findViewById(R.id.btnModeA);
		modeB = (Button)findViewById(R.id.btnModeB);
		
		modeA.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChosePostModeActivity.this,AddMessageActivity.class);
				startActivity(intent);
			}
		});
		
		modeB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChosePostModeActivity.this,PostMessageModeBActivity.class);
				startActivity(intent);
			}
		});
		
		
	}
	
	public void btn_back_onClick(View v)
	{
		finish();
	}
	
	

}
