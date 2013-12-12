import java.util.HashSet;
public class Entry {
	private HashSet<String> data = new HashSet<String>();
	private String[] dateList = {"January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December"};	
	private short date;
	private String dateName = "";
	public static int format = 0;
	public static final int DMY = 0;
	public static final int DYM = 1;
	public static final int MDY = 2;
	public static final int MYD = 3;
	public static final int YMD = 4;
	public static final int YDM = 5;
	
	public Entry(String date){
		this.date = parseDate(date, 0);
	}
	
	public boolean add(String info) {
		return data.add(info);
	}
	
	public boolean remove(String info) {
		return data.remove(info);
	}
	
	public short parseDate(String dateString, int option){
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
			temp = (short) Integer.parseInt(split[0]);
			if( option == 0)
				dateName = dateName + (int) temp;
			date += temp;

			if( option == 0)
				dateName = dateName + " ";
			date += parseMonth(split[1], option);
			temp = (short) Integer.parseInt(split[2]);
			if( option == 0)
				dateName = dateName + ", " + (int) temp;
			date += temp << 8;
			break;
		case DYM:
			temp = Byte.parseByte(split[0]);
			if( option == 0)
				dateName = dateName + (int) temp;
			date += temp;
			temp = Byte.parseByte(split[1]);
			if( option == 0)
				dateName = dateName + " " + (int) temp;
			date += temp << 8;
			if( option == 0)
				dateName = dateName + " ";
			date += parseMonth(split[2], option);
			break;
		case MDY:
			date += parseMonth(split[0], option);
			temp = Byte.parseByte(split[1]);
			if( option == 0)
				dateName = dateName + " " + (int) temp;
			date += temp;
			temp = Byte.parseByte(split[2]);
			if( option == 0)
				dateName = dateName +  ", " + (int) temp;
			date += temp << 8;
			break;
		case MYD:
			date += parseMonth(split[0], option);
			temp = Byte.parseByte(split[1]);
			if( option == 0)
				dateName = dateName +  ", " + (int) temp;
			date += temp << 8;
			temp += Byte.parseByte(split[2]);
			if( option == 0)
				dateName = dateName +  " " + (int) temp;
			date += temp;
			break;
		case YDM:
			temp = Byte.parseByte(split[0]);
			if( option == 0)
				dateName = dateName + (int) temp + ",";
			date += temp << 8;
			temp = Byte.parseByte(split[1]);
			if( option == 0)
				dateName = dateName + " " + (int) temp;
			date += temp;
			if( option == 0)
				dateName = dateName + " ";
			date += parseMonth(split[2], option);
			break;
		case YMD:
			temp = Byte.parseByte(split[0]);
			if( option == 0)
				dateName = dateName + ((int) temp) + ",";
			date += temp << 8;
			if( option == 0)
				dateName = dateName + " ";
			date += parseMonth(split[1], option);
			temp = Byte.parseByte(split[2]);
			if( option == 0)
				dateName = dateName +  " " + (int) temp;
			date += temp;
			break;
		}
		
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
		
		if( option == 0 )
			dateName = dateName + dateList[monthValue];
		monthValue *= 16;
		return monthValue;
	}

	public short getDate(){
		return date;
	}
	
	public String getName(){
		return dateName;
	}
	
	public HashSet<String> getList(){
		return data;
	}
}
