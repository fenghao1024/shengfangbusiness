package icedot.work.struct;

public class PayInfo 
{
	private int _notifyActivity;
	private String _title;
	private String _type;		//套餐类型
	private double _price;		//价格
	public int get_notifyActivity() {
		return _notifyActivity;
	}
	public void set_notifyActivity(int _notifyActivity) {
		this._notifyActivity = _notifyActivity;
	}
	
	public String get_title() {
		return _title;
	}
	public void set_title(String _title) {
		this._title = _title;
	}
	public String get_type() {
		return _type;
	}
	public void set_type(String _type) {
		this._type = _type;
	}
	public double get_price() {
		return _price;
	}
	public void set_price(double _price) {
		this._price = _price;
	}
}
