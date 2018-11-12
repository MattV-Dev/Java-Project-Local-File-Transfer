package Protocol;

import java.util.Scanner;

public class CommonProtocol {
	
	public String processResponse(String response) {
		String message = "Could not read server response.";
		try(Scanner sc = new Scanner(response)){
			int responseCode = sc.nextInt();
			String responseMessage = sc.nextLine().trim();
			switch (responseCode) {
			case 100:
				message = processOpenConnectionResponse(responseMessage);
				break;
			case 101:
				message = processDenyConnectionResponse(responseMessage);
				break;
			case 102:
				message = processCloseConnectionResponse(responseMessage);
				break;
			case 200:
				message = processOKResponse(responseMessage);
				break;
			case 201:
				message = processOpenTextStreamResponse(responseMessage);
				break;
			case 202:
				message = processOpenByteStreamResponse(responseMessage);
				break;
			case 203:
				message = processSendingFinishedResponse(responseMessage);
				break;
			case 204:
				message = processEmptyResponse(responseMessage);
				break;
			case 205:
				message = processAmbiguousArgumentResponse(responseMessage);
				break;
			case 400:
				message = processBadRequestResponse(responseMessage);
				break;
			case 401:
				message = processIncompleteRequestResponse(responseMessage);
				break;
			case 404:
				message = processErrorResponse(responseMessage);
				break;
			}
		}
		return message;
	}
	
	public String processRequest (String request) {
		String message;
		try(Scanner sc = new Scanner(request)){
			String requestToken = sc.next();
			String argument;
			switch (requestToken) {
			case "open":
				message = processOpenRequest();
				break;
			case "list":
				argument = sc.nextLine().trim();
				message = processListRequest(argument);
				break;
			case "fetch":
				argument = sc.nextLine().trim();
				message = processFetchRequest(argument);
				break;
			case "help":
				message = processHelpRequest();
				break;
			case "close":
				message = processCloseRequest();
				break;
			default:
				message = CPMessage.BadRequest.toString();	
				break;
			}
		}
		catch(RuntimeException e) {
			message = CPMessage.IncompleteRequest.toString();
		}
		return message;
	}
	public String getRequestArgument(String request) {
		String argument = null;
		try(Scanner sc = new Scanner(request)){
			String requestToken = sc.next();
			switch (requestToken) {
			case "open":
				break;
			case "list":
				argument = sc.nextLine().trim();
				break;
			case "fetch":
				argument = sc.nextLine().trim();
				break;
			case "help":
				break;
			case "close":
				break;
			default:
				break;
			}
		}
		catch(RuntimeException e) {
			argument = null;
		}
		return argument;
	}
	public String sendDenyConnectionResponse() {
		return CPMessage.DenyConnection.toString();
	}
	public String sendSendingFinishedResponse() {
		return CPMessage.SendingFinished.toString();
	}
	public String sendAmbiguousArgumentResponse() {
		return CPMessage.AmbiguousArgument.toString();
	}
	
	private String processOpenConnectionResponse(String response) {
		return "Connection open.";
	}
	private String processDenyConnectionResponse(String response) {
		return "Connection denied.";
	}
	private String processCloseConnectionResponse(String response) {
		return "Connection closed.";
	}
	private String processOKResponse(String response) {
		return "Done.";
	}
	private String processOpenTextStreamResponse(String response) {
		return "Preparing to receive text data.";
	}
	private String processOpenByteStreamResponse(String response) {
		return "Preparing to receive data";
	}
	private String processSendingFinishedResponse(String response) {
		return "Sending data finished.";
	}
	private String processEmptyResponse(String response) {
		return "";
	}
	private String processAmbiguousArgumentResponse(String response) {
		return "Argument Nonconclusive. Please specify.";
	}
	private String processBadRequestResponse(String response) {
		return "Invalid request.";
	}
	private String processIncompleteRequestResponse(String response) {
		return "Incomplete request.";
	}
	private String processErrorResponse(String response) {
		return "Error occured.";
	}
	
	private String processOpenRequest() {
		return CPMessage.OpenConnection.toString();
	}
	private String processListRequest(String argument) {
		return CPMessage.OpenTextStream.toString();
	}
	private String processFetchRequest(String argument) {
		return CPMessage.OpenByteStream.toString();
	}
	private String processHelpRequest() {
		return CPMessage.Empty.toString();
	}
	private String processCloseRequest() {
		return CPMessage.CloseConnection.toString();
	}
	
	
}
