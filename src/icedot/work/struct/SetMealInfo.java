package icedot.work.struct;

public class SetMealInfo 
{
	private int 	_ID;			
	private String _type;		//套餐类型
	private double _price;		//价格
	private int _count;			//套餐次数
	private String _description;	//说明
	
	public SetMealInfo(SetMealInfo sm)
	{
		if(sm == null)
			return;
		this._ID = sm.get_ID();
		this._type = sm.get_type();
		this._price = sm.get_price();
		this._description = sm.get_description();
		this._count = sm.get_count();
	}
	
	public SetMealInfo()
	{
		this._ID = 0;
		this._type = "";
		this._price = 0;
		this._description = "";
		this._count = 0;
	}
	
	public int get_ID() {
		return _ID;
	}
	public void set_ID(int _ID) {
		this._ID = _ID;
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
	public int get_count() {
		return _count;
	}
	public void set_count(int _count) {
		this._count = _count;
	}
	public String get_description() {
		return _description;
	}
	public void set_description(String _description) {
		this._description = _description;
	}
	
}
