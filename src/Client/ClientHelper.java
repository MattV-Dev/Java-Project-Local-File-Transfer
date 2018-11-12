package Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import Protocol.CPMessage;

class ClientHelper {
	
	static String readAndDisplayListOnConsole(BufferedReader in) throws IOException {
		String inLine;
		int count = 1;
		while((inLine = in.readLine()) != null) {
			if(inLine.equals(CPMessage.SendingFinished.toString())) {
				return inLine;
			}
			System.out.println(count++ + ": " + inLine);
		}
		return null;
	}
	
	static String readAndDisplayListOnConsole(BufferedReader in,
			Collection<String> filenames) throws IOException {
		String inLine;
		int count = 1;
		while((inLine = in.readLine()) != null) {
			if(inLine.equals(CPMessage.SendingFinished.toString())) {
				return inLine;
			}
			System.out.println(count++ + ": " + inLine);
			filenames.add(inLine);
		}
		return null;
	}
	
	static String writeFromBufferedReaderToFile(BufferedReader in, int size, String name) {
		File f = new File(name);
		String inLine;
		int count = 1;
		int numberOfPackets = (size-1)/1024+1;
		System.out.println("Statring sending "+ name + " in " + numberOfPackets + " packets.");
		
		try(FileOutputStream dat = new FileOutputStream(f)){
			while((inLine = in.readLine()) != null) {
    			if(inLine.equals(CPMessage.SendingFinished.toString())) {
    				return inLine;
    			}
    			System.out.println("Packet " + count++ + "/" + numberOfPackets + ": " + "OK");
    			byte[] bytes = new byte[inLine.length()/2];
    			for(int i=0; i<inLine.length() ;i+=2) {
    				bytes[i/2] = (byte)((Character.digit(inLine.charAt(i), 16) << 4)
    						+ Character.digit(inLine.charAt(i+1), 16));
    			}
    			dat.write(bytes);
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
