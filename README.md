# Choreography

Step 1: Install Docker

https://docs.docker.com/get-docker/

Step 2: Open the project in IntelliJ and run the docker-compose.yaml file

You can run docker using the run button or the following command:

docker-compose up -d

Check on the docker-desktop app or the terminal if the zookeeper and kafka is up and running.

Note: You only need to run the docker-compose.yaml for one repository.

Step 3: Run all the Application run files for each and every service.

Step 4: Hit the following endpoint on Postman:

POST http://localhost:8080/order/create

JSON body:
{
    "userId": 1, 
    "productId": 3
}

Expected Response:
{
    "id": "5c31f67c-8186-45fd-953e-d2feee7b9002",
    "userId": 1,
    "productId": 3,
    "price": 300.0,
    "status": "ORDER_CREATED"
}

Step 5: View all the orders and thier status using the following endpoint:

GET http://localhost:8080/order/all

Expected Response:
[
    {
        "orderId": "6a26c43e-58d6-4967-8a7d-0b8ba2f93e1d",
        "userId": 1,
        "productId": 3,
        "amount": 300.0,
        "status": "ORDER_COMPLETED"
    }
]

Step 6: Go to jmeter repositor to perform load testing and further analysis

https://github.com/hunnybalani/jmeter-installation

