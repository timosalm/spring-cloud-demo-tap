# Order Service

With the [order service](order-service.md) REST API, clients are able to fetch the current orders that are stored in a PostgreSQL database and they are also able to create new orders.
The product id for a new order is validated with the list of products that will be fetched from the [product service](product-service.md) via a synchronous REST call.

After the order is created and stored in the database, information like the shipping address will be send to the [shipping service](shipping-service.md) via asynchronous messaging and after a configurable amout of time (for example 10 seconds) a status update for the delivery will be sent back via asynchronous messaging.
