import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class FileGenerator {

	public static int uniqueIDs = 20;
	public static int dataPoints = 1000; 
	
	public static void main(String[] args){
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("BajaTest.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> info = generateData();
		
		for(int i=0;i<info.size();i++)
			writer.println(info.get(i));
		
		writer.close();
	}
	
	
	public static ArrayList<String> generateData(){
		
		ArrayList<String> toReturn = new ArrayList<String>(dataPoints);
		
		for(int i=0;i<dataPoints;i++){
			
			int curID = i % uniqueIDs;
			String toAdd = curID + ":" + System.currentTimeMillis() + ":"+ curID+i;
			toReturn.add(toAdd);
		}
		
		return toReturn;
		
	}
}
