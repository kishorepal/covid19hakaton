package com.hakaton.covid.bot;

import org.springframework.stereotype.Component;

@Component
public class BotService implements IBotService {
	@Override
	public Response getBotResponse(String query) {
		
		Response response = new Response();
		
		System.out.println("Covid 19 Bot Service...");
		System.out.println(String.format("Covid Bot Response Query [%s]",query));
		return response;
	}

}
