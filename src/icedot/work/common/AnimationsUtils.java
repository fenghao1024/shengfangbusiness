package icedot.work.common;

import icedot.work.shengfang.business.R;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimationsUtils 
{
	private Context _context;
	private Animation _pushDownOutAnim;
	private Animation _pushUpInAnim;
	private Animation _ZoomEnterAnim;
	private Animation _WaveScaleAnim;
	private Animation _ButtonTipInAnim;
	private Animation _LoginingAnimation;
	
	public AnimationsUtils(Context context)
	{
		this._context=context;
		initAmin();
	}
	
	private void initAmin() 
	{
		_pushDownOutAnim=AnimationUtils.loadAnimation(_context, R.anim.push_down_out);
		_pushUpInAnim=AnimationUtils.loadAnimation(_context, R.anim.push_up_in);
		_ZoomEnterAnim=AnimationUtils.loadAnimation(_context, R.anim.zoom_enter);
		_WaveScaleAnim=AnimationUtils.loadAnimation(_context, R.anim.wave_scale);
		_ButtonTipInAnim=AnimationUtils.loadAnimation(_context, R.anim.buttomtip_in);
		_LoginingAnimation=AnimationUtils.loadAnimation(_context, R.anim.loading);
	}

	/**
	 * @return
	 */
	public  Animation getPushDownOutAnim()
	{
		_pushDownOutAnim.setFillAfter(true);
		return _pushDownOutAnim;
	}
	
	/**
	 * @return
	 */
	public  Animation getPushUpInAnim()
	{
		return _pushUpInAnim;
	}
	
	/**
	 * @return
	 */
	public  Animation getZoomEnterAnim()
	{
		return _ZoomEnterAnim;
	}
	/**
	 * @return
	 */
	public  Animation getWaveScaleAnim()
	{
		return _WaveScaleAnim;
	}
	
	public Animation getButtonTipInAnim()
	{
		return _ButtonTipInAnim;
	}
	
	public Animation getLogingAnim()
	{
		return _LoginingAnimation;
	}
}
