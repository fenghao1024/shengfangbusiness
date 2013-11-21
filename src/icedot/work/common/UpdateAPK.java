package icedot.work.common;

import icedot.work.shengfang.business.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateAPK {
	private Context _context;
	private String _apkUrl = "";
	private Dialog _noticeDialog;
	private Dialog _downloadDialog;
	private static final String _savePath = Environment.getExternalStorageDirectory().getPath() + "/Download/";
	private String _saveFileName = null;
	private ProgressBar _progressBar;
	private TextView _progressText;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private int _progress;
	private Thread _downLoadThread;
	private boolean _interceptFlag = false;
	
	private String _updateMsg = "该软件有最新版本发布,请快下载吧";
	private String _updateTitle = "软件更新";
	
	private Handler _handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case DOWN_UPDATE:
				_progressBar.setProgress(_progress);
				_progressText.setText(_progress+"% "+ _progress + "/100");
				break;
			case DOWN_OVER:
				_downloadDialog.dismiss();
				installApk();
				//uninstallApk();
				break;
			default:
				break;
			}
		}
	};
	
	private Runnable downApkRunnable = new Runnable() 
	{
		public void run() 
		{
			try 
			{			
				URL url = new URL(_apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(_savePath);
				if (!file.exists()) {
					file.mkdir();
				}

				File ApkFile = new File(_savePath+_saveFileName);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					_progress = (int) (((float) count / length) * 100);
					
					// 更新进度
					_handler.sendEmptyMessage(DOWN_UPDATE);							
					fos.write(buf, 0, numread);
					Log.d("UpdateApplication","更新进度" + _progress + "%");
					if (length == count) 
					{
						// 下载完成通知安装
						_handler.sendEmptyMessage(DOWN_OVER);
						break;
					}
				} while (!_interceptFlag); // 点击取消就停止下载
				
				Log.d("UpdateApplication","下载完成");
				
				fos.close();
				is.close();
			}
			catch (MalformedURLException e) 
			{
				Log.d("UpdateApplication", e.toString());
			} 
			catch (IOException e) 
			{
				Log.d("UpdateApplication", e.toString());
			}
		}
	};
	
	public UpdateAPK(Context context)
	{
		_context = context;
	}
	
	public String getApkUrl() 
	{
		return _apkUrl;
	}

	public void setApkUrl(String apkUrl)
	{
		this._apkUrl = apkUrl;
	}
	
	public String get_updateMsg() {
		return _updateMsg;
	}

	public void set_updateMsg(String _updateMsg) {
		this._updateMsg = _updateMsg;
	}

	public String get_updateTitle() {
		return _updateTitle;
	}

	public void set_updateTitle(String _updateTitle) {
		this._updateTitle = _updateTitle;
	}

	public String get_saveFileName() {
		return _saveFileName;
	}

	public void set_saveFileName(String _saveFileName) {
		this._saveFileName = _saveFileName;
	}

	// 外部接口让主Activity调用
	public void checkUpdateInfo() 
	{
		showNoticeDialog();
	}
	
	private void showNoticeDialog() 
	{
		Builder builder = new Builder(_context);
		builder.setTitle(_updateTitle);
		builder.setMessage(_updateMsg);

		builder.setPositiveButton("下载", new OnClickListener() 
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
				showDownloadDialog();
			}
		});

		builder.setNegativeButton("以后再说", new OnClickListener() 
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});

		_noticeDialog = builder.create();
		_noticeDialog.show();
	}
	
	private void showDownloadDialog() 
	{
		Builder builder = new Builder(_context);
		builder.setTitle("软件下载");

		final LayoutInflater inflater = LayoutInflater.from(_context);
		View v = inflater.inflate(R.layout.common_updateapk_progressdlg, null);
		_progressBar = (ProgressBar) v.findViewById(R.id.updateapk_progress);
		_progressText = (TextView) v.findViewById(R.id.updateapk_text);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() 
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
				_interceptFlag = true;
			}
		});

		_downloadDialog = builder.create();
		_downloadDialog.show();

		downloadApk();
	}
	
	private void downloadApk()
	{
		_downLoadThread = new Thread(downApkRunnable);
		_downLoadThread.start();
		
	}
	
	private void installApk()
	{
		File apkFile = new File(_savePath+_saveFileName);
		if(!apkFile.exists())
		{
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://"+ apkFile.toString()), "application/vnd.android.package-archive");
		_context.startActivity(i);
	}
}
