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
	private FileInputStream iss;
	private InputStreamReader isr;
	private BufferedReader reader;
	private FileOutputStream ofs;
	private OutputStreamWriter osw;
	private BufferedWriter writer;
	private Lister list;
	private String separator = "=";
	
	public FileHandler(Lister a){
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
			input = reader.readLine();
			Entry.format = Integer.parseInt(input);
			while(reader.ready()){
				input = reader.readLine();
				split = input.split(separator);
				list.add(split[0], split[1]);
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Error reading from file");
		}
	}
	
	public void saveData(){
		openFileWriter("data.txt");
		writeData();
	}
	//write data to file
	private void writeData(){
		try {
			writer.write("" + Entry.format);
			writer.newLine();
			for(Entry a: list.getList().values()){
				for(DateEntry b: a.getList().values()){
					writer.write(String.format("%s%s%s", a.getName(), separator, b));
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
