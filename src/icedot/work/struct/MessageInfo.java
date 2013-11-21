package icedot.work.struct;

public class MessageInfo 
{
	private String _title;			//标题
	private String _content;		//内容
	private String _startTime;		//开始日期
	private String _endTime;		//结束日期
	private String _shopID; 		//商铺ID
	private String _shopName;		//商铺名称
	private String _count;			//消息的条数
	private int _sendState;			//发送状态
	private String _mealType;		//套餐类型
	private double _price;			//套餐价格
	
	public MessageInfo()
	{
		_title = "";
		_content = "";
		_startTime = "";
		_endTime = "";
		_shopID = "";
		_shopName = "";
		_count = "0";	
		_mealType = "";
	}
	
	public String get_title() {
		return _title;
	}
	public void set_title(String _title) {
		this._title = _title;
	}
	public String get_content() {
		return _content;
	}
	public void set_content(String _content) {
		this._content = _content;
	}
	public String get_startTime() {
		return _startTime;
	}
	public void set_startTime(String _startTime) {
		this._startTime = _startTime;
	}
	public String get_endTime() {
		return _endTime;
	}
	public void set_endTime(String _endTime) {
		this._endTime = _endTime;
	}
	public String get_shopID() {
		return _shopID;
	}
	public void set_shopID(String _shopID) {
		this._shopID = _shopID;
	}
	public String get_shopName() {
		return _shopName;
	}
	public void set_shopName(String _shopName) {
		this._shopName = _shopName;
	}
	public String get_count() {
		return _count;
	}
	public void set_count(String count) {
		this._count = count;
	}

	public int get_sendState() {
		return _sendState;
	}

	public void set_sendState(int _sendState) {
		this._sendState = _sendState;
	}
	
	public String get_mealType() {
		return _mealType;
	}

	public void set_mealType(String _mealType) {
		this._mealType = _mealType;
	}
	

	public double get_price() {
		return _price;
	}

	public void set_price(double _price) {
		this._price = _price;
	}

	public String getSendStateString()
	{
		switch(_sendState)
		{
		case 0:
			return "未发送";
		case 1:
			return "发送中";
		case 2:
			return "发送完成";
		default:
			return "未定义";
		}
	}
	
	public void clear()
	{
		_title = "";
		_content = "";
		_startTime = "";
		_endTime = "";
		_shopID = "";
		_shopName = "";
		_count = "0";		
	}
}
