package icedot.work.application;

import icedot.work.common.Md5;
import icedot.work.struct.GlobleDefault;
import icedot.work.struct.HolidayInfo;
import icedot.work.struct.Meal_Type;
import icedot.work.struct.MessageInfo;
import icedot.work.struct.SetMealInfo;
import icedot.work.struct.ShopInfo;
import icedot.work.struct.UserInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class ShengFangSoap {
	
	private static final String s_wsdlUrl = "http://219.151.10.235:8023/Service/Service1.svc?wsdl";
	//private static final String s_wsdlUrl = "http://192.168.1.55/servicer/Service1.svc?wsdl";
    
	private static final String s_namesapce="http://tempuri.org/";
	
	private UserInfo _userInfo;					//个人信息
	private List<SetMealInfo> _userMealInfoList;	//个人购买的套餐列表
	private List<ShopInfo>  _shopList;			//商铺信息列表
	private List<MessageInfo> _msgList;			//历史信息
	private List<SetMealInfo>  _mealInfoList;	//套餐列表
	private List<HolidayInfo>  _holidayList; 	//节假日列表
	private MessageInfo	_addMsg;				//发布消息信息
	
	private int curMsgCount = 0;			//历史信息总数
	private int preMsgCount = 0;
	private int newestMsgCount = 0;
	private int defaultPageSize = 10;
	private int receiveCount;			//本次读取了多少条
	
	private String _appVer;
	private String _downUrl;
	private int _appVersionCode = -1;
	private int countPerPage = 10;

	public ShengFangSoap()
	{
		_userInfo = new UserInfo();
		_shopList = new ArrayList<ShopInfo>();
		_msgList = new ArrayList<MessageInfo>();
		_mealInfoList = new ArrayList<SetMealInfo>();
		_addMsg = new MessageInfo();
		_userMealInfoList = new ArrayList<SetMealInfo>();
		_holidayList = new ArrayList<HolidayInfo>();
	}
	
	public List<ShopInfo> get_shopList() 
	{
		return _shopList;
	}

	public UserInfo get_userInfo() 
	{
		return _userInfo;
	}
	
	public List<MessageInfo> get_msgList() 
	{
		return _msgList;
	}
	
	public List<SetMealInfo> get_mealInfoList() {
		return _mealInfoList;
	}

	public int get_msgTotal() 
	{
		return curMsgCount;
	}

	public String get_appVer() {
		return _appVer;
	}
	
	public int get_appVersionCode()
	{
		return _appVersionCode;
	}

	public String get_downUrl() {
		return _downUrl;
	}

	
	public MessageInfo get_addMsg() {
		return _addMsg;
	}

	public void set_addMsg(MessageInfo _addMsg) {
		this._addMsg = _addMsg;
	}	

	public List<SetMealInfo> get_userMealInfoList() {
		return _userMealInfoList;
	}
	
	public SetMealInfo get_setmealInfo(int year,int month,int day,int shopType)
	{
		for(int i = 0; i < _holidayList.size();i++)
		{
			HolidayInfo holiday = _holidayList.get(i);
			if(holiday.checkDate(year, month, day))
			{
				if(shopType == Meal_Type.MAIN_SHOP)
					return getMealInfo(Meal_Type.MAIN_HOLIDAY);
				else
					return getMealInfo(Meal_Type.MINOR_HOLIDAY);
			}
		}
		
		Calendar date = Calendar.getInstance(Locale.CHINA);
//		date.set(year, month, day);
		date.set(Calendar.YEAR, year);
		date.set(Calendar.MONTH, month - 1);
		date.set(Calendar.DAY_OF_MONTH, day);
		int week = date.get(Calendar.DAY_OF_WEEK);
		if(week == Calendar.SUNDAY || week == Calendar.SATURDAY)
		{
			if(shopType == Meal_Type.MAIN_SHOP)
				return getMealInfo(Meal_Type.MAIN_RESTDAY);
			else
				return getMealInfo(Meal_Type.MINOR_RESTDAY);
		}
		else
		{
			if(shopType == Meal_Type.MAIN_SHOP)
				return getMealInfo(Meal_Type.MAIN_WORKDAY);
			else
				return getMealInfo(Meal_Type.MINOR_WORKDAY);
		}
	}
	
	public SetMealInfo getMealInfo(int type)
	{
		for(int i = 0; i < _mealInfoList.size();i++)
		{
			SetMealInfo info = _mealInfoList.get(i);
			if(info.get_ID() == type)
			{
				return info;
			}
		}
		return null;
	}

	public void clearData() 
	{
		_userInfo.clear();
		_shopList.clear();
		_msgList.clear();
		_mealInfoList.clear();
		_addMsg.clear();
		receiveCount = 0;
		curMsgCount = 0;
		pageID = 0;
	}

	public int loginSoap(String account,String pwd)
	{
		String funName = "Login";
		// 第1步：创建SoapObject对象，并指定WebService的命名空间和调用的方法名
		SoapObject request = new SoapObject(s_namesapce, funName);
		// 第2步：设置WebService方法的参数
		request.addProperty("name", account);
		String md5Pwd = Md5.string2MD5(pwd);
		request.addProperty("password", md5Pwd);
		// 第3步：创建SoapSerializationEnvelope对象，并指定WebService的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		 // 设置bodyOut属性
		envelope.bodyOut = request;
		envelope.dotNet = true;
		// 第4步：创建HttpTransportSE对象，并指定WSDL文档的URL
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
        try
        {
            // 第5步：调用WebService
        	String callStr = "http://tempuri.org/IService1/" + funName;
            ht.call(callStr,envelope);
            if (envelope.getResponse() != null)
            {
            	_userInfo = new UserInfo();
                // 第6步：使用getResponse方法获得WebService方法的返回结果
            	String str = envelope.getResponse().toString();
            	
            	//JSONArray
            	
        		if(str.equals("-1"))
        		{
        			clearData();
        			return -1;
        		}
        		
        		_userInfo.set_ID(str);
            	return 1;
            }
            else
            {
            	return -2;
            }
        }
        catch (Exception e)
        {
        	Log.d("ShengFangSoap , loginSoap", "Soap Exception" + e.getMessage());         
            return -2;
        }
	}
	
	
	public int updateUserInfo_InSoap()
	{
		if(_userInfo.get_ID().length() == 0)
		{
			clearData();
			return -3;
		}
		
		String funName = "updateUser";
		// 第1步：创建SoapObject对象，并指定WebService的命名空间和调用的方法名
		SoapObject request = new SoapObject(s_namesapce, funName);
		// 第2步：设置WebService方法的参数
		request.addProperty("userid", _userInfo.get_ID());

		// 第3步：创建SoapSerializationEnvelope对象，并指定WebService的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		 // 设置bodyOut属性
		envelope.bodyOut = request;
		envelope.dotNet = true;
		// 第4步：创建HttpTransportSE对象，并指定WSDL文档的URL
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
        try
        {
            // 第5步：调用WebService
        	String callStr = "http://tempuri.org/IService1/" + funName;
            ht.call(callStr,envelope);
            if (envelope.getResponse() != null)
            {
            	_userInfo = new UserInfo();
                // 第6步：使用getResponse方法获得WebService方法的返回结果
            	String str = envelope.getResponse().toString();
            	JSONObject jsonObject = new JSONObject(str.toString());
            	//JSONArray
            	_userInfo.set_ID(jsonObject.getString("userid"));
            	_userInfo.set_account(jsonObject.getString("login_name"));
            	_userInfo.set_phone(jsonObject.getString("userid"));
            	_userInfo.set_address(jsonObject.getString("address"));
            	_userInfo.set_sex(jsonObject.getString("sex"));
            	_userInfo.set_name(jsonObject.getString("username"));
            	_userInfo.set_phone(jsonObject.getString("login_name"));
            	_userInfo.set_count(jsonObject.getInt("count"));
            	_userInfo.set_money(jsonObject.getDouble("money"));            

            	return 1;
            }
            else
            {
            	return -1;
            }
        }
        catch (Exception e)
        {
        	Log.d("ShengFangSoap, updateUserInfo_InSoap", "Soap Exception" + e.getMessage());         
            return -2;
        }
	}
	
	
	public int shopListSoap()
	{
		String funName = "SPInfoList";
		if(_userInfo.get_ID().length() == 0)
		{
			clearData();
			return -3;
		}
		_shopList.clear();
		SoapObject request = new SoapObject(s_namesapce, funName);
		request.addProperty("userid", _userInfo.get_ID());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
	    try
        {
	    	String callStr = "http://tempuri.org/IService1/" + funName;
            ht.call(callStr,envelope);
            if (envelope.getResponse() != null)
            {
            	String str = envelope.getResponse().toString();
            	JSONArray array = new JSONArray(str);  
            	for (int i = 0; i < array.length(); i++)  
            	{
            		JSONObject jsonObj = (JSONObject) array.getJSONObject(i);  
            		ShopInfo info = new ShopInfo();
            		info.set_address(jsonObj.getString("address"));
            		info.set_description(jsonObj.getString("description"));
            		info.set_ID(jsonObj.getInt("id"));
            		info.set_name(jsonObj.getString("spname"));
            		info.set_area(jsonObj.getString("sqid"));
            		info.set_phone(jsonObj.getString("phone"));
            		info.set_check(jsonObj.getInt("is_inable"));
            		info.set_areaType(jsonObj.getInt("is_sq"));
            		
            		_shopList.add(info);
            	}
            	return 1;
            }
            else
            {
            	return -1;
            }
        }
        catch (Exception e)
        {
        	Log.d("ShengFangSoap", "Soap Exception" + e.getMessage());
            e.printStackTrace();
            return -2;
        }
	}

	public int addMsgInfoSoap(MessageInfo info, int setmealID)
	{
		if(_userInfo.get_ID().length() == 0)
		{
			clearData();
			return -3;
		}
		
		String funName = "MessageGo";
		SoapObject request = new SoapObject(s_namesapce, funName);
		
		request.addProperty("userid",_userInfo.get_ID());
		request.addProperty("spid",info.get_shopID().toString());
		request.addProperty("title",info.get_title());
		request.addProperty("home",info.get_content());
		request.addProperty("begin_time",info.get_startTime());
		request.addProperty("end_time",info.get_endTime());
		request.addProperty("sendcount",info.get_count());
		request.addProperty("tcid", String.valueOf(setmealID));
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.encodingStyle = SoapEnvelope.ENC;
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
		try
        {
			String callStr = "http://tempuri.org/IService1/" + funName;
            ht.call(callStr,envelope);
            if (envelope.getResponse() != null)
            {
            	String str = envelope.getResponse().toString();
            	Integer ret = Integer.valueOf(str);
				if(ret == 2)
				{
					getUserSetMeal();
				}
            	return ret;
            }
            else
            {
            	return -1;
            }
        }
        catch (Exception e)
        {
        	Log.d("ShengFangSoap", "Soap Exception" + e.getMessage());
            e.printStackTrace();
            return -2;
        }
	}
	
	public int registerSoap(UserInfo info)
	{
		String funName = "Register";
		SoapObject request = new SoapObject(s_namesapce, funName);
		
		request.addProperty("login_name",info.get_account());
		String md5Pwd = Md5.string2MD5(info.get_pwd());
		request.addProperty("login_password",md5Pwd);
		request.addProperty("username",info.get_name());
		request.addProperty("sex",info.get_sex());
		request.addProperty("address",info.get_address());
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
	    try
        {
	    	String callStr = "http://tempuri.org/IService1/" + funName;
            ht.call(callStr,envelope);
            if (envelope.getResponse() != null)
            {
            	String str = envelope.getResponse().toString();
            	Integer ret = Integer.valueOf(str);
            	return ret;
            }
            else
            {
            	return -1;
            }
        }
        catch (Exception e)
        {
        	Log.d("ShengFangSoap", "Soap Exception" + e.getMessage());
            e.printStackTrace();
            return -2;
        }
	}
	
	public int getSentMsgCount_inSoap()
	{
		if(_userInfo.get_ID().length() == 0)
		{
			clearData();
			return -3;
		}
		String funName = "ALLCount";
		
		SoapObject request = new SoapObject(s_namesapce, funName);
		request.addProperty("userid", _userInfo.get_ID());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
		try
		{
	    	String callStr = "http://tempuri.org/IService1/" + funName;
            ht.call(callStr,envelope);
            if (envelope.getResponse() != null)
            {
            	
            	String str = envelope.getResponse().toString();
            	
            	curMsgCount = Integer.parseInt(str);
            	
            	return 1;
            }
            return 1;
		}
        catch (Exception e)
        {
            Log.d("ShengFangSoap", "Soap Exception" + e.getMessage());
            e.printStackTrace();
            return -2;
        }
		//return 1;
	}
	
	public int pageID = 0;
	public int msgInfoListSoap(int orentation)
	{
		if(_userInfo.get_ID().length() == 0)
		{
			clearData();
			return -3;
		}
		//boolean ifFirstGetMsg = false;
		int startPageNum = 0;
		int endPageNum = 0;
		String funName = "MessageList";
		
		int _newmsg;
		
		
		/// refrash curMsgCount
		int ret = this.getSentMsgCount_inSoap();
		//Log.d("getMsgCount_inSoap return value : ", " " + String.valueOf(ret));
		
		int localMsgCount = _msgList.size();

		if(localMsgCount == 0)
		{
			preMsgCount = curMsgCount;
			orentation = GlobleDefault.BACK_ORNT;
		}
		_newmsg = curMsgCount - preMsgCount;
		preMsgCount = curMsgCount;
		newestMsgCount += _newmsg;
		
		switch(orentation)
		{
		case GlobleDefault.FRONT_ORNT:
			startPageNum = newestMsgCount - defaultPageSize;
			newestMsgCount = startPageNum;
			if(newestMsgCount < 0)
			{
				newestMsgCount = 0;
			}
			endPageNum = startPageNum + defaultPageSize;
			break;
		case GlobleDefault.BACK_ORNT:
			startPageNum = localMsgCount + newestMsgCount;
			endPageNum = startPageNum + defaultPageSize;
			break;
		default:
			return -10;
		}
		
		if(curMsgCount == localMsgCount && curMsgCount != 0 && _msgList.size() != 0)
		{
			return 10;
		}
		if((_newmsg + localMsgCount) == curMsgCount && orentation == GlobleDefault.BACK_ORNT)
		{
			return 11;
		}
		if(_newmsg == 0 && orentation == GlobleDefault.FRONT_ORNT)
		{
			return 12;
		}
    	
    	SoapObject request = new SoapObject(s_namesapce, funName);
		request.addProperty("userid", _userInfo.get_ID());
    	//getNewesMsg
		request.addProperty("startnum",String.valueOf(startPageNum));
    	request.addProperty("endnum",String.valueOf(endPageNum));
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
	    try
        {
	    	String callStr = "http://tempuri.org/IService1/" + funName;
            ht.call(callStr,envelope);
            if (envelope.getResponse() != null)
            {
            	String str = envelope.getResponse().toString();
            	JSONObject jsonObject = new JSONObject(str);
            	
            	curMsgCount = jsonObject.getInt("allcount");
            	receiveCount = jsonObject.getInt("count");
            	
            	List<MessageInfo> tempMsgList = new ArrayList<MessageInfo>();
            	
            	JSONArray array = jsonObject.getJSONArray("list"); 
            	for (int i = 0; i < array.length(); i++)  
            	{
            		JSONObject jsonObj = (JSONObject) array.getJSONObject(i);  
            		MessageInfo info = new MessageInfo();
            		info.set_title(jsonObj.getString("title"));
            		info.set_content(jsonObj.getString("home"));
            		info.set_count(String.valueOf(jsonObj.getInt("sendcount")));
            		info.set_shopName(jsonObj.getString("spname")); 
            		info.set_startTime(jsonObj.getString("begin_time"));
            		info.set_endTime(jsonObj.getString("end_time"));
            		info.set_sendState(jsonObj.getInt("is_send"));	
            		tempMsgList.add(info);
            		
            	}
            	switch(orentation)
            	{
            	case GlobleDefault.BACK_ORNT:
            		_msgList.addAll(tempMsgList);
            		break;
            	case GlobleDefault.FRONT_ORNT:
            		_msgList.addAll(0, tempMsgList);
            		break;
            		default:
            			return 2;
            	}
            	receiveCount = 0;
            	return 1;
            }
            else
            {
            	return 2;
            }
        }
        catch (Exception e)
        {
            Log.d("ShengFangSoap", "Soap Exception" + e.getMessage());
            e.printStackTrace();
            return 3;
        }
	}
	

	
	public int defaultRechargeInterface_inSoap(double rechargeAmount)
	{
		String funcName = "PayFun";
		if(_userInfo.get_ID().length() == 0)
		{
			clearData();
			return -3;
		}
		
		SoapObject request = new SoapObject(s_namesapce, funcName);
		request.addProperty("userid", _userInfo.get_ID());
		request.addProperty("money", String.valueOf(rechargeAmount));
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);

		try 
		{
			String callStr = "http://tempuri.org/IService1/" + funcName;
			ht.call(callStr,envelope);
			if (envelope.getResponse() != null)
            {
				String str = envelope.getResponse().toString();
				char status = str.charAt(0);
				
				if(status == '0')
				{
					str = str.substring(1, str.length() - 1);
					if(rechargeAmount != Double.valueOf(str))
					{
						// 充值余额数据包被篡改
					}
				}
				
				return Integer.valueOf(status);
            }
			else
            {
            	return -3;
            }
		} 
		catch (Exception e) 
		{
			 Log.d("ShengFangSoap---upDateBalance_inSoap", "Soap Exception" + e.getMessage());
	         e.printStackTrace();
	         return -2;
		}
	}
	
	public int appVersionSoap()
	{
		String funName = "UpdateApp";
		SoapObject request = new SoapObject(s_namesapce, funName);
		request.addProperty("is_ios", 0);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
		try 
		{
			String callStr = "http://tempuri.org/IService1/" + funName;
			ht.call(callStr,envelope);
			if (envelope.getResponse() != null)
            {
				String str = envelope.getResponse().toString();
				JSONArray array = new JSONArray(str);  
            	for (int i = 0; i < array.length(); i++)  
            	{
            		JSONObject jsonObj = (JSONObject) array.getJSONObject(i); 
					_appVer = jsonObj.getString("version");
					_downUrl = jsonObj.getString("url");
					this._appVersionCode = jsonObj.getInt("versioncode");
            	}
            	
            	Log.d("appVersionSoap",_appVer + " " + _downUrl);
				return 1;
            }
			else
            {
            	return -1;
            }
		} 
		catch (Exception e) 
		{
			 Log.d("ShengFangSoap", "Soap Exception" + e.getMessage());
	         e.printStackTrace();
	         return -2;
		}
	}
	
	public int setmealListSoap()
	{
		if(_userInfo.get_ID().length() == 0)
		{
			clearData();
			return -3;
		}
		_mealInfoList.clear();
		String funName = "TCList";
		SoapObject request = new SoapObject(s_namesapce, funName);
		request.addProperty("userid", _userInfo.get_ID());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
		try 
		{
			String callStr = "http://tempuri.org/IService1/" + funName;
			ht.call(callStr,envelope);
			if (envelope.getResponse() != null)
            {
				String str = envelope.getResponse().toString();
				JSONArray array = new JSONArray(str);  
            	for (int i = 0; i < array.length(); i++)  
            	{
            		JSONObject jsonObj = (JSONObject) array.getJSONObject(i); 
            		if(jsonObj.getInt("is_show") == 0)
            		{
            			SetMealInfo info = new SetMealInfo();
                		info.set_count(jsonObj.getInt("count"));
                		info.set_ID(jsonObj.getInt("id"));
                		info.set_price(jsonObj.getDouble("money"));
                		info.set_type(jsonObj.getString("name"));
                		_mealInfoList.add(info);
            		}
            		
            	}
            	
            	Log.d("appVersionSoap",_appVer + " " + _downUrl);
				return 1;
            }
			else
            {
            	return -1;
            }
		} 
		catch (Exception e) 
		{
			 Log.d("ShengFangSoap", "Soap Exception" + e.getMessage());
	         e.printStackTrace();
	         return -2;
		}
	}
	
	//返回值: 0:用户不存在
	//        
	public int getHolidayList()
	{
		if(_userInfo.get_ID().length() == 0)
		{
			clearData();
			return -3;
		}
		_holidayList.clear();
		String funName = "FreedayList";
		SoapObject request = new SoapObject(s_namesapce, funName);
		request.addProperty("userid", _userInfo.get_ID());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
		try 
		{
			String callStr = "http://tempuri.org/IService1/" + funName;
			ht.call(callStr,envelope);
			if (envelope.getResponse() != null)
            {
				String str = envelope.getResponse().toString();
				JSONArray array = new JSONArray(str);  
            	for (int i = 0; i < array.length(); i++)  
            	{
            		JSONObject jsonObj = (JSONObject) array.getJSONObject(i); 
					HolidayInfo day = new HolidayInfo();
					String holidayStr = jsonObj.getString("datetime");
					int year = Integer.valueOf(holidayStr.substring(0, 4));
					int month = Integer.valueOf(holidayStr.substring(5, 7));
					int nday = Integer.valueOf(holidayStr.substring(8, 10));
					day.set_year(year);
					day.set_month(month);
					day.set_day(nday);
					
					_holidayList.add(day);
            	}
            	
            	Log.d("appVersionSoap",_appVer + " " + _downUrl);
				return 1;
            }
			else
            {
            	return -1;
            }
		} 
		catch (Exception e) 
		{
			 Log.d("ShengFangSoap", "Soap Exception" + e.getMessage());
	         e.printStackTrace();
	         return -2;
		}
	}
	
	public int getUserSetMeal()
	{
		if(_userInfo.get_ID().length() == 0)
		{
			clearData();
			return -3;
		}

		
		String funName = "TCResidue";
		SoapObject request = new SoapObject(s_namesapce, funName);
		request.addProperty("userid", _userInfo.get_ID());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
		try 
		{
			String callStr = "http://tempuri.org/IService1/" + funName;
			ht.call(callStr,envelope);
			if (envelope.getResponse() != null)
            {
				String str = envelope.getResponse().toString();
				JSONArray array = new JSONArray(str);
				if(_userMealInfoList != null)
					_userMealInfoList.clear();
            	for (int i = 0; i < array.length(); i++)  
            	{
            		JSONObject jsonObj = (JSONObject) array.getJSONObject(i); 
					SetMealInfo info = new SetMealInfo();
					info.set_ID(jsonObj.getInt("TCID"));
					info.set_count(jsonObj.getInt("COUNT"));
					info.set_type(jsonObj.getString("NAME"));
					_userInfo.set_money(jsonObj.getDouble("MONEY"));
					_userMealInfoList.add(info);					
            	}
            	
            	Log.d("appVersionSoap",_appVer + " " + _downUrl);
				return 1;
            }
			else
            {
            	return -1;
            }
		} 
		catch (Exception e) 
		{
			 Log.d("ShengFangSoap", "Soap Exception" + e.getMessage());
	         e.printStackTrace();
	         return -2;
		}
	}
	
	//群发
	public int messageGroupSending(String shopId, String title, String content, String beginTime, String endTime)
	{
		if(_userInfo.get_ID().length() == 0)
		{
			clearData();
			return -3;
		}
		
		String funcName = "VIPMessageGo";
		SoapObject request = new SoapObject(s_namesapce, funcName);
		
		request.addProperty("userid", _userInfo.get_ID());
		request.addProperty("spid", shopId);
		request.addProperty("title", title);
		request.addProperty("home", content);
		request.addProperty("begin_time", beginTime);
		request.addProperty("end_time", endTime);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
		try 
		{
			String callStr = "http://tempuri.org/IService1/" + funcName;
			ht.call(callStr,envelope);
			if (envelope.getResponse() != null)
            {
				String result = envelope.getResponse().toString();
				int ret = Integer.valueOf(result);
				if(ret == 0)
				{ 
					getUserSetMeal();
					updateUserInfo_InSoap();
					//this.loginSoap(_appConfig.get_username(), _appConfig.getPassword());
				}
				return ret;
            }
			else
            {
            	return -1;
            }
		} 
		catch (Exception e) 
		{
			 Log.d("ShengFangSoap", "Soap Exception" + e.getMessage());
	         e.printStackTrace();
	         return -2;
		}
	}


	
	
	public int buySetmeal_inSoap(String[] strOrder, String strTotalPrice)
	{
		if(_userInfo.get_ID().length() == 0)
		{
			clearData();
			return -3;
		}
		
		String funcName = "TCupdate";
		SoapObject request = new SoapObject(s_namesapce, funcName);
		request.addProperty("userid", _userInfo.get_ID());
		request.addProperty("detail", strOrder[0]);
		request.addProperty("money", strTotalPrice);
		request.addProperty("tcid", strOrder[1]);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(s_wsdlUrl);
		
		try 
		{
			String callStr = "http://tempuri.org/IService1/" + funcName;
			ht.call(callStr,envelope);
			if (envelope.getResponse() != null)
            {
				String result = envelope.getResponse().toString();
				int ret = Integer.valueOf(result);
				if(ret == 0)
				{ 
					getUserSetMeal();
					updateUserInfo_InSoap();
					//this.loginSoap(_appConfig.get_username(), _appConfig.getPassword());
				}
				return ret;
            }
			else
            {
            	return -1;
            }
		} 
		catch (Exception e) 
		{
			 Log.d("ShengFangSoap", "Soap Exception" + e.getMessage());
	         e.printStackTrace();
	         return -2;
		}
	}
	
}
