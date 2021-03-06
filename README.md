# to-do-application-backend-api
A Java backend api to serve the to-do-application repository. 
The project uses the serverless framework to deploy the project to AWS & create AWS Lambda functions.

To run:
* install the serverless cli on your machine: [install serverless](https://www.serverless.com/framework/docs/getting-started/)
* set up an AWS IAM user called 'serverless' with programmatic access [set up IAM user](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html)
* copy the AWS key & secret or download the CSV file from the AWS IAM console  
* create the following file:
  ~/.aws/credentials
* add the serverless credentials to the .aws/credentials file [add serverless credentials](https://www.serverless.com/framework/docs/providers/aws/guide/credentials/#setup-with-serverless-config-credentials-command)
  `serverless config credentials --provider aws --key AKIAIOSFODNN7EXAMPLE --secret wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY`
* mvn install
* serverless deploy

