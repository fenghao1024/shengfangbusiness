package icedot.work.struct;

public class HolidayInfo {
	private int _year;
	private int _month;
	private int _day;
	
	public int get_year() {
		return _year;
	}
	public void set_year(int _year) {
		this._year = _year;
	}
	public int get_month() {
		return _month;
	}
	public void set_month(int _month) {
		this._month = _month;
	}
	public int get_day() {
		return _day;
	}
	public void set_day(int _day) {
		this._day = _day;
	}
	public boolean checkDate(int year,int month,int day)
	{
		if(this._year == year && this._month == month &&
				this._day == day)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
