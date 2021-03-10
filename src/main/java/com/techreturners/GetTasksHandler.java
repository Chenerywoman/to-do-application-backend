package com.techreturners;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class GetTasksHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger LOG = LogManager.getLogger(GetTasksHandler.class);

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        // this is returned when run serverless -f tasks.api
        LOG.info("request received");

		/* if want to use one handler for two methods:
			String requestMethod = request.getHttpMethod();
			e.g. GET or POST
			if post - save task, else get tasks
		*/

        String UserId = request.getPathParameters().get("userId");

        List<Task> tasks = new ArrayList<>();

        try {
            // need to refactor to put the connection to the database in a different class as will need it for each SQL query

            // instantiate the sql driver
            Class.forName("com.mysql.jdbc.Driver");

            LOG.debug(String.format("Connecting to DB on %s", System.getenv("DB_HOST")));

            // instantiate the connection
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s",
                    System.getenv("DB_HOST"),
                    System.getenv("DB_NAME"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASSWORD")));

            preparedStatement = connection.prepareStatement("SELECT * FROM tasks WHERE UserId = ?");
            preparedStatement.setString(1, UserId);
            resultSet = preparedStatement.executeQuery();

            // this gets each row of the returned data
            while (resultSet.next()) {
                Task t1 = new Task(resultSet.getString("taskId"),
                        resultSet.getString("description"),
                        resultSet.getBoolean("completed"));

                tasks.add(t1);

            }


        } catch (Exception e) {
            LOG.error(String.format("Unable to query database for tasks for user %s", UserId), e);
        } finally {
			// close the connection when it is finished, to stop using up connections

			closeConnection();
		}


		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);

        // cannot put tasks array list straight into the response;
        // need to use ObjectMapper dependency to change to a JSON object
        ObjectMapper objectMapper = new ObjectMapper();

        // need to have in a try catch, in case code errors
        try {
            // sets the tasks Array to a string (json) object
            String responseBody = objectMapper.writeValueAsString(tasks);
            // adds json string responseBody to the body of the response
            response.setBody(responseBody);

        } catch (JsonProcessingException error) {
            LOG.error("Unable to marshal tasks array", error);
        }

        return response;
    }

    // separate function to close connection - put in own reusable class, so the handler calls a different class to get the data & represent it as JSON
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


