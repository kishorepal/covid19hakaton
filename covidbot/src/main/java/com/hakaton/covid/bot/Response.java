package com.hakaton.covid.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Response {
	
	private int code;

	private String errorMessage;

	private String botResponse; //FixMe delete this legacy after completing the Client Side
	
	private HashMap<String, Float > bot_response;
	
	private List<String> action;
	
	public Response() {
		bot_response = new HashMap<String, Float>();
		action = new ArrayList<String>();
	}


	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getBotResponse() {
		return botResponse;
	}

	public void setBotResponse(String botResponse) {
		this.botResponse = botResponse;
	}

	public HashMap<String, Float> getBot_response() {
		return bot_response;
	}

	public void setBot_response(HashMap<String, Float> bot_response) {
		this.bot_response = bot_response;
	}

	public List<String> getAction() {
		return action;
	}

	public void setAction(List<String> action) {
		this.action = action;
	}
	
	
}
