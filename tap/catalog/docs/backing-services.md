# Backing Services

A lot of challenges can be mitigated with the help of Spring Boot and Cloud, but regarding the deployment,
there is for example still a high effort to manage the cloud infrastructure for the microservice.

For our typical Spring Cloud architecture the following backing services are required:

* VMware Tanzu SQL with Postgres database for storing orders
* VMware Tanzu RabbitMQ messaging systems for asynchronous messaging between the order and product service
* VMware Tanzu GemFire cache for caching of relational database and rest calls from the order service. In this case we are using the Redis compatible API.
* Spring Configuration Server
* OAuth 2 Authorization Server
* VMware Aria Operations for Applications tenant to provide monitoring and distributed tracing capabilities


