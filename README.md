# Spring E-Commerce REST API

## Overview

Spring E-Commerce REST API is a modular backend service built with **Java 17** and **Spring Boot 3.5** that powers core e-commerce workflows: user account management, product catalog administration, dynamic shopping cart state, and hosted payment checkout via the **Stripe API**.

The application is structured around domain features, enforces stateless **JWT Bearer Authentication** combined with method-level RBAC/ownership guards, and is backed by a comprehensive automated test suite utilizing **JUnit 5**, **Mockito** (including static SDK mocking), and **Testcontainers**.

## Architectural & Security Highlights

* **Feature-Driven Modular Packaging:** The codebase is partitioned into self-contained domain packages (`user`, `product`, `cart`, `payment`, and `shared`), isolating controllers, services, repositories, mappers, DTOs, and exception advisors within clear business boundaries.
* **Stateless JWT Authentication:** Custom `JwtAuthenticationFilter` (`OncePerRequestFilter`) and `JwtService` (`io.jsonwebtoken:jjwt`) provide stateless session management (`SessionCreationPolicy.STATELESS`) using HMAC-SHA256 (`HS256`) signed tokens and `BCryptPasswordEncoder` credential hashing.
* **Method-Level Security & IDOR Protection (`@EnableMethodSecurity`):**
    * **Catalog Administration:** Mutating product endpoints in `ProductService` are restricted via fine-grained authorities (`@PreAuthorize("hasAuthority('CREATE_PRODUCTS')")` and `@PreAuthorize("hasAuthority('UPDATE_PRODUCTS')")`), while product browsing remains publicly accessible.
    * **Shopping Cart Isolation:** `CartService` enforces `@PreAuthorize("#username == authentication.name")` across all cart operations, guaranteeing strict resource ownership and preventing Insecure Direct Object Reference (IDOR) vulnerabilities.
* **Stripe Hosted Checkout Integration:** `PaymentController` and `PaymentService` integrate with the official **Stripe Java SDK** (`SessionCreateParams`), dynamically aggregating cart line items (priced in cents) to initialize a Stripe Checkout Session (`Session.create`) and returning the hosted redirect URL while automatically clearing the user's cart.
* **Immutable Java Record DTOs & Decentralized Error Handling:** All request and response contracts are modeled as immutable Java 17 Records. Domain-specific `@ControllerAdvice` classes map business and security exceptions to standardized `ErrorMessageResponse` payloads.
* **Comprehensive Testing Strategy:**
    * **Web Slice Tests (`@WebMvcTest`):** Controller contract and security filter chain verification (`CartControllerTest`, `PaymentControllerTest`, `ProductControllerTest`, `UserControllerTest`).
    * **Method Security & Service Unit Tests:** Verification of business logic and `@PreAuthorize` authorization rules (`AuthorizationDeniedException`) using `TestSecurityConfig` (`CartServiceTest`, `ProductServiceTest`, `UserServiceTest`).
    * **Third-Party SDK Static Mocking:** `PaymentServiceTest` utilizes Mockito's `MockedStatic<Session>` to deterministically test Stripe session creation without external network calls.
    * **Database Integration Tests (`@DataJpaTest`):** Persistence layer verification against a real PostgreSQL instance managed via **Testcontainers** and `@ServiceConnection` (`CartRepositoryTestcontainersTest`, `UserRepositoryTestcontainersTest`).

## Project Structure

```text
src/main/java/com/nn/spring_simple_e_commerce_rest_api
├── SpringSimpleECommerceRestApiApplication.java
├── cart/
│   ├── api/response/CartResponse.java          # Cart DTO with dynamic totalPrice calculation
│   ├── controller/CartController.java          # REST endpoints (/api/v1/carts)
│   ├── domain/Cart.java                        # JPA Entity with @ElementCollection product map
│   ├── repository/CartRepository.java          # Spring Data JPA repository
│   ├── service/CartService.java                # Cart logic & @PreAuthorize ownership guards
│   └── support/                                # CartMapper, CartExceptionAdvisor, CartNotFoundException
├── payment/
│   ├── api/                                    # PaymentRequest & PaymentResponse Records
│   ├── controller/PaymentController.java       # Checkout endpoint (/api/v1/payments/checkout)
│   ├── service/PaymentService.java             # Stripe Checkout Session integration
│   └── support/                                # PaymentExceptionAdvisor & custom exceptions
├── product/
│   ├── api/                                    # ProductRequest, ProductUpdateRequest, ProductResponse
│   ├── controller/ProductController.java       # Catalog endpoints (/api/v1/products)
│   ├── domain/                                 # Product Entity & ProductCategory Enum
│   ├── repository/ProductRepository.java       # Spring Data JPA repository
│   ├── service/ProductService.java             # Catalog logic & authority guards
│   └── support/                                # ProductMapper, ProductExceptionAdvisor, exceptions
├── shared/
│   ├── api/response/ErrorMessageResponse.java  # Unified error payload Record
│   ├── config/                                 # SecurityConfig & JwtAuthenticationFilter
│   ├── service/JwtService.java                 # JWT generation, parsing, and validation
│   └── support/AuthExceptionAdvisor.java       # Security & JWT exception handlers
└── user/
    ├── api/                                    # LoginRequest, RegisterRequest, LoginResponse
    ├── config/AuthConfig.java                  # UserDetailsService, BCrypt & AuthenticationManager
    ├── controller/UserController.java          # Auth endpoints (/api/v1/users)
    ├── domain/User.java                        # JPA Entity implementing UserDetails
    ├── repository/UserRepository.java          # Spring Data JPA repository
    ├── service/UserService.java                # Registration & authentication orchestration
    └── support/                                # UserMapper, UserExceptionAdvisor, exceptions
```

