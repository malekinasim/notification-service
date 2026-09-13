Notification Service

A multi-tenant, event-driven notification microservice built with Java and Spring Boot. It accepts notification requests over REST, resolves a routing policy per tenant, and dispatches messages across multiple channels with retries and full delivery tracking.

Key Features
Event-driven ingestion — notifications are queued and processed asynchronously via Apache Kafka (producer/consumer), decoupling submission from delivery.
Multi-channel delivery — pluggable provider clients for SendGrid, Twilio, and SMTP behind a common NotificationProviderClient interface, resolved at runtime by a ProviderFactory.
Policy-based routing — each notification follows a RoutingPolicy made of ordered RoutingPolicySteps (e.g. try email, then fall back to SMS), each with its own retry limit; routes progress through PENDING → PROCESSING → SENT / FAILED / SKIPPED.
Delivery tracking — DeliveryAttempt and NotificationStatusHistory record every send attempt and status transition for full auditability.
Templated messages — notifications are rendered from reusable Templates with typed TemplateVariables before dispatch.
Multi-tenancy — tenant context is resolved per request (TenantContextFilter) and propagated through the domain model (TenantBaseEntity, TenantAwarePrincipal), isolating data per tenant.
REST API — POST /notification to submit a new notification for processing.
Security & validation — Spring Security integration, custom payload validation annotations, centralized exception handling, and i18n-ready messages.
Tech Stack

Java · Spring Boot 3.5 · Spring Data JPA · Spring Security · Spring Kafka · Spring AMQP (RabbitMQ) · PostgreSQL · Lombok · Docker Compose

Status

Actively evolving personal project. The core domain model, routing-plan generation, and provider dispatch pipeline are implemented; automated test coverage and docs are still growing.
