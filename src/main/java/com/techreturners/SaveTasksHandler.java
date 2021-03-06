package com.techreturners;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreturners.model.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SaveTasksHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(SaveTasksHandler.class);

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

		// this is returned when run serverless -f tasks.api
		LOG.info("request received: {}");

		String userId = request.getPathParameters().get("userId");
		String requestBody = request.getBody();

		ObjectMapper objectMapper = new ObjectMapper();
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);

		// need to have in a try catch, or code will error
		try {
			// marshalling to convert JSON object to a Java Task class
			Task t = objectMapper.readValue(requestBody, Task.class);
			LOG.debug("Saved task " + t.getDescription());
			response.setBody("Task Saved");

		} catch (IOException error){
			LOG.error("Unable to unmarshal JSON for adding a task", error);
		}
		return response;
	}
}
