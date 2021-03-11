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
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SaveTaskHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger LOG = LogManager.getLogger(SaveTaskHandler.class);

	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

		// this is returned when run serverless -f save-tasks.api
		LOG.info("received the request");

		String userId = request.getPathParameters().get("userId");
		String requestBody = request.getBody();

		ObjectMapper objectMapper = new ObjectMapper();
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);
		Map<String, String> headers = new HashMap<>();
		headers.put("Access-Control-Allow-Origin", "*");
		response.setHeaders(headers);

		// need to have in a try catch, or code will error
		try {
			// marshalling to convert JSON object to a Java Task class
			Task t = objectMapper.readValue(requestBody, Task.class);

			Class.forName("com.mysql.jdbc.Driver");

			// instantiate the connection
			connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s",
					System.getenv("DB_HOST"),
					System.getenv("DB_NAME"),
					System.getenv("DB_USER"),
					System.getenv("DB_PASSWORD")));

			preparedStatement = connection.prepareStatement("INSERT INTO tasks VALUES (?, ?, ?, " +
					"?)");
			preparedStatement.setString(1, UUID.randomUUID().toString());
			preparedStatement.setString(2, userId);
			preparedStatement.setString(3, t.getDescription());
			preparedStatement.setBoolean(4, t.isCompleted());

			preparedStatement.execute();

			// is this needed as close connection below?
			connection.close();

		} catch (IOException error){
			LOG.error("Unable to unmarshal JSON for adding a task", error);
		} catch (ClassNotFoundException error){
			LOG.error("ClassNotFoundException", error);
		} catch (SQLException throwables) {
			LOG.error("SQL Exception", throwables);
		} finally {
			closeConnection();
		}

		return response;
	}

	private void closeConnection() {
		// close the connection when it is finished, to stop using up connections
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (connection != null) {
				connection.close();
			}

		} catch (Exception e) {
			LOG.error("Unable to close connection to MySQL - {}", e.getMessage());
		}
	}

}
