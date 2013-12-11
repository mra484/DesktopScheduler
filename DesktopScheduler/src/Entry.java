import java.util.HashSet;
public class Entry {
	private HashSet<String> data = new HashSet<String>();
	private byte date;
	private int format = 0;
	public static final int DMY = 0;
	public static final int DYM = 1;
	public static final int MDY = 2;
	public static final int MYD = 3;
	public static final int YMD = 4;
	public static final int YDM = 5;
	
	public Entry(String date){
		this.date = parseDate(date);
	}
	
	public void add(String info) {
		data.add(info);
	}
	
	public void remove(String info) {
		data.remove(info);
	}
	
	public byte parseDate(String dateString){
		String[] split;
		byte date = 0;
		dateString.toLowerCase();
		
		//try to determine separators and split up the date
		if( dateString.contains("-") )
			split = dateString.split("-");
		else if( dateString.contains("/") )
			split = dateString.split("/");
		else if( dateString.contains("\\") )
			split = dateString.split("\\");
		else
			split = dateString.split(" ");
		
		//convert the date into a value depending on the input format
		switch( format ){
		case DMY:
			date += Byte.parseByte(split[2]) << 8;
			date += parseMonth(split[1]);
			date += Byte.parseByte(split[0]);
			break;
		case DYM:
			date += Byte.parseByte(split[1]) << 8;
			date += parseMonth(split[2]);
			date += Byte.parseByte(split[0]);
			break;
		case MDY:
			date += Byte.parseByte(split[2]) << 8;
			date += parseMonth(split[0]);
			date += Byte.parseByte(split[1]);
			break;
		case MYD:
			date += Byte.parseByte(split[1]) << 8;
			date += parseMonth(split[0]);
			date += Byte.parseByte(split[2]);
			break;
		case YDM:
			date += Byte.parseByte(split[0]) << 8;
			date += parseMonth(split[2]);
			date += Byte.parseByte(split[1]);
			break;
		case YMD:
			date += Byte.parseByte(split[0]) << 8;
			date += parseMonth(split[1]);
			date += Byte.parseByte(split[2]);
			break;
		}
		
		return date;
	}
	
	private byte parseMonth(String month){
		
		month.toLowerCase().replace(" ", "").replace(",", "");
		
		if( month.contains("oc")  || month.contains("10") )
			return (byte) (9 << 5);
		else if( month.contains("no")   || month.contains("11") )
			return (byte) (10 << 5);
		else if( month.contains("de")   || month.contains("12") )
			return (byte) (12 << 5);
		else if( month.contains("ja")  || month.contains("1") ) 
			return 0;
		else if( month.contains("fe")  || month.contains("2") )
			return 1 << 5;
		else if( month.contains("mar")  || month.contains("3") )
			return (byte) (2 << 5);
		else if( month.contains("ap")   || month.contains("4") )
			return (byte) (3 << 5);
		else if( month.contains("ma")   || month.contains("5") )
			return (byte) (4 << 5);
		else if( month.contains("jun")   || month.contains("6") )
			return (byte) (5 << 5);
		else if( month.contains("jul")   || month.contains("7") )
			return (byte) (6 << 5);
		else if( month.contains("au")   || month.contains("8") )
			return (byte) (7 << 5);
		else if( month.contains("se")  || month.contains("9")  )
			return (byte) (8 << 5);
		
		return -1;
	}
}
