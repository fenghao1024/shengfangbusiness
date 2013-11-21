package icedot.work.application;

import icedot.work.common.AnimationsUtils;
import icedot.work.common.GlobalData;
import icedot.work.struct.SetMealInfo;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.util.Log;

public class ShengFangApplication extends Application 
{

	private AnimationsUtils _anims;
	private ShengFangSoap _soap;
	private ShengFangAppConfig _globalSettings = null;
	private String _version = null;
	private int _versionCode;
	private boolean _updateApp = false;
	
	public ShengFangApplication() 
	{
		
	}
	
	public AnimationsUtils getAnimationsUtils()
	{
		if(_anims==null)
		{
			_anims=new AnimationsUtils(getApplicationContext());
		}
		return _anims;
	}
	
	public ShengFangSoap getSoap()
	{
		if(null == _soap)
			_soap = new ShengFangSoap();

		return _soap;
	}
	
	/**
	 * 获取全局配置文件
	 * @return
	 */
	public ShengFangAppConfig getAppConfig() 
	{		
		_globalSettings = ShengFangAppConfig.getInstance(this.getApplicationContext());
		return _globalSettings;
	}
	
	public String getAppVersion()
	{
		if(_version == null || _version.length() == 0)
		{
			_version = GlobalData.getCurrentVersionName(this);
		}
		return _version;
	}
	
	public int getAppVersionCode()
	{
		this._versionCode = GlobalData.getCurrentVersionCode(this);
		return this._versionCode;
	}

	public boolean is_updateApp() {
		return _updateApp;
	}

	public void set_updateApp(boolean _updateApp) {
		this._updateApp = _updateApp;
	}
	
	public String setmealInfoToJSON(List<SetMealInfo> list)
	{
		JSONObject json = new JSONObject();
		
		JSONArray arr = new JSONArray();
		
		try 
		{
			for(int i = 0; i < list.size();i++)
			{
				JSONObject obj = new JSONObject();
				obj.put("id", list.get(i).get_ID());
				obj.put("type", list.get(i).get_type());
				obj.put("price", list.get(i).get_price());
				
				arr.put(obj);
			}
			json.put("userid", _soap.get_userInfo().get_ID());
			json.put("count", list.size());
			json.put("list", arr);
			return json.toString();
		} 
		catch (JSONException e) 
		{
			Log.d("JSONException",e.getMessage());
			return null;
		}	
		
	}
	
	public String setRechargeTypeToJSON() 
	{
		JSONObject json = new JSONObject();
		
		try
		{
			json.put("type", "price");
		}
		catch(JSONException e)
		{
			Log.d("JSONException",e.getMessage());
			return null;
		}

		return json.toString();
	}
	
	public MyDataCache getDataCache()
	{
		if(dataCache == null)
		{
			this.dataCache = new MyDataCache();
		}
		return this.dataCache;
	}
	
	MyDataCache dataCache;
	
	public class MyDataCache{
		String dataStr;
		MyDataCache()
		{
			//dataStr = "ba;ldkfjasdf";
		}
		public void setData(String data)
		{
			this.dataStr = data;
		}
		public String getData()
		{
			return this.dataStr;
		}
	}
	
	
}