## Technology Stack

* **Language:** Java 17
* **Framework:** Spring Boot 3.5.7 (Spring Web, Spring Data JPA, Spring Security)
* **Authentication:** JSON Web Tokens (`io.jsonwebtoken:jjwt` 0.11.5), BCrypt
* **Payment Gateway:** Stripe Java SDK (`com.stripe:stripe-java` 24.3.0)
* **Database:** PostgreSQL
* **Testing:** JUnit 5, Mockito (`mockito-inline` / static mocking), AssertJ, Spring Security Test, Testcontainers (`postgresql`, `junit-jupiter`)
* **Build Tool:** Apache Maven (Maven Wrapper 3.9.11)

---

## Getting Started

### Prerequisites

* **JDK 17** or higher installed
* **PostgreSQL** running locally on port `5432` (for local application execution)
* **Docker** running (required for executing `@DataJpaTest` Testcontainers integration tests)
* **Stripe Developer Account** (API Secret Key for checkout session creation)

### 1. Database & Environment Configuration

1. Create a local PostgreSQL database named `ecommerce_db`:
   ```sql
   CREATE DATABASE ecommerce_db;
   ```

2. Export the required environment variables referenced in `src/main/resources/application.properties`:
   ```bash
   export DB_USERNAME=postgres
   export DB_PASSWORD=your_db_password
   # Generate a 256-bit Base64-encoded secret key (e.g., via: openssl rand -base64 32)
   export JWT_SECRET_KEY=dGhpcyBpcyBhIHZlcnkgc2VjdXJlIGJhc2U2NCBrZXkgZm9yIGp3dA==
   export STRIPE_SECRET_KEY=sk_test_your_stripe_secret_key
   ```

### 2. Running the Application

Use the repository-bound Maven Wrapper to start the application:

