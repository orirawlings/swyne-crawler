package edu.iit.swyne.crawler.server;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URL;

import edu.iit.swyne.crawler.FeedAlreadyTrackedException;

public class SwyneCrawlerServerProtocol {

	@SuppressWarnings("serial")
	private static class ServerNotRunningException extends Exception {
		public ServerNotRunningException(String string) {
			super(string);
		}
	}

	public static final String SHUTDOWN_SUCCESS	= "100";
	public static final String SHUTDOWN_FAILURE	= "101";
	public static final String ADD_SUCCESS		= "102";
	public static final String ADD_FAILURE		= "103";
	public static final String REMOVE_SUCCESS	= "104";
	public static final String REMOVE_FAILURE	= "105";
	public static final String UNKNOWN_COMMAND	= "107";
	public static final String UNEXPECTED_ERROR	= "108";
	
	private static final String COMMAND_SHUTDOWN	= "shutdown";
	private static final String COMMAND_ADD			= "add";
	private static final String COMMAND_REMOVE		= "remove";
	
	public static final String SHUTDOWN_SUCCESS_MESSAGE				= SHUTDOWN_SUCCESS + " Shutdown Success";
	public static final String SHUTDOWN_FAILURE_MESSAGE				= SHUTDOWN_FAILURE + " SHUTDOWN_FAILURE";
	public static final String ADD_SUCCESS_MESSAGE					= ADD_SUCCESS + " Successfully added new feed";
	public static final String ADD_FAILURE_MESSAGE					= ADD_FAILURE + " ADD_FAILURE Unknown reason";
	public static final String ADD_FAILURE_MESSAGE_ALREADY_TRACKED	= ADD_FAILURE + " ADD_FAILURE";
	public static final String ADD_FAILURE_MESSAGE_NO_URL_GIVEN		= ADD_FAILURE + " ADD_FAILURE No URL was specified";
	public static final String REMOVE_SUCCESS_MESSAGE				= REMOVE_SUCCESS + " Successfully removed feed";
	public static final String REMOVE_FAILURE_MESSAGE				= REMOVE_FAILURE + " REMOVE_FAILURE Unknown reason";
	public static final String REMOVE_FAILURE_MESSAGE_NO_URL_GIVEN	= REMOVE_FAILURE + " REMOVE_FAILURE No URL was specified";
	public static final String UNKNOWN_COMMAND_MESSAGE				= UNKNOWN_COMMAND + " UNKNOWN_COMMAND";
	public static final String UNEXPECTED_ERROR_MESSAGE				= UNEXPECTED_ERROR + " UNEXPECTED_ERROR";

	public static String run(String command, CrawlerServer server) {
		String response = "";
		
		BufferedReader in = new BufferedReader(new StringReader(command));
		try {
			if (!server.isRunning()) throw new ServerNotRunningException("Swyne crawler server not running.");
			
			String line = null;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				String[] args = line.split("\\s");
				
				if (args[0].compareToIgnoreCase(COMMAND_SHUTDOWN) == 0) {
					server.stopServer();
					if (!server.isRunning()) response += SHUTDOWN_SUCCESS_MESSAGE;
					else response += SHUTDOWN_FAILURE_MESSAGE;
					return response;
				}
				else if (args[0].compareToIgnoreCase(COMMAND_ADD) == 0) {
					if (args.length < 2)
						response += ADD_FAILURE_MESSAGE_NO_URL_GIVEN;
					else {
						URL feedURL = new URL(args[1]);
						server.getCrawler().addFeed(feedURL);
						response += !server.getCrawler().isTrackingFeed(feedURL) ? ADD_FAILURE_MESSAGE : ADD_SUCCESS_MESSAGE+" "+feedURL.toString();
					}
				}
				else if (args[0].compareToIgnoreCase(COMMAND_REMOVE) == 0) {
					if (args.length < 2)
						response += REMOVE_FAILURE_MESSAGE_NO_URL_GIVEN;
					else {
						URL feedURL = new URL(args[1]);
						server.getCrawler().removeFeed(feedURL);
						response += server.getCrawler().isTrackingFeed(feedURL) ? REMOVE_FAILURE_MESSAGE : REMOVE_SUCCESS_MESSAGE+" "+feedURL.toString();
					}
				}
				else response += UNKNOWN_COMMAND_MESSAGE+" \""+line+"\"";
				
				response += "\n";
			}
		}
		catch (FeedAlreadyTrackedException e) {
			response += ADD_FAILURE_MESSAGE_ALREADY_TRACKED + " " + e.getMessage() + "\n";
		}
		catch (Exception e) {
			response += UNEXPECTED_ERROR_MESSAGE + " \""+e.getMessage()+"\"\n";
		}
		return response;
	}

}
