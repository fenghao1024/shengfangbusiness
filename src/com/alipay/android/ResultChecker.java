package com.alipay.android;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * 对签名进行验签
 * 
 */
public class ResultChecker {

	public static final int RESULT_INVALID_PARAM = 0;
	public static final int RESULT_CHECK_SIGN_FAILED = 1;
	public static final int RESULT_CHECK_SIGN_SUCCEED = 2;

	String mContent;
	
	public static final Map<String, String> ResultStatus;
	
	static {
		ResultStatus = new HashMap<String, String>();
		ResultStatus.put("9000", "操作成功");
		ResultStatus.put("4000", "系统异常");
		ResultStatus.put("4001", "数据格式不正确");
		ResultStatus.put("4003", "该用户绑定的支付宝账户被冻结或不允许支付");
		ResultStatus.put("4004", "该用户已解除绑定");
		ResultStatus.put("4005", "绑定失败或没有绑定");
		ResultStatus.put("4006", "订单支付失败");
		ResultStatus.put("4010", "重新绑定账户");
		ResultStatus.put("6000", "支付服务正在进行升级操作");
		ResultStatus.put("6001", "用户中途取消支付操作");
		ResultStatus.put("7001", "网页支付失败");
	}
	
	
	public String getResultStatusByCode(int code)
	{
		if(ResultStatus.containsKey(String.valueOf(code)))
			return ResultStatus.get(String.valueOf(code));
		else
			return "未知错误，请联系支付宝开发人员";
	}
	

	public ResultChecker(String content) {
		this.mContent = content;
	}

//	/**
//	 * 从验签内容中获取成功状态
//	 * 
//	 * @return
//	 */
//	private String getSuccess() {
//		String success = null;
//
//		try {
//			JSONObject objContent = BaseHelper.string2JSON(this.mContent, ";");
//			String result = objContent.getString("result");
//			result = result.substring(1, result.length() - 1);
//
//			JSONObject objResult = BaseHelper.string2JSON(result, "&");
//			success = objResult.getString("success");
//			success = success.replace("\"", "");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return success;
//	}

	/**
	 * 对签名进行验签
	 * 
	 * @return
	 */
	public int checkSign() {
		int retVal = RESULT_CHECK_SIGN_SUCCEED;

		try {
			JSONObject objContent = BaseHelper.string2JSON(this.mContent, ";");
			String result = objContent.getString("result");
			result = result.substring(1, result.length() - 1);
			// 获取待签名数据
			int iSignContentEnd = result.indexOf("&sign_type=");
			String signContent = result.substring(0, iSignContentEnd);
			// 获取签名
			JSONObject objResult = BaseHelper.string2JSON(result, "&");
			String signType = objResult.getString("sign_type");
			signType = signType.replace("\"", "");

			String sign = objResult.getString("sign");
			sign = sign.replace("\"", "");
			// 进行验签 返回验签结果
			if (signType.equalsIgnoreCase("RSA")) {
				if (!Rsa.doCheck(signContent, sign,
						PartnerConfig.RSA_ALIPAY_PUBLIC))
					retVal = RESULT_CHECK_SIGN_FAILED;
			}
		} catch (Exception e) {
			retVal = RESULT_INVALID_PARAM;
			e.printStackTrace();
		}

		return retVal;
	}

//	boolean isPayOk() {
//		boolean isPayOk = false;
//
//		String success = getSuccess();
//		if (success.equalsIgnoreCase("true")
//				&& checkSign() == RESULT_CHECK_SIGN_SUCCEED)
//			isPayOk = true;
//
//		return isPayOk;
//	}
}