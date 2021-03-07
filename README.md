# to-do-application-backend-api

A Java backend api created during the TechReturners Your Return To Tech course.  It can serve the to-do-application repository. 

The project uses the serverless framework to deploy the project to AWS & create AWS Lambda functions.

To set up the project code:
* Install the serverless cli on your machine: [install serverless](https://www.serverless.com/framework/docs/getting-started/)
* Set up an AWS IAM user called 'serverless' with programmatic access [set up IAM user](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html)
* Copy the AWS key & secret or download the CSV file from the AWS IAM console  
* Create the following file:
  ~/.aws/credentials
* Add the serverless credentials to the .aws/credentials file [add serverless credentials](https://www.serverless.com/framework/docs/providers/aws/guide/credentials/#setup-with-serverless-config-credentials-command)
  `serverless config credentials --provider aws --key AKIAIOSFODNN7EXAMPLE --secret wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY --profile serverless`
* When you input `cat ~/.aws/credentials`, it should output
  `[serverless] 
  aws_access_key_id=AKIAIOSFODNN7EXAMPLE
  aws_secret_access_key=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY`

To build & deploy:
* `mvn install`
* `serverless deploy`

