#!/bin/bash

# Exchanges for each service to publish their events
rabbitmqadmin --user=admin --password=password declare exchange name=customer-events type=fanout durable=true
rabbitmqadmin --user=admin --password=password declare exchange name=order-events type=fanout durable=true
rabbitmqadmin --user=admin --password=password declare exchange name=inventory-events type=fanout durable=true
rabbitmqadmin --user=admin --password=password declare exchange name=catalog-events type=fanout durable=true
rabbitmqadmin --user=admin --password=password declare exchange name=shipping-events type=fanout durable=true

# Each service that subscribes to an event type will need a queue for that event.
# Naming convention: subscriber-exhange
rabbitmqadmin --user=admin --password=password declare queue name=order-shipping-events durable=true
rabbitmqadmin --user=admin --password=password declare queue name=catalog-inventory-events durable=true
rabbitmqadmin --user=admin --password=password declare queue name=inventory-catalog-events durable=true

# We need to bind the subscriber queues to the exchanges
rabbitmqadmin --user=admin --password=password declare binding source=shipping-events destination=order-shipping-events
rabbitmqadmin --user=admin --password=password declare binding source=catelog-events destination=inventory-catelog-events
rabbitmqadmin --user=admin --password=password declare binding source=inventory-events destination=catalog-inventory-events
