package icedot.work.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShengFangAppConfig 
{
	private boolean _isFirstRun;//判断是否是第一次登录
	private boolean _isAutoCheck;
	private String _username;
	private String _password;

	private Context _context;
	private  final String SHARE_PREGERENCE_NM="ShengFangBusiness";

	private SharedPreferences _sharedPreferences;
	private Editor _editor;
	private static ShengFangAppConfig _settings;

	public String get_username() 
	{
		return _username;
	}
	
	public String getPassword()
	{
		return _password;
	}
	
	public void setPassword(String psw)
	{
		this._password = psw;
		_editor.putString("password", psw);
	}

	public void set_username(String m_username) 
	{
		_username = m_username;
		_editor.putString("username", m_username);
	}
//
//	public String get_password() {
//		return _password;
//	}
//
//	public ShengFangAppConfig set_password(String m_password) {
//		_password = m_password;
//		_editor.putString("password", m_password);
//		return this;
//	}
	
	private ShengFangAppConfig(Context context)
	{
		this._context=context;
		initData();
	}
	
	/**
	 * 获取实例
	 * @param context
	 * @return
	 */
	public static ShengFangAppConfig getInstance(Context context)
	{
		if(_settings==null)
		{
			_settings=new ShengFangAppConfig(context);
		}
		return _settings;
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() 
	{
		_sharedPreferences=_context.getSharedPreferences(SHARE_PREGERENCE_NM, Context.MODE_PRIVATE);
		_editor=_sharedPreferences.edit();
		_isFirstRun=_sharedPreferences.getBoolean("isFirstRun", true);
		_isAutoCheck=_sharedPreferences.getBoolean("isAutoCheck", true);
		_username = _sharedPreferences.getString("username", "");
		_password = _sharedPreferences.getString("password", "");
		
	}

	/**
	 * 获取是否是第一次登录
	 * @return
	 */
	public boolean get_isFirstRun() {
		return _isFirstRun;
	}

	/**
	 * 更改是否是第一次登录
	 * @param m_isFirstRun
	 */
	public void set_isFirstRun(boolean m_isFirstRun) {
		_isFirstRun = m_isFirstRun;
		_editor.putBoolean("isFirstRun", m_isFirstRun);
	}
	
	//-----------------------是否自动检测程序更新------------------------------
	/**
	 * 是否自动检测程序更新
	 * @return
	 */
	public boolean get_isAutoCheck() {
		return _isAutoCheck;
	}

	public void set_isAutoCheck(boolean m_isAutoCheck) {
		_isAutoCheck = m_isAutoCheck;
		_editor.putBoolean("isAutoCheck", m_isAutoCheck);
	}
	//----------------------是否自动检测程序更新-------------------------------
	/**
	 * 提交事务修改
	 */
	public void commit()
	{
		if(_editor!=null)
		{
			_editor.commit();
		}
	}
}
