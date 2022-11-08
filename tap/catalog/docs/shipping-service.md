#Shipping Service

After the order is created and stored in the database, information like the shipping address will be send to the [shipping service](shipping-service.md) via asynchronous messaging and after a configurable amout of time (for example 10 seconds) a status update for the delivery will be sent back via asynchronous messaging to be consumed by the [order service](order-service.md).
