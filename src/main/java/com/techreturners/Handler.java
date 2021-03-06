package com.techreturners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.techreturners.model.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		// this is returned when run serverless -f tasks.api
		LOG.info("request received: {}", input);
//		Response responseBody = new Response("Hello World!", input);

		Task t1 = new Task("abc123", "Pick up newspapers", false );
		Task t2 = new Task("abc1234" , "Learn Java!", false);

/*		// generating an error - to check logs

		Task t3 = null;
		System.out.println(t3.getDescription());
*/
		List<Task> tasks = new ArrayList<>();
		tasks.add(t1);
		tasks.add(t2);


		// builder will convert to JSON to send the response
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(tasks)
				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
				.build();
	}
}
