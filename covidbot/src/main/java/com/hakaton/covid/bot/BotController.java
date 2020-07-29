package com.hakaton.covid.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {
	
	@Autowired
	private IBotService botService;
	
    @RequestMapping(value = "/api/bot_response")
	public @ResponseBody Object getBotResponse(@Param("query") String query ) {
		
		System.out.println("getBotResponse");
		
		Response response = botService.getBotResponse(query);
		
		
		
		return response;
		
	}
}
