package com.techreturners;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreturners.model.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

		// this is returned when run serverless -f tasks.api
		LOG.info("request received: {}");

		String userId = request.getPathParameters().get("userId");

		List<Task> tasks = new ArrayList<>();

		if (userId.equals("abc123")){
			Task t1 = new Task("abc123", "Pick up newspapers", false );
			tasks.add(t1);
		} else {
			Task t2 = new Task("abc1234" , "Learn Java!", false);
			tasks.add(t2);
		}


/*		// generating an error - to check logs

		Task t3 = null;
		System.out.println(t3.getDescription());
*/

		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);

		// cannot put tasks array list straight into the response;
		// need to use ObjectMapper dependency to change to a JSON object
		ObjectMapper objectMapper = new ObjectMapper();

		// need to have in a try catch, or code will error
		try {
			// sets the tasks Array to a string (json) object
			String responseBody = objectMapper.writeValueAsString(tasks);
			// adds json string responseBody to the body of the response
			response.setBody(responseBody);

		} catch (JsonProcessingException error){
			LOG.error("Unable to marshal tasks array");
		}
		return response;
	}
}
