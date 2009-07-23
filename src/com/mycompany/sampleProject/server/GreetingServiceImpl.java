package com.mycompany.sampleProject.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mycompany.sampleProject.client.GreetingService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {
	static ReadEPAData rep = null;
	public GreetingServiceImpl() {
		 if (rep == null) {
			 rep = new ReadEPAData();
			 System.out.println("creating EPA");
		 } 
		 else {
			 System.out.println("Not creating EPA");
		 }
	}
	public String greetServer(String input) {
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		try {
		//rep.readFileDataAndLoad();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}
}
