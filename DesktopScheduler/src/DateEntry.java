
public class DateEntry implements Comparable<DateEntry>{
	private short date;
	private String title;
	private String memo;
	
	public DateEntry(short newDate, String newTitle, String newMemo){
		date = newDate;
		title = newTitle;
		memo = newMemo;
	}
	
	public short getDate(){
		return date;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setMemo(String newTitle){
		title = newTitle;
	}
	
	public String toString(){
		return title + "=" + memo;
	}

	@Override
	public int compareTo(DateEntry a) {
		
		return a.getDate() - date;
	}
}
