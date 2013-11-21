package icedot.work.struct;


public class UserInfo {
	private String   _ID;		//用户ID
	private String   _account;	//帐号
	private String   _pwd;
	private String   _name;		//姓名
	private String   _sex;		//性别
	private String   _address;	//地址
	private String   _phone;	//电话
	private double   _money;	//余额
	private int      _count;	//剩余条数
	
	public UserInfo()
	{
		_ID = "";
		_name = "";
		_sex = "男";
		_address = "";
		_phone = "";
		_count = 0;
		_account = "";
		_money=0.0f;
	}
	
	public void clear()
	{
		_ID = "";
		_name = "";
		_sex = "男";
		_address = "";
		_phone = "";
		_count = 0;
		_account = "";
		_money=0.0f;
	}
	
	public String get_account() {
		return _account;
	}

	public void set_account(String _account) {
		this._account = _account;
	}
	
	public String get_pwd() {
		return _pwd;
	}

	public void set_pwd(String _pwd) {
		this._pwd = _pwd;
	}

	public double get_money() {
		return _money;
	}

	public void set_money(double _money) {
		this._money = _money;
	}

	public int get_count() {
		return _count;
	}

	public void set_count(int _count) {
		this._count = _count;
	}

	public String get_ID() {
		return _ID;
	}

	public void set_ID(String _ID) {
		this._ID = _ID;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_sex() {
		return _sex;
	}

	public void set_sex(String _sex) {
		this._sex = _sex;
	}

	public String get_address() {
		return _address;
	}

	public void set_address(String _address) {
		this._address = _address;
	}

	public String get_phone() {
		return _phone;
	}

	public void set_phone(String _phone) {
		this._phone = _phone;
	}
	
	
}
