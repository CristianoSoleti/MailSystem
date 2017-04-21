
public class Date {

	private int year;
	private int month;
	private int day;
	private Date date;
	
	public Date(int year,int month,int day)
	{
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	public String getDate(){
		
		return year + "/" + month + "/" + day + "";
		
	}
}
