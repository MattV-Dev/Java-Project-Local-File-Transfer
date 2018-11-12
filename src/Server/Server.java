package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;

import Protocol.CommonProtocol;

public final class Server {
	private static final int threadCount = 10;
    private static Collection<NewServerThread> threads = new ArrayList<>();
	
    public static void main(String[] args) throws IOException{
        if(args.length != 1){
            System.err.println("Use: java Server <port>");
            System.exit(1);
        }
        
        int portNumber = Integer.parseInt(args[0]);
        boolean active = true;
        try(ServerSocket server = new ServerSocket(portNumber)){
            while(active){
            	Collection<NewServerThread> toDestroy = new ArrayList<>();
                for(NewServerThread t : threads) {
                	if(!t.isAlive()){
                		toDestroy.add(t);
                	}
                }
                threads.removeAll(toDestroy);
                
                Socket client = server.accept();
                if(threads.size() < threadCount) {
                	NewServerThread thread = new NewServerThread(client);
                	thread.start();
                	threads.add(thread);
                } else {
                	new DenyConnectionThread(client).start();
                }
            	
                
                
            }
        }catch (IOException e){
            System.err.println("Listen failure on port " + portNumber);
            System.exit(-1);
        }
    }
}

class DenyConnectionThread extends Thread{
	private Socket socket = null;
    private CommonProtocol CCP;
    
    DenyConnectionThread(Socket socket){
        super();
        this.socket = socket;
        CCP = new CommonProtocol();
    }
    
    @Override
    public void run(){
    	try(PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
            String outMessage;
            
            while((in.readLine()) != null){
                outMessage = CCP.sendDenyConnectionResponse();
                out.println(outMessage);
            }
    	}catch (IOException e){
            e.printStackTrace();
        }
    }
}
