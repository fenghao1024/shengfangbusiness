package icedot.work.activity;

import icedot.work.application.ShengFangApplication;
import icedot.work.shengfang.business.R;
import icedot.work.struct.PayInfo;
import icedot.work.struct.View_Selector;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alipay.android.AlixId;
import com.alipay.android.BaseHelper;
import com.alipay.android.MobileSecurePayHelper;
import com.alipay.android.MobileSecurePayer;
import com.alipay.android.PartnerConfig;
import com.alipay.android.ResultChecker;
import com.alipay.android.Rsa;


public class AlipayActivity extends Activity 
{
	private PayInfo _payInfo;
	private ProgressDialog mProgress = null;
	//private int mPosition=-1;
	
	// 这里接收支付结果，支付宝手机端同步通知
	private Handler mHandler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			try 
			{
				String ret = (String) msg.obj;

				Log.e("AlipayActivity", ret);
				switch (msg.what) 
				{
				case AlixId.RQF_PAY: 
				{
					//
					closeProgress();

					BaseHelper.log("AlipayActivity", ret);

						// 处理交易结果
						try
						{
							// 获取交易状态码，具体状态代码请参看文档
							int intTradeStatus;
							String strTradeStatus;
							JSONObject objContent = BaseHelper.string2JSON(ret, ";");
							String tradeStatus = objContent.getString("resultStatus");
							
							tradeStatus = tradeStatus.substring(1, tradeStatus.length() - 1);
							intTradeStatus = Integer.valueOf(tradeStatus);
	
							// 先验签通知
							ResultChecker resultChecker = new ResultChecker(ret);
							
							strTradeStatus = resultChecker.getResultStatusByCode(intTradeStatus);
							
							int retVal = resultChecker.checkSign();
							// 验签失败
							if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) 
							{
								intTradeStatus = 100001;
								strTradeStatus = "您的订单已经被非法篡改！！请拨110";
							} 
				
							Intent intent = new Intent(AlipayActivity.this,RechargeActivity.class);
							intent.putExtra("msg", ret);
							intent.putExtra("tradeStatus", intTradeStatus);
							intent.putExtra("tradeStatusString", strTradeStatus);
							intent.putExtra("isAlibabaReturn", true);
							
							String result = "";
							double d = 0;
							
							if(intTradeStatus == 9000)
							{
								result = objContent.getString("result");
								result = result.substring(1, result.length() - 1);
								JSONObject objResult = BaseHelper.string2JSON(result, "&");
								String strTotalFee = objResult.getString("total_fee");
								strTotalFee = strTotalFee.substring(1, strTotalFee.length() - 1);
								
								try
								{
									d = (Double.valueOf(strTotalFee));
								}
								catch (Exception e) 
								{
									d = 0;
								}
									
								
							}
							else
							{
								d = 0;
							}
							intent.putExtra("total_fee", d);
							startActivity(intent);
	
							AlipayActivity.this.finish();

						} 
						catch (Exception e) 
						{
							e.printStackTrace();
							BaseHelper.showDialog(AlipayActivity.this, "提示", ret,
									R.drawable.infoicon);
						}
					}
					break;
				 
				}

				super.handleMessage(msg);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	};
	
//	private BroadcastReceiver mPackageInstallationListener = new BroadcastReceiver() 
//	{
//		@Override
//		public void onReceive(Context context, Intent intent) 
//		{
//			String packageName = intent.getDataString();
//			if (!TextUtils
//					.equals(packageName, "package:com.alipay.android.app")) 
//			{
//				return;
//			}
//
//			if (mPosition != -1) 
//			{
//				performPay();
//				mPosition = -1;
//			}
//		}
//	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alipay);
		
		_payInfo = new PayInfo();
		Bundle extras = getIntent().getExtras();
		_payInfo.set_notifyActivity(extras.getInt("fromActivity"));
		_payInfo.set_title(extras.getString("title"));
		_payInfo.set_type(extras.getString("type"));
		_payInfo.set_price(extras.getDouble("price"));		
		
//		// 检测安全支付服务是否被安装
//		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
//		mspHelper.detectMobile_sp();
		
//		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
//		filter.addDataScheme("package");
//		registerReceiver(mPackageInstallationListener, filter);
		
		performPay();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			if(mProgress != null && mProgress.isShowing())
			{
				mProgress.dismiss();
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void performPay() 
	{
		// 检测安全支付服务是否被安装
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		mspHelper.detectMobile_sp();
		
		if(!checkInfo())
		{
			BaseHelper
			.showDialog(
					AlipayActivity.this,
					"提示",
					"缺少partner或者seller。",
					R.drawable.infoicon);
			return;
		}
		
		// 根据订单信息开始进行支付
		try 
		{
			// 准备订单信息
			String orderInfo = getOrderInfo(0);
			// 这里根据签名方式对订单信息进行签名
			String signType = getSignType();
			String strsign = sign(signType, orderInfo);
			Log.d("sign:", strsign);
			// 对签名进行编码
			strsign = URLEncoder.encode(strsign, "UTF-8");
			// 组装好参数
			String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&"
					+ getSignType();
			Log.d("orderInfo:", info);
			// 调用pay方法进行支付
			
			//Alipay a = new Alipay();
			
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, this);
			if (bRet) 
			{
				// show the progress bar to indicate that we have started
				// paying.
				// 显示“正在支付”进度条
				closeProgress();
				mProgress = BaseHelper.showProgress(this, null, "正在支付", false,
						true);
			} 
		}
		catch (Exception ex) 
		{
			Toast.makeText(AlipayActivity.this, R.string.remote_call_failed,
					Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * check some info.the partner,seller etc. 检测配置信息
	 * partnerid商户id，seller收款帐号不能为空
	 * 
	 * @return
	 */
	private boolean checkInfo() 
	{
		String partner = PartnerConfig.PARTNER;
		String seller = PartnerConfig.SELLER;
		if (partner == null || partner.length() <= 0 || seller == null
				|| seller.length() <= 0)
			return false;

		return true;
	}
	
	/**
	 * get the selected order info for pay. 获取商品订单信息
	 * 
	 * @param position
	 *            商品在列表中的位置
	 * @return
	 */
	String getOrderInfo(int position) 
	{
		String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		strOrderInfo += "&";
		strOrderInfo += "subject=" + "\"" + _payInfo.get_title()
				+ "\"";
		strOrderInfo += "&";
		strOrderInfo += "body=" + "\"" + _payInfo.get_type() + "\"";
		strOrderInfo += "&";
		strOrderInfo += "total_fee=" + "\""
				+ _payInfo.get_price() + "\"";
		strOrderInfo += "&";
		strOrderInfo += "notify_url=" + "\""
				+ "http://219.151.10.235:8023" + "\"";

		return strOrderInfo;
	}
	
	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 * @return
	 */
	String getOutTradeNo() 
	{
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String strKey = format.format(date);

		java.util.Random r = new java.util.Random();
		strKey = strKey + r.nextInt();
		strKey = strKey.substring(0, 15);
		return strKey;
	}
	
	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 * @return
	 */
	String getSignType()
	{
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}
	
	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param signType
	 *            签名方式
	 * @param content
	 *            待签名订单信息
	 * @return
	 */
	String sign(String signType, String content)
	{
		return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
	}
	
	//
	// close the progress bar
	// 关闭进度框
	void closeProgress() 
	{
		try 
		{
			if (mProgress != null) 
			{
				mProgress.dismiss();
				mProgress = null;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
