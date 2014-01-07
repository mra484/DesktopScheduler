
public class DateEntry implements Comparable<DateEntry>{
	private short date;
	private String title;
	
	public DateEntry(short newDate, String newTitle){
		date = newDate;
		title = newTitle;
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
		return getTitle();
	}

	@Override
	public int compareTo(DateEntry a) {
		
		return date - a.getDate();
	}
}
