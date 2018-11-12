package Server;

import java.net.*;
import java.util.*;
import Protocol.CommonProtocol;
import Protocol.CPMessage;
import java.io.*;

public class NewServerThread extends Thread {
    private Socket socket = null;
    private CommonProtocol CCP;
    private String repositoryName;
    
    NewServerThread(Socket socket){
        super();
        this.socket = socket;
        CCP = new CommonProtocol();
        repositoryName = "Repository";
    }
    
    @Override
    public void run(){
        try(PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
            String inLine, outMessage;
            
            while((inLine = in.readLine()) != null){
                System.out.println(socket.toString() + ": " + inLine);
                outMessage = CCP.processRequest(inLine);
                
                if(outMessage.equals(CPMessage.OpenByteStream.toString())) {
                	File checker = new File(repositoryName
                			+ "/" + CCP.getRequestArgument(inLine));
                	
                	if(!checker.exists() || CCP.getRequestArgument(inLine).equals(".")
                			|| CCP.getRequestArgument(inLine).contains("..")) {
                		out.println(CCP.sendAmbiguousArgumentResponse());
                		listFilesInRepository(out, CCP.getRequestArgument(inLine));
                		outMessage = CCP.sendSendingFinishedResponse();
                	}
                }
                
                out.println(outMessage);
                
                if(outMessage.equals(CPMessage.OpenTextStream.toString())) {
                	listFilesInRepository(out, CCP.getRequestArgument(inLine));
                	out.println(CCP.sendSendingFinishedResponse());
                }
                if(outMessage.equals(CPMessage.OpenByteStream.toString())) {
                	sendFileFromRepository(out, CCP.getRequestArgument(inLine));
                	out.println(CCP.sendSendingFinishedResponse());
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    private void sendFileFromRepository(PrintWriter listOut, String argument) {
    	File f = new File(repositoryName + "/" + argument);
    	listOut.println(f.length() + " " + f.getName());
    	
    	try(FileInputStream dat = new FileInputStream(f)){
    		byte[] buffer = new byte[1024];
    		int bufferSize = 0;
    		while((bufferSize = dat.read(buffer)) != -1) {
    			StringBuilder str = new StringBuilder();
    			for(int i=0;i<bufferSize;i++) {
    				str.append(String.format("%02x", buffer[i]));
    			}
    			listOut.println(str.toString());
    		}
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    }
    
    private void listFilesInRepository(PrintWriter listOut, String argument) {
    	if(argument == null) {
    		return;
    	}
    	Collection<String> filenames;
    	if(argument.equals(".")) {
    		filenames = FileHandler.getFilenamesInDirectory(repositoryName);
    	} else {
    		filenames = FileHandler.getFilenamesInDirectory(repositoryName, argument);
    	}
    	System.out.println(filenames);
    	for(String s : filenames) {
    		listOut.println(s);
    	}
    }
    
}
