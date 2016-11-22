# Amazon API Gateway RAML Importer

The **Amazon API Gateway RAML Importer** lets you create or update [Amazon API Gateway][service-page] APIs from  RAML 1.0 API representation.

To learn more about API Gateway, please see the [service documentation][service-docs] or the [API documentation][api-docs].

[service-page]: http://aws.amazon.com/api-gateway/
[service-docs]: http://docs.aws.amazon.com/apigateway/latest/developerguide/
[api-docs]: http://docs.aws.amazon.com/apigateway/api-reference

[![Build Status](https://api.travis-ci.org/awslabs/aws-apigateway-importer.svg?branch=master)](https://travis-ci.org/awslabs/aws-apigateway-importer)

## Usage

### Prerequisites

#### Credentials
This tool requires AWS credentials to be configured in at least one of the locations specified by the [default credential provider chain](http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/credentials.html).

It will look for configured credentials in environment variables, Java system properties, [AWS SDK/CLI](http://aws.amazon.com/cli) profile credentials, and EC2 instance profile credentials.

#### Installation

 - Run `git clone https://github.com/petrochenko-pavel-a/aml.git`
 - Goto to project folder
 - Run `mvn install`
 - Goto apigatewayimporter folder (`cd org.aml.apigatewayimporter`)
 - Build apigatewayimporter using following command: `mvn assembly:assembly`

### Import API 

```sh
./aws-api-import.sh -c path/to/api.raml
```

For Windows environments replace `./aws-api-import.sh` with `./aws-api-import.cmd` in the examples.

### API Gateway Extension Example

Typical usage is to to write an overlay to existing RAML api, using aws-lib.raml as a sample

Defined on an Operation:

```raml
#%RAML 1.0 Overlay
extends: apigateway.raml
uses:
  AWS: https://raw.githubusercontent.com/OnPositive/aml/master/org.aml.apigatewayimporter/apigateway-lib.raml
/products:
  get:
    (AWS.amazon-apigateway-integration):
      type: aws
      uri: "arn:aws:apigateway:us-west-2:lambda:path/2015-03-31/functions/arn:aws:lambda:us-west-2:393636047515:function:MyFunction/invocations"
      httpMethod: POST
      credentials: "arn:aws:iam::393636047515:role/lambda_exec_role"
      requestTemplates:
        application/json: "json request template 2"
        application/xml: "xml request template 2"
      requestParameters:
          integration.request.path.integrationPathParam: "method.request.querystring.latitude"
          integration.request.querystring.integrationQueryParam: "method.request.querystring.longitude"
      cacheNamespace: "cache namespace"
      cacheKeyParameters: []
      responses:
          "200":
            statusCode: "200"
            responseTemplates:
              application/json: "json 200 response template"
              application/xml: "xml 200 response template"
          "400":
            statusCode: "400"
            responseTemplates:
              application/json: "json 400 response template"
              application/xml: "xml 400 response template"

```
Detailed guide on annotations that might be used to customize import to Apigateway, may be viewed [here](https://petrochenko-pavel-a.github.io/raml-explorer/#https://raw.githubusercontent.com/OnPositive/aml/master/org.aml.apigatewayimporter/apigateway-lib.raml)


## Testing

```sh
mvn test
```
