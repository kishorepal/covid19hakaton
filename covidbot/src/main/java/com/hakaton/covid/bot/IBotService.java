package com.hakaton.covid.bot;

import org.springframework.stereotype.Component;


public interface IBotService {
	
	public Response getBotResponse(String query);
	

}
