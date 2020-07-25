package com.hakaton.covid.bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

class CovidbotApplicationTests {

	@Autowired
	private IBotService botService;

	@Test
	void contextLoads() {


	}

	@Test
	public void testBotResponse() {
		botService.getBotResponse("corona");

	}

}
