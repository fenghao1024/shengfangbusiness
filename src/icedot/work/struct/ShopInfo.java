package icedot.work.struct;

public class ShopInfo
{
	private int _ID;			//商铺ID
	private String _name;		//商铺名称
	private String _address;	//地址
	private String _phone;		//电话
	private String _area;		//商圈
	private int _areaType;		//商圈类型
	private int _check;			//审核状态
	private String _description;//描述
	
	public ShopInfo()
	{
		_ID = 0;
		_name = "";
		_address = "";
		_phone = "";
		_area = "";
		_description = "";
		_areaType = 1;
	}
	
	public String get_description() {
		return _description;
	}
	public void set_description(String _description) {
		this._description = _description;
	}
	public void set_area(String _area) {
		this._area = _area;
	}
	public String get_area() {
		return _area;
	}
	public int get_ID()
	{
		return _ID;
	}
	public void set_ID(int _ID)
	{
		this._ID = _ID;
	}
	public String get_name() 
	{
		return _name;
	}
	public void set_name(String _name) 
	{
		this._name = _name;
	}
	public String get_address() 
	{
		return _address;
	}
	public void set_address(String _address)
	{
		this._address = _address;
	}
	public String get_phone()
	{
		return _phone;
	}
	public void set_phone(String _phone)
	{
		this._phone = _phone;
	}

	public int get_check() {
		return _check;
	}

	public void set_check(int _check) {
		this._check = _check;
	}	
	
	public int get_areaType() {
		return _areaType;
	}

	public void set_areaType(int _areaType) {
		this._areaType = _areaType;
	}

	public String getCheckString()
	{
		switch(_check)
		{
		case 0:
			return "未审核";
		case 1:
			return "审核通过";
		case 2:
			return "审核未通过";
		default:
			return "未定义";
		}
	}
}
