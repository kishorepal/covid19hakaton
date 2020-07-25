package com.hakaton.covid.bot;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.dialogflow.v2beta1.*;
import com.google.common.collect.Maps;
import com.hakaton.covid.common.Constants;
import com.hakaton.covid.common.ResponseCode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

@Component
public class BotService implements IBotService {
	@Override
	public Response getBotResponse(String query) {
		
		Response response = new Response();
		
		System.out.println("Covid 19 Bot Service...");
		System.out.println(String.format("Covid Bot Response Query [%s]",query));

		try{
			QueryResult queryResult = detectIntentTexts(query);
			response.setCode(ResponseCode.SUCCESS.hashCode()); //FIXME get by Value after completing Enum implementation
            response.setBotResponse(queryResult.getFulfillmentText());

		} catch (Exception ex) {

			ex.printStackTrace();

			response.setCode(ResponseCode.FAILED.hashCode()); //FIXME get by Value after completing Enum implementation
			response.setErrorMessage(ex.getMessage());
		}
		return response;
	}


	// DialogFlow API Detect Intent sample with text inputs.
	public QueryResult detectIntentTexts(String query) throws IOException, ApiException {

		// Instantiates a client
		    SessionsClient sessionsClient = SessionsClient.create();
			// Set the session name using the sessionId (UUID) and projectID (my-project-id)
			SessionName session = SessionName.of(Constants.PROJECT_ID, Constants.SESSION_ID);
			System.out.println("Session Path: " + session.toString());

				// Detect intents for each text input

				// Set the text (hello) and language code (en-US) for the query
				TextInput.Builder textInput =
						TextInput.newBuilder().setText(query).setLanguageCode(Constants.LANGUAGE_CODE);

				// Build the query with the TextInput
				QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

				// Performs the detect intent request
				DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

				// Display the query result
				QueryResult queryResult = response.getQueryResult();

				System.out.println("====================");
				System.out.format("User Query: '%s'\n", queryResult.getQueryText());
                   /* System.out.format(
                            "Detected Intent: %s (confidence: %f)\n",
                            queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());*/
				System.out.format("Response: '%s'\n", queryResult.getFulfillmentText());

				return  queryResult;



	}

}
