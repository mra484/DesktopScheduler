
public class DateEntry implements Comparable<DateEntry>{
	private int date;
	private int day;
	private String title;
	private String memo;
	private int repeat = 0;
	private int currentRep = 0;
	
	public DateEntry(int newDate, String newTitle, String newMemo){
		if(newTitle.compareTo("") == 0)
			newTitle = " ";
		if(newMemo.compareTo("") == 0)
			newMemo = " ";
		date = newDate;
		title = newTitle;
		memo = newMemo;
	}
	
	public DateEntry(int day, String newTitle, String newMemo, int interval){
		if(newTitle.compareTo("") == 0)
			newTitle = " ";
		if(newMemo.compareTo("") == 0)
			newMemo = " ";
		this.day = day;
		title = newTitle;
		memo = newMemo;
		repeat = interval;
		currentRep = interval;
	}
	
	public int getDate(){
		return date;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setMemo(String newTitle){
		title = newTitle;
	}
	
	public String getMemo(){
		return memo;
	}
	
	public String toString(){
		return title + "=" + memo;
	}

	@Override
	public int compareTo(DateEntry a) {
		
		return a.getDate() - date;
	}
}
