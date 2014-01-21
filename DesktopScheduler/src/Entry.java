import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
public class Entry {
	
	//data will be stored using the event title as a key for the the dataentry object
	private HashMap<String, DateEntry> data = new HashMap<String, DateEntry>();
	private String[] dateList = {"January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December"};	
	private short date;
	private String dateName = "";
	private String month = "";
	private short day = 0;
	private short year = 0;
	public static int format = 0;
	public static final int DMY = 0;
	public static final int DYM = 1;
	public static final int MDY = 2;
	public static final int MYD = 3;
	public static final int YMD = 4;
	public static final int YDM = 5;
	public static Calendar today = Calendar.getInstance();
	
	public Entry(String date){
		this.date = parseDate(date, 0);
	}
	
	public void add(String title, String memo) {
		data.put(title, new DateEntry(date, title, memo));
	}
	
	public void remove(String info) {
		data.remove(info);
	}
	
	public short parseDate(String dateString, int option){
		//option = 0 for creating a new entry, includes date name as a string
		//option = 1 for only calculating the (short) value of a date
		
		String[] split;
		short date = 0;
		short temp = 0;
		dateString.toLowerCase();
		
		//try to determine separators and split up the date
		if( dateString.contains("-") )
			split = dateString.split("-");
		else if( dateString.contains("/") )
			split = dateString.split("/");
		else if( dateString.contains("\\") )
			split = dateString.split("\\");
		else if( dateString.contains("||"))
			split = dateString.split("||");
		else
			split = dateString.split(" ");
		
		//convert the date into a value depending on the input format
		switch( Entry.format ){
		case DMY:
			//day
			temp = (short) Integer.parseInt(split[0]);
			day = temp;
			date += temp;
			
			//month
			date += parseMonth(split[1], option);
			
			//year
			temp = (short) Integer.parseInt(split[2]);
			if( temp < 1000)
				temp += 2000;
			year = temp;
			date += temp << 8;
			break;
		case DYM:
			//day
			temp = Byte.parseByte(split[0]);
			day = temp;
			date += temp;
			
			//year
			temp = Byte.parseByte(split[1]);
			if( temp < 1000)
				temp += 2000;
			year = temp;
			date += temp << 8;
			
			//month
			date += parseMonth(split[2], option);
			break;
		case MDY:
			//month
			date += parseMonth(split[0], option);
			
			//day
			temp = Byte.parseByte(split[1]);
			day = temp;
			date += temp;
			
			//year
			temp = Byte.parseByte(split[2]);
			if( temp < 1000)
				temp += 2000;
			year = temp;
			date += temp << 8;
			break;
		case MYD:
			//month
			date += parseMonth(split[0], option);
			
			//year
			temp = Byte.parseByte(split[1]);
			if( temp < 1000)
				temp += 2000;
			year = temp;
			date += temp << 8;
			
			//day
			temp += Byte.parseByte(split[2]);
			date += temp;
			day = temp;
			break;
		case YDM:
			//year
			temp = Byte.parseByte(split[0]);
			if( temp < 1000)
				temp += 2000;
			year = temp;
			date += temp << 8;
			
			//day
			temp = Byte.parseByte(split[1]);
			day = temp;
			date += temp;
			
			//month
			date += parseMonth(split[2], option);
			break;
		case YMD:
			//year
			temp = Byte.parseByte(split[0]);
			if( temp < 1000)
				temp += 2000;
			year = temp;
			date += temp << 8;
			
			//month
			date += parseMonth(split[1], option);
			
			//day
			temp = Byte.parseByte(split[2]);
			day = temp;
			date += temp;
			break;
		}
		makeName();
		return date;
	}
	
	private short parseMonth(String month, int option){
		byte monthValue = -1;
		month = month.toLowerCase().replace(" ", "").replace(",", "");
		
		if( month.contains("oc")  || month.contains("10") )
			monthValue = 9;
		else if( month.contains("no")   || month.contains("11") )
			monthValue = 10;
		else if( month.contains("de")   || month.contains("12") )
			monthValue = 11;
		else if( month.contains("ja")  || month.contains("1") ) 
			monthValue = 0;
		else if( month.contains("fe")  || month.contains("2") )
			monthValue = 1;
		else if( month.contains("mar")  || month.contains("3") )
			monthValue = 2;
		else if( month.contains("ap")   || month.contains("4") )
			monthValue = 3;
		else if( month.contains("ma")   || month.contains("5") )
			monthValue = 4;
		else if( month.contains("jun")   || month.contains("6") )
			monthValue = 5;
		else if( month.contains("jul")   || month.contains("7") )
			monthValue = 6;
		else if( month.contains("au")   || month.contains("8") )
			monthValue = 7;
		else if( month.contains("se")  || month.contains("9")  )
			monthValue = 8;
		
//		if( option == 0 )
//			dateName = dateName + dateList[monthValue];
		this.month = dateList[monthValue];
		monthValue *= 16;
		return monthValue;
	}
	
	public void makeName(){
		switch (format){ 
		case DMY:
			dateName = String.format(day +" " + month + ", " + year);
			break;
		case DYM:
			dateName = String.format(day +" " + year + ", " + month);
			break;
		case MDY:
			dateName = String.format(month +" " + day + ", " + year);
			break;
		case MYD:
			dateName = String.format(month +" " + year + ", " + day);
			break;
		case YMD:
			dateName = String.format(year +" " + ", " + month + day);
			break;
		case YDM:
			dateName = String.format(year +" " + ", " + day + month);
			break;
			default:
				break;
				
		}
	}

	public short getDate(){
		return date;
	}
	
	public String getName(){
		return dateName;
	}
	
	public HashMap<String, DateEntry> getList(){
		return data;
	}
	public void setToday(){
		
	}
}
