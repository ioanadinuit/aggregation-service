# Aggregation Service

## Run this application by using these commands:

1. Clone the application and move to the root where pom.xml is running:

##### &emsp;&emsp;&emsp;git clone https://github.com/ioanadinuit/aggregationservice.git

2. Change directory

##### &emsp;&emsp;&emsp;cd aggregetionservice

3. Build and run:

##### &emsp;&emsp;&emsp;docker-compose up --build

&emsp;&emsp;If running in background is necessary: 

##### &emsp;&emsp;&emsp;docker-compose up --build -d


##### &emsp; Application runs on port 8085.

### &emsp;Mentions
* Access in postman/browser:

&emsp; http://127.0.0.1:8085/aggregation?shipmentsOrderNumbers=987654321,123456789&trackOrderNumbers=987654321,123456789&pricingCountryCodes=NL,CN

* There is a postman collection in this root dir: 

#### &emsp;&emsp;&emsp;Assignment.postman_collection.json


* Make sure to have the qwkz/backand-services pulled beforehand:

#### &emsp;&emsp;&emsp;docker pull qwkz/backend-services:latest

* Run tests:
#### &emsp;&emsp;&emsp;mvn clean package
&emsp;&emsp;NOTE: both applications should be up and running to be able to run the integration tests

&emsp;&emsp;In a CI/CD environment a special run automation tests should be in place

&emsp;&emsp;In this case, unit tests and automated integration tests will run together

* Documentation can be found here:
  
https://github.com/ioanadinuit/aggregationservice/blob/main/documentation