```bash
git clone https://github.com/Wilie12/spring-simple-e-commerce-rest-api.git
cd spring-simple-e-commerce-rest-api
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

### 3. Running Automated Tests

Ensure Docker is active on your machine, then execute the full unit, web slice, and Testcontainers suite:

```bash
./mvnw clean verify -B -ntp
```

---

## API Reference

### Authentication & Access Matrix

* **Public Endpoints:** `POST /api/v1/users/register`, `POST /api/v1/users/login`, `GET /api/v1/products`, `GET /api/v1/products/{productId}`
* **Protected Endpoints:** Require a valid Bearer token in the `Authorization` header:
  ```http
  Authorization: Bearer <your_jwt_token>
  ```

| Method | Endpoint | Access / Authority | Success Status | Description |
| :--- | :--- | :---: | :---: | :--- |
| `POST` | `/api/v1/users/register` | Public | `201 Created` | Registers a new user account with default `USER` authority. |
| `POST` | `/api/v1/users/login` | Public | `200 OK` | Authenticates credentials and returns a JWT token (`LoginResponse`). |
| `GET` | `/api/v1/products` | Public | `200 OK` | Retrieves all available products in the catalog. |
| `GET` | `/api/v1/products/{productId}` | Public | `200 OK` | Retrieves detailed information for a single product. |
| `POST` | `/api/v1/products` | `CREATE_PRODUCTS` | `201 Created` | Creates a new product in the catalog. |
| `PUT` | `/api/v1/products/{productId}` | `UPDATE_PRODUCTS` | `200 OK` | Updates an existing product's details and inventory. |
| `POST` | `/api/v1/carts` | Authenticated (Owner) | `201 Created` | Initializes an empty cart for the user (or returns existing). |
| `GET` | `/api/v1/carts` | Authenticated (Owner) | `200 OK` | Retrieves the user's cart with dynamically calculated `totalPrice`. |
| `POST` | `/api/v1/carts/items/{productId}` | Authenticated (Owner) | `200 OK` | Adds a product with specified `quantity` (default `1`) to the cart. |
| `DELETE` | `/api/v1/carts` | Authenticated (Owner) | `200 OK` | Removes all items from the authenticated user's cart. |
| `POST` | `/api/v1/payments/checkout` | Authenticated (Owner) | `200 OK` | Creates a Stripe Checkout Session for the cart total and clears the cart. |

---

### Detailed Endpoint Specifications

#### 1. Register a New User (`POST /api/v1/users/register`)
* **Request Body (`RegisterRequest`):**
  ```json
  {
    "username": "buyer1",
    "password": "securePassword123"
  }
  ```
* **Responses:** `201 Created` (`"User registered successfully"`), `409 Conflict` (Username already exists).

#### 2. User Login (`POST /api/v1/users/login`)
* **Request Body (`LoginRequest`):**
  ```json
  {
    "username": "buyer1",
    "password": "securePassword123"
  }
  ```
* **Responses:** `200 OK` (`LoginResponse`), `401 Unauthorized` (Invalid credentials).

#### 3. Create a Product (`POST /api/v1/products`)
* **Required Authority:** `CREATE_PRODUCTS`
* **Request Body (`ProductRequest`):**
  ```json
  {
    "name": "Wireless Mouse",
    "shortDescription": "Ergonomic wireless mouse",
    "fullDescription": "Detailed optical sensor specifications...",
    "price": 2500,
    "quantity": 100,
    "category": "ELECTRONICS",
    "producer": "TechCorp"
  }
  ```
* **Responses:** `201 Created` (`ProductResponse`), `403 Forbidden` (Missing authority).

#### 4. Update a Product (`PUT /api/v1/products/{productId}`)
* **Required Authority:** `UPDATE_PRODUCTS`
* **Request Body:** `ProductUpdateRequest` (Same schema as `ProductRequest`).
* **Responses:** `200 OK` (`ProductResponse`), `403 Forbidden`, `404 Not Found`.

#### 5. Manage Shopping Cart (`/api/v1/carts`)
* **Initialize Cart:** `POST /api/v1/carts` -> `201 Created` (`CartResponse`)
* **Get Cart:** `GET /api/v1/carts` -> `200 OK` (`CartResponse`), `404 Not Found`
* **Add Item to Cart:** `POST /api/v1/carts/items/{productId}?quantity=2` -> `200 OK` (`CartResponse`), `404 Not Found`
* **Clear Cart:** `DELETE /api/v1/carts` -> `200 OK` (`CartResponse`), `404 Not Found`

#### 6. Stripe Payment Checkout (`POST /api/v1/payments/checkout`)
* **Description:** Calculates the total amount from the authenticated user's cart, creates a Stripe payment session in `USD`, clears the local cart, and returns the Stripe checkout URL.
* **Responses:**
    * `200 OK` (`PaymentResponse`):
      ```json
      {
        "status": "SUCCESS",
        "message": "Payment session created",
        "sessionId": "cs_test_a1b2c3d4...",
        "sessionUrl": "https://checkout.stripe.com/c/pay/cs_test_..."
      }
      ```
    * `404 Not Found` (Cart not found for user).

---

## Data Models

### `RegisterRequest` / `LoginRequest`
| Field | Type | Description |
| :--- | :--- | :--- |
| `username` | `String` | Unique account username. |
| `password` | `String` | Raw account password. |

### `LoginResponse`
| Field | Type | Description |
| :--- | :--- | :--- |
| `token` | `String` | Signed HS256 JWT Bearer token. |
| `expiresIn` | `long` | Token expiration time in milliseconds (default `3600000` = 1 hour). |

### `ProductRequest` / `ProductUpdateRequest`
| Field | Type | Description |
| :--- | :--- | :--- |
| `name` | `String` | Display name of the product. |
| `shortDescription` | `String` | Brief summary of the item. |
| `fullDescription` | `String` | Full product specifications. |
| `price` | `long` | Unit price in minor currency units (cents) for Stripe compatibility. |
| `quantity` | `int` | Available inventory stock count. |
| `category` | `ProductCategory` | Enum: `CLOTHING`, `ELECTRONICS`, `HOME`, `BOOKS`, `SPORTS`, `HEALTH`, `AUTOMOTIVE`, `OTHER`. |
| `producer` | `String` | Manufacturer or brand name. |

### `ProductResponse`
Contains all fields from `ProductRequest`, plus:
| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | `long` | Unique database identifier. |
| `createdAt` | `Instant` | UTC timestamp of product creation. |
| `updatedAt` | `Instant` | UTC timestamp of the last modification. |

### `CartResponse`
| Field | Type | Description |
| :--- | :--- | :--- |
| `username` | `String` | Owner of the shopping cart. |
| `products` | `Map<ProductResponse, Integer>` | Mapping of product details to selected quantities. |
| `totalPrice` | `long` | Dynamically computed sum (`price * quantity`) across all cart items in cents. |

### `PaymentResponse`
| Field | Type | Description |
| :--- | :--- | :--- |
| `status` | `String` | Execution status (e.g., `"SUCCESS"`). |
| `message` | `String` | Human-readable status description. |
| `sessionId` | `String` | Unique Stripe Checkout Session identifier. |
| `sessionUrl` | `String` | Stripe-hosted payment page URL for client redirection. |

### `ErrorMessageResponse`
| Field | Type | Description |
| :--- | :--- | :--- |
| `message` | `String` | Error description returned by `@ControllerAdvice` handlers. |