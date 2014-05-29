import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class FileSeparator {

	public static Map<String, String> nameMap;	//Maps from ID to Human Name
	public static Map<String, String[] > configMap; //Maps from ID to rest of Config (Offset,Scalar,Units,HumanName)
	public static int startTime=0;
	public static final String xbowID = "1111";
	public static final String dataFileSeparator = ":";
	public static final String configFileSeparator = "#";
	
	//@display = words to be displayed
	//Selects a file and returns it; 

	public static File selectFile(String display){
		
		JFileChooser fc = new JFileChooser();
		
		int result = fc.showDialog(null, display);
		if(result == JFileChooser.APPROVE_OPTION){
			
			return fc.getSelectedFile();
		}
		else
		{
			return null;
		}
		
	}

	/* ID # Sample Rate # Offset # Scalar # Units # Human Name
	 *
	 *
	 * ID - Mapping
	 */
	//Creates an ID-Name mapping based on File Name
	//If ID is not mapped to File Name, ID becomes file name
	public static void generateMap(File key){
		
		nameMap = new TreeMap<String, String>();
		
		if(key == null)
			errorCall(11);
		
		try{
			
			BufferedReader in = new BufferedReader(new FileReader(key));
			String line = null;
			
			//get all files
			while((line = in.readLine()) != null){
				
				String[] lines = line.split(configFileSeparator);
				
				if(lines.length <=1){
					errorCall(2);
				}else{
					
					//if name exists, or not
					if(lines.length == 7){
						nameMap.put(lines[0].trim(), lines[5].trim());
					}
					else{
						nameMap.put(lines[0].trim(), lines[0].trim());
					}
					
				}	
			}
			
		
		}catch(IOException e){
			
			errorCall(11);
		}
	}
	
	//Creates an ID Map for rest of the configurations
	public static void generateConfig(File key){
		
		configMap = new TreeMap<String, String[]>();
		
		if(key == null)
			errorCall(11);
		
		try{
			
			
			BufferedReader in = new BufferedReader(new FileReader(key));
			String line = null;
			
			//get all files
			while((line = in.readLine()) != null){
				
				String[] lines = line.split(configFileSeparator);
				
				
				if(lines.length <= 1 || lines.length >6 ){
					errorCall(2);
				}else{
					
					String[] config = Arrays.copyOfRange(lines, 1, 5);
					configMap.put(lines[0], config);
				}	
			}
			
		
		}catch(IOException e){
			
			errorCall(11);
		}
		
	}

	//Call this only after generateMap and generateConfig has been called
	public static void generateFiles(File data){
		
		Map<String, File> filesMap = new TreeMap<String, File>();
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(data));
			String line = null;
			line = reader.readLine();
			startTime = Integer.parseInt(line.trim());

			// get all files
			while ((line = reader.readLine()) != null) {

				String[] lines = line.split(dataFileSeparator);

				// If data file has erroneous input
				if (lines.length >= 3 || lines.length == 0) {
					errorCall(4);
				}

				String ID = lines[0];	// Line-Data ID
				String time = lines[1]; // Line-Data Time
				String info = lines[2];	// Line-Data Information
				
				//special xbow case
				if(ID==xbowID){
					
					File write; 
					String name = "xbowData.txt";
					
					//Get the xbowDataFile
					if(filesMap.containsKey(name))
						write = filesMap.get(name);
					else{
						
						write = new File(name);
						// clears file
						PrintWriter output = new PrintWriter(new BufferedWriter(
								new FileWriter(write)));
						output.print("");
						output.close();
					}
					
					// write data to file
					try {
						PrintWriter output = new PrintWriter(new BufferedWriter(
								new FileWriter(write, true)));
						
						String toWrite = time+",";
						
						//xbow data gotten and converted
						ArrayList<Double> convertedData = 
								xbowValue(info);
						
						//make xbowData into a string to be written
						for(int i=0;i<convertedData.size();i++)
							toWrite += (convertedData.get(i)) +",";
						
						toWrite = toWrite.substring(0,toWrite.length()-1);
						output.println(toWrite);
						
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					filesMap.put(name, write);
					
					continue;
				}
				
				//Normal Data case
				
				double value = binToDec(info);
				value = configValue(value, ID);
				
				String name;
				File write;

				// determines what the fileName to write to disk is
				if (nameMap.containsKey(ID))
					name = nameMap.get(ID);
				else
					name = ID;

				// If we already opened the correct file to write to
				if (filesMap.containsKey(name))
					write = filesMap.get(name);
				else {

					write = new File(name + ".txt");
					// clears file
					PrintWriter output = new PrintWriter(new BufferedWriter(
							new FileWriter(write)));
					output.print("");
					output.close();
				}

				// write data to file
				try {
					PrintWriter output = new PrintWriter(new BufferedWriter(
							new FileWriter(write, true)));
					output.println(time + "," + value);
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				filesMap.put(name, write);

			}

		} catch (IOException e) {

			errorCall(3);
		} catch (NumberFormatException e){
			
			errorCall(10);
		}

		System.out.println("Generated ID-Name Map");
	}
	
	//Call this to invoke error
	//@x = Represents error number
	private static void errorCall(int x){
		
		switch(x){
		
		case 1: 
			JOptionPane.showMessageDialog(null, "Error 01: Corrupt Config File");
			break;
		case 2: 
			JOptionPane.showMessageDialog(null, "Error 02: Config File Error - Invalid input line ");
			break;
		case 3:
			JOptionPane.showMessageDialog(null, "Error 03: Corrupt Data File");
			break;
		case 4:
			JOptionPane.showMessageDialog(null, "Error 04: Data File error. Is it in the format of ID:Name ? ");
			break;
		case 10:
			JOptionPane.showMessageDialog(null, "Error 10x: Data File error. First line of file is not the time ? ");
			break;
		case 11:
			JOptionPane.showMessageDialog(null, "Error 11: Corrupt Config File - None Selected");
			break;
		case 20: 
			JOptionPane.showMessageDialog(null, "Error 20: XBow Data Corrupted - Too Short Data File");
			break;
		default:
			JOptionPane.showMessageDialog(null, "Unknown Error:"+x);
			break;
		}
		System.exit(1);
	}
	
	//converts a bitstring to direct integer value in integer format
	public static int binToDecUnsigned(String bin){
		
		return Integer.parseInt(bin,2);
	}
	
	//converts a bitstring to 2's complement value in integer format
	public static int binToDec(String bin){
		
		if(bin.length()<32){
			
			int diff = 32 - bin.length();
			for(int i=0;i<diff;i++){
				
				bin = bin.charAt(0)+bin;
			}
			
		}
		
		return (int)Long.parseLong(bin,2);
	}
	
	//configs the input value correctly
	public static double configValue(double input, String ID){
		
		String[] config = configMap.get(ID);
		double offset = Double.parseDouble(config[0]);
		double scalar = Double.parseDouble(config[1]);
		return (input-offset)*scalar;
	}
	
	/* Roll Angle, Pitch Angle, yawAngleTrue, xRCorrected
	 * yRCorrected, zRCorrected, xAccel, yAccel, zAccel, 
	 * nVel, eVel, dVel, longGPS(I4), latGPS(I4), altGPS(I2), xRTemp
	 * timelTOW, BITstatus
	 * */
	public static ArrayList<Double> xbowValue(String input){
		
		if(input.length()<42){
			errorCall(20);
		}
		ArrayList<Double> toReturn = new ArrayList<Double>();
		for(int i=0;i<input.length();i++){
			
			switch(i){
			case 0:
			case 2:
			case 4:
				String angleTrueS = input.substring(i,2);
				double angleTrue = ((double)binToDec(angleTrueS))*
						((double)360/(Math.pow(2, 16)));
				toReturn.add(angleTrue);
				break;
			case 6:
			case 8:
			case 10: 
				String rCS = input.substring(i,2);
				double rC = ((double)binToDec(rCS)) *
						((double)1260/Math.pow(2,16));
				toReturn.add(rC);
				break;
			case 12:
			case 14:
			case 16: 
				String accelS = input.substring(i,2);
				double accel = ((double)binToDec(accelS)) *
						((double)20/Math.pow(2, 16));
				toReturn.add(accel);
				break;
			case 18:
			case 20:
			case 22:
				String velS = input.substring(i,2);
				double vel = ((double)binToDec(velS)) *
						((double)512/Math.pow(2, 16));
				toReturn.add(vel);
				break;
			case 24:
			case 28:
				String longlatS = input.substring(i,4);
				double longlat = ((double)binToDec(longlatS)) *
						((double)360/Math.pow(2,32));
				toReturn.add(longlat);
				break;
			case 32: 
				String altS = input.substring(i,2);
				double alt = ((double)binToDec(altS)) * 
						((double)Math.pow(2,14)/Math.pow(2,16));
				toReturn.add(alt);
				break;
			case 34: 
				String xRTS = input.substring(i,2);
				double xRT = ((double)binToDec(xRTS)) *
						((double)200/Math.pow(2, 16));
				toReturn.add(xRT);
				break;
			case 36: 
				String timelS = input.substring(i,4);
				double timel = ((double)binToDecUnsigned(timelS));
				toReturn.add(timel);
				break;
			default:
				break;
				
			}
			
		}
		return toReturn;
	}
	
	public static void main(String[] args){
		
		//Data file
		File data = null;
		File config = null;

		data = new File("data.txt");
		
		if(!data.exists())
			data = new File("Data.txt");
		
		if(!data.exists()){
			JOptionPane.showMessageDialog(null, "data.txt not found. " +
					"Press enter to manually select config file");
			data = selectFile("This is my data file");
		}
		
		
		config = new File(data.getParentFile(), "config.txt");
		if(!config.exists())
			config = new File(data.getParentFile(), "Config.txt");
		
		//Grabs key and ID
		if(!config.exists()){
			JOptionPane.showMessageDialog(null, "config.txt not found. " +
					"Press enter to manually select config file");
			config = selectFile("This is my Config file");
		}
		
		generateMap(config);
		generateConfig(config);

		generateFiles(data);
	}
}
