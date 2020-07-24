package com.hakaton.covid.bot;

public class Response {
	
	private int code;

	private String errorMessage;

	private String botResponse;


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
}
