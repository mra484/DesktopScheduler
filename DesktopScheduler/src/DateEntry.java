
public class DateEntry implements Comparable<DateEntry>{
	private short date;
	private String title;
	private String memo;
	
	public DateEntry(short newDate, String newTitle, String newMemo){
		if(newTitle.compareTo("") == 0)
			newTitle = " ";
		if(newMemo.compareTo("") == 0)
			newMemo = " ";
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
