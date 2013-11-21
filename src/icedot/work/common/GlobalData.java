package icedot.work.common;

import icedot.work.shengfang.business.R;

import java.util.Hashtable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class GlobalData {
	public static void printLog(String tag,String content)
	{
		Log.i(tag, content);
	}
	/**
	 * dip------>px
	 * @param context
	 * @param dipvalue
	 * @return
	 */
	public static int dipTopix(Context context , float dipvalue)
	{
		float scale=context.getResources().getDisplayMetrics().density;
		return (int)(dipvalue*scale+0.5f);
	}
	
	/**
	 * px------>dip
	 * @param context
	 * @param dipvalue
	 * @return
	 */
	public static int pixTodip(Context context,float pixvalue)
	{
		float scale=context.getResources().getDisplayMetrics().density;
		return (int)(pixvalue/scale+0.5f);
	}
	
	/**
	 * 获取当前程序的版本号
	 * @param context
	 * @return
	 */
	public static int getCurrentVersionCode(Context context) {
		int version = -1;
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			version = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}
	
	/**
	 * 获取当前程序的版本号 (VersionName ，是版本名，string类型，不是版本号)
	 * @param context
	 * @return
	 */
	public static String getCurrentVersionName(Context context) {
		String version = null;
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}
	

	/**
	 * 二维码生成
	 * @return
	 */
	public static  Bitmap getDimemsionCode(String preData,int QR_WIDTH,int QR_HEIGHT)
	{
		Bitmap bitmap=null;
		 try {
	            // 需要引入core包
	            //QRCodeWriter writer = new QRCodeWriter();
	            GlobalData.printLog("yhy", "生成的文本：" + preData);
	            if (preData == null || "".equals(preData) || preData.length() < 1) 
	            {
	                return null;
	            }
	            // 把输入的文本转为二维码
//	            BitMatrix martix = writer.encode(preData, BarcodeFormat.QR_CODE,
//	                    QR_WIDTH, QR_HEIGHT);
	            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
	            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	            BitMatrix bitMatrix = new QRCodeWriter().encode(preData,
	                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
	            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
	            for (int y = 0; y < QR_HEIGHT; y++) {
	                for (int x = 0; x < QR_WIDTH; x++) {
	                    if (bitMatrix.get(x, y)) {
	                        pixels[y * QR_WIDTH + x] = 0xff7A576F;
	                    } else {
	                        pixels[y * QR_WIDTH + x] = 0xFFFFFFFF;
	                    }

	                }
	            }
	            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,Bitmap.Config.ARGB_8888);
	            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
	        } catch (WriterException e) {
	            e.printStackTrace();
	        }
		return bitmap;
	}

	/**
	 * 获取自定义Toast
	 * @param context
	 * @param warningDrawable
	 * @param tips
	 * @param duration
	 * @return
	 */
	public static Toast getCustomToast(Context context,Drawable warningDrawable,String tips,int duration)
	{
		Toast toast=new Toast(context);
		toast.setGravity(Gravity.TOP, 0, dipTopix(context, 50));
		toast.setDuration(duration);
		View view=LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
		ImageView l_ivHead=(ImageView) view.findViewById(R.id.custom_toast_image);
		TextView l_tvTips=(TextView) view.findViewById(R.id.custom_toast_dialogText);
		l_ivHead.setImageDrawable(warningDrawable);
		l_tvTips.setText(tips);
		toast.setView(view);
		return toast;
	}
	
	public static AlertDialog exitNoticeDialog(final Activity context)
	{
		Builder builder = new Builder(context);
		builder.setTitle("退出");
		builder.setMessage("确认要退出黔商通吗?");
		AlertDialog ret;
		
		builder.setPositiveButton("确定", new OnClickListener() 
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
				context.finish();
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener() 
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});

		ret = builder.create();
		ret.show();
		return ret;
	}
}
