/**Desktop Schedule
 * Copyright(C) 2014 Mark Andrews
 * 
 *   Desktop Scheduler is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Desktop Scheduler is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 * Class for reading and writing to file
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;


public class FileHandler {
	public static final String SEPARATOR = "\ufffe";
	public static final String LINEBREAK = "\uffff";
	private FileInputStream iss;
	private InputStreamReader isr;
	private BufferedReader reader;
	private FileOutputStream ofs;
	private OutputStreamWriter osw;
	private BufferedWriter writer;
	private MainWindow main;
	private Lister list;
	private String oldSeparator = "=";
	
	public FileHandler(Lister a, MainWindow b){
		main = b;
		list = a;
		if(openFileReader("data.txt"))
			readData();
		else
			System.out.println("no file read");		
	}
	
	//open file for reading in UTF-8 format
	private boolean openFileReader(String filename){

		try {
			iss = new FileInputStream(filename);
			isr = new InputStreamReader(iss, "UTF-8");
			reader = new BufferedReader(isr);
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open data.txt for reading");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error creating input stream reader");
		}
		return false;
	}
	
	//open file for writing in UTF-8 format
	private boolean openFileWriter(String filename){

		try {
			ofs = new FileOutputStream(filename);
			osw = new OutputStreamWriter(ofs, "UTF-8");
			writer = new BufferedWriter(osw);
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open data.txt for writing");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error creating output stream writer");
		}
		return false;
	}
	
	//read data from file
	private void readData(){
		String[] split;
		String input;

		try {
			while(reader.ready()){
				input = reader.readLine();
				split = input.split(SEPARATOR);
				
				/***************for handling old versions of data files**************/
				if(split.length == 1)
				{
					//try using old separator =
					split = split[0].split(oldSeparator);
					
					//read format if still no separator found
					if(split.length == 1)
					{
						Entry.format = Integer.parseInt(split[0]);
						break;
					}
				}
				
				//read font sizes
				if(split[0].compareTo("Font_Sizes") == 0 ){
					MainWindow.dateSize = Integer.parseInt(split[1]);
					MainWindow.titleSize = Integer.parseInt(split[2]);
				}
				
				//read date format
				else if(split[0].compareTo("Date_Format") == 0 )
					Entry.format = Integer.parseInt(split[1]);
				
				//read diplay preference
				else if( split[0].compareTo("Empty_Dates") == 0)
					MainWindow.emptyDates = Boolean.parseBoolean(split[1]);
				
				//read whether or not to save window position
				else if( split[0].compareTo("Position_Pref") == 0) {
					MainWindow.positionPref = Integer.parseInt(split[1]);
					
					input = reader.readLine();
					split  = input.split(SEPARATOR);
					
					/******************** handle old file separator type ***********************/
					if(split.length == 1)
						split = split[0].split(oldSeparator);
					
					if(MainWindow.positionPref == MainWindow.CUSTOM_WINDOW){
					main.setBounds(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]),
							Integer.parseInt(split[4]));
					}
					
				//read delete dialog preference
				}else if( split[0].compareTo("Confirm_Delete") == 0 )
					MainWindow.deleteDialog = Boolean.parseBoolean(split[1]);
				
				//add event to list
				else if( split.length == 3)
				{
					System.out.println("Contains " + LINEBREAK + " " + split[2].contains(LINEBREAK));
					list.add(split[0], split[1], split[2].replace(LINEBREAK, "\r\n"));
				}
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Error reading from file");
		} catch(NumberFormatException e) {
			System.out.println("Error reading format integer");			
		}
	}
	
	public void saveData(){
		openFileWriter("data.txt");
		writeData();
	}
	//write data to file
	private void writeData(){
		try {
			writer.write(String.format("%s%s%s", "Date_Format", SEPARATOR, Entry.format));
			writer.newLine();

			writer.write(String.format("%s%s%s", "Empty_Dates", SEPARATOR, MainWindow.emptyDates));
			writer.newLine();
			
			writer.write(String.format("%s%s%s%s%s", "Font_Sizes", SEPARATOR, MainWindow.dateSize, SEPARATOR,
					MainWindow.titleSize));
			writer.newLine();
			
			writer.write(String.format("%s%s%s", "Confirm_Delete", SEPARATOR, MainWindow.deleteDialog));
			writer.newLine();
			
			writer.write(String.format("%s%s%s", "Position_Pref", SEPARATOR, MainWindow.positionPref));
			writer.newLine();
			
			writer.write(String.format("%s%s%s%s%s%s%s%s%s", "WindowXYWH", SEPARATOR, main.getX(), SEPARATOR,
					main.getY(), SEPARATOR, main.getWidth(), SEPARATOR, main.getHeight()));
			writer.newLine();
			
			for(Entry a: list.getList().values()){
				for(DateEntry b: a.getList().values()){
					writer.write(String.format("%s%s%s", a.getName(), SEPARATOR, b.toOut()));
					writer.newLine();
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.out.println("Error writing to file");
		}

	}

}
