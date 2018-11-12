package Client;

import java.util.*;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

import Protocol.CommonProtocol;
import Protocol.CPMessage;
import java.io.*;

public class Client {
    public static void main(String[] args) throws IOException{
        if(args.length != 2){
            System.err.println("Use: java Client <host> <port>");
        }
        
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        CommonProtocol CCP = new CommonProtocol();
        
        try(Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(
                		new InputStreamReader(socket.getInputStream()));
        		Scanner sc = new Scanner(System.in);
        ){
        	
            String inLine,outLine;
            System.out.println("Patcher ver 0.1a.\n"
            		+ "Type \"help\" for list of commands");
            out.println("open");
            
            while((inLine = in.readLine()) != null){
            	if(!inLine.equals(CPMessage.Empty.toString())) {
            		System.out.println(CCP.processResponse(inLine));
            	}
                if(inLine.equals(CPMessage.CloseConnection.toString())){
                    break;
                }
                if(inLine.equals(CPMessage.DenyConnection.toString())){
                    break;
                }
                
                if(inLine.equals(CPMessage.OpenTextStream.toString())) {
                	try {
                		String exitMessage = receiveAndDisplayText(in);
                		System.out.println(CCP.processResponse(exitMessage));
                	} catch(ErrorWhileSending e) {
                		System.out.println("An error occured while sending.");
                	}
                } else if(inLine.equals(CPMessage.OpenByteStream.toString())) {
                	try {
                		String exitMessage = receiveAndSaveToFileBinary(in);
                		System.out.println(CCP.processResponse(exitMessage));
                	} catch(ErrorWhileSending e) {
                		System.out.println("An error occured while sending.");
                	}
                } else if(inLine.equals(CPMessage.AmbiguousArgument.toString())) {
                	try {
                		String filename = receiveTextForSpecifying(in,sc);
                		System.out.println("Fetching " + filename);
                		String request = "fetch " + filename;
                		out.println(request);
                		continue;
                	} catch(ErrorWhileSending e) {
                		System.out.println("An error occured while sending.");
                	} catch(EmptyChoiceList e) {
                		System.out.println("No file matches the request.");
                	}
                }
                
                System.out.print(">> ");
                String request = sc.nextLine();
                if(request.equals("help")) {
                	System.out.println(showListOfCommands());
                }
                outLine = request;
                out.println(outLine);
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Patcher has closed.");
    }
    
    private static String receiveTextForSpecifying(BufferedReader in, Scanner sc)
    		throws ErrorWhileSending, EmptyChoiceList {
    	try {
    		Collection<String> filenames = new ArrayList<>();
    		ClientHelper.readAndDisplayListOnConsole(in, filenames);
    		if(filenames.isEmpty()) {
    			throw new EmptyChoiceList();
    		}
    		
    		int choice = 0;
    		while(choice <= 0 || choice > filenames.size()) {
    			System.out.print("Choose the file (1-" + filenames.size() + "): ");
    			try{
    				choice = Integer.parseInt(sc.nextLine());
    			} catch (RuntimeException e) {;}
    		}
    		
    		return (String)filenames.toArray()[choice-1];
    	} catch (IOException e) {
    		throw new ErrorWhileSending();
    	}
    }
    
    private static String receiveAndDisplayText(BufferedReader in) throws ErrorWhileSending {
    	try {
    		return ClientHelper.readAndDisplayListOnConsole(in);
    	} catch (IOException e) {
    		throw new ErrorWhileSending();
    	}
    }
    
    private static String receiveAndSaveToFileBinary(BufferedReader in) throws ErrorWhileSending {
    	try {
    		String meta = in.readLine();
    		int size;
    		String name;
    		
    		if(meta == null)
    			throw new ErrorWhileSending();
    		try(Scanner sc = new Scanner(meta)){
    			size = Integer.parseInt(sc.next());
    			name = sc.nextLine().trim();
    		}catch(RuntimeException e) {
    			throw new ErrorWhileSending();
    		}
    		
    		return ClientHelper.writeFromBufferedReaderToFile(in, size, name);
	    		
    	} catch (IOException e) {
    		throw new ErrorWhileSending();
    	}
    }
    
    private static String showListOfCommands() {
    	return new String("open - estabilish connection with the server, "
    			+ "unless the connection has already been estabilished\n"
    			+ "close - close the connection with the server, "
    			+ "use \"close\" command to exit the program\n"
    			+ "list <word> - request a list of files from the server "
    			+ "containing <word> in their name,"
    			+ "in order to list all the files use: list .\n"
    			+ "fetch <name> - request a file to be sent from the server, "
    			+ "it will copy the file to the client side\n"
    			+ "help - display list of viable commands with their usage");
    }
    
    private static class ErrorWhileSending extends Exception{
    	private static final long serialVersionUID = 123456789L;}
    private static class EmptyChoiceList extends Exception {
    	private static final long serialVersionUID = 987654321L;}
}
