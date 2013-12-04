package icedot.work.struct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;

public class GlobleDefault {
	
	public static final int FRONT_ORNT = 1; // 滚动方向， 前方, 用于scollView
	public static final int BACK_ORNT = 2;  // 滚动方向， 后方, 用于scollView
	
	//public static final String[] MSGSENDED_RESULT = {"2323", "sdfdf", "sdfsd", "sdfsdf", "sdfsdf", "sdfsd", "fefe", "eee", "eee", "eee"};
	 
	
	
	public GlobleDefault() {
		
		//MSGSENDED_RESULT.add()

		
	}
	
	public static String getMsgSendedResult(int result)
	{
		String rstr;
		 switch(result)
		{
		case -1:
			rstr = "内部错误,请与管理员联系!";
			break;
		case -2:
			rstr = "无法连接服务器!";
			break;
		case 0:
			rstr = "商铺信息重复，添加失败！";
			break;
		case 1:
			rstr = "添加信息不完整，添加失败！";
			break;
		case 2:
			rstr = "信息添加成功";
			break;
		case 3:
			rstr = "信息添加失败！";
			break;
		case 4:
			rstr = "插入数据库失败！";
			break;
		case 5:
			rstr = "参数传递有误！";
			break;
		case 7:
			rstr = "您今天发送信息条数已达上限！";
			break;
		case 8:
			rstr = "群发短信剩余条数不够！";
			break;
		case 9:
			rstr = "您还没有设置VIP名单！";
			break;
		default:
			rstr = "Unknow Return Message!";
			break;
		}
		return rstr;
	}

}
