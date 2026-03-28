# Simple E-commerce API

A comprehensive, logic-heavy RESTful API built with Spring Boot that serves as the backend for an e-commerce platform. This project handles a complex data model mapping Users, Products, and Shopping Carts, while implementing robust JWT-based authentication. It also features a seamless integration with the Stripe API to handle secure payment checkouts.

## Technologies Used

  * **Java**
  * **Spring Boot** (Web, Security, Data JPA)
  * **Spring Security (JWT)** for stateless authentication
  * **PostgreSQL** for relational data mapping
  * **Stripe Java SDK** for payment gateway integration
  * **Maven**

-----

## Configuration & Setup

To run this application, you will need a running PostgreSQL database (defaulting to `localhost:5432/ecommerce_db`) and an active Stripe developer account.

Configure the following environment variables before starting the server:
* `DB_USERNAME`: Your database username.
* `DB_PASSWORD`: Your database password.
* `JWT_SECRET_KEY`: A secure, Base64-encoded secret key used to sign the authentication tokens.
* `STRIPE_SECRET_KEY`: Your secret API key provided by your Stripe dashboard.

-----

## API Reference

**Base URLs:**
* Users: `/api/v1/users`
* Products: `/api/v1/products`
* Carts: `/api/v1/carts`
* Payments: `/api/v1/payments`

*(The application will start by default on `http://localhost:8080`)*

**Authentication:** Endpoints under Users (register/login) and `GET` requests for Products are public. All other endpoints require a valid JWT passed in the `Authorization` header as a Bearer token:
`Authorization: Bearer <your_jwt_token>`

### 1\. Register a New User

Creates a new user account.

  * **URL:** `/register` (appended to Users Base URL)
  * **Method:** `POST`
  * **Request Body:**
    ```json
    {
      "username": "buyer1",
      "password": "securePassword"
    }
    ```
  * **Success Response:**
      * **Code:** `201 CREATED`
      * **Content:** `"User registered successfully"` (String)
  * **Error Response:**
      * **Code:** `409 Conflict` (If the username already exists)

### 2\. User Login

Authenticates a user and returns a JWT session token and expiration time.

  * **URL:** `/login` (appended to Users Base URL)
  * **Method:** `POST`
  * **Request Body:**
    ```json
    {
      "username": "buyer1",
      "password": "securePassword"
    }
    ```
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** `LoginResponse` (JSON)
  * **Error Response:**
      * **Code:** `401 Unauthorized` (If credentials are bad)

### 3\. Get All Products

Retrieves a list of all available products (Public).

  * **URL:** `/` (appended to Products Base URL)
  * **Method:** `GET`
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** `List<ProductResponse>` (JSON)

### 4\. Get a Single Product

Retrieves specific product details (Public).

  * **URL:** `/{productId}` (appended to Products Base URL)
  * **Method:** `GET`
  * **URL Parameters:** `productId=[Long]` (Required)
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** `ProductResponse` (JSON)
  * **Error Response:**
      * **Code:** `404 Not Found` (If the product does not exist)

### 5\. Create a Product

Creates a new product listing. Requires a valid JWT and the `CREATE_PRODUCTS` authority.

  * **URL:** `/` (appended to Products Base URL)
  * **Method:** `POST`
  * **Headers:** `Authorization: Bearer <token>`
  * **Request Body:**
    ```json
    {
      "name": "Wireless Mouse",
      "shortDescription": "Ergonomic wireless mouse",
      "fullDescription": "Detailed specs...",
      "price": 2500,
      "quantity": 100,
      "category": "ELECTRONICS",
      "producer": "TechCorp"
    }
    ```
  * **Success Response:**
      * **Code:** `201 CREATED`
      * **Content:** `ProductResponse` (JSON)
  * **Error Response:**
      * **Code:** `403 Forbidden` (If the user does not have access to perform creation)

### 6\. Update a Product

Modifies an existing product. Requires a valid JWT and the `UPDATE_PRODUCTS` authority.

  * **URL:** `/{productId}` (appended to Products Base URL)
  * **Method:** `PUT`
  * **Headers:** `Authorization: Bearer <token>`
  * **URL Parameters:** `productId=[Long]` (Required)
  * **Request Body:** `ProductUpdateRequest` (JSON)
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** The updated `ProductResponse` object.
  * **Error Response:**
      * **Code:** `404 Not Found` (If the product does not exist)
      * **Code:** `403 Forbidden` (If the user does not have access to perform update)

### 7\. Initialize Cart

Initializes a new, empty shopping cart for the authenticated user (or fetches it if it already exists).

  * **URL:** `/` (appended to Carts Base URL)
  * **Method:** `POST`
  * **Headers:** `Authorization: Bearer <token>`
  * **Success Response:**
      * **Code:** `201 CREATED`
      * **Content:** `CartResponse` (JSON)

### 8\. Get Cart

Retrieves the user's current shopping cart, including the dynamically calculated total price.

  * **URL:** `/` (appended to Carts Base URL)
  * **Method:** `GET`
  * **Headers:** `Authorization: Bearer <token>`
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** `CartResponse` (JSON)
  * **Error Response:**
      * **Code:** `404 Not Found` (If the cart does not exist)

### 9\. Add Item to Cart

Adds a specific product to the user's cart.

  * **URL:** `/items/{productId}` (appended to Carts Base URL)
  * **Method:** `POST`
  * **Headers:** `Authorization: Bearer <token>`
  * **URL Parameters:** `productId=[Long]` (Required)
  * **Query Parameters:** `quantity=[Integer]` (Optional, defaults to 1)
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** The updated `CartResponse` object.
  * **Error Responses:**
      * **Code:** `404 Not Found` (If the cart or product does not exist)

### 10\. Clear Cart

Empties all items from the user's shopping cart.

  * **URL:** `/` (appended to Carts Base URL)
  * **Method:** `DELETE`
  * **Headers:** `Authorization: Bearer <token>`
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** The emptied `CartResponse` object.
  * **Error Response:**
      * **Code:** `404 Not Found` (If the cart does not exist)

### 11\. Checkout

Calculates the total price of the user's cart, generates a secure Stripe Payment Session, clears the user's local cart, and returns the Stripe checkout URL.

  * **URL:** `/checkout` (appended to Payments Base URL)
  * **Method:** `POST`
  * **Headers:** `Authorization: Bearer <token>`
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** `PaymentResponse` (JSON)
  * **Error Response:**
      * **Code:** `404 Not Found` (If the cart does not exist)

-----

## Data Models

### ProductRequest / ProductUpdateRequest
Payload used to create or edit products in the catalog.
| Field | Type | Description |
| :--- | :--- | :--- |
| `name` | String | The display name of the product |
| `shortDescription` | String | A brief summary of the item |
| `fullDescription` | String | Detailed specifications and info |
| `price` | long | The price of the product (represented in cents for Stripe compatibility) |
| `quantity` | int | Current available inventory stock |
| `category` | ProductCategory | Enum mapping (e.g., `ELECTRONICS`, `CLOTHING`, `HOME`) |
| `producer` | String | The brand or manufacturer |

### ProductResponse
The standard response representing a catalog item. Contains all fields from `ProductRequest`, plus:
| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | long | Unique database identifier |
| `createdAt` | Instant | Timestamp of creation |
| `updatedAt` | Instant | Timestamp of the last update |

### CartResponse
Represents the user's shopping cart state.
| Field | Type | Description |
| :--- | :--- | :--- |
| `username` | String | The owner of the cart |
| `products` | Map\<ProductResponse, Integer\> | A dictionary mapping the product details to the quantity selected |
| `totalPrice` | long | A dynamically calculated total cost of all items in the cart |

### PaymentResponse
Returned after successfully initializing a checkout flow.
| Field | Type | Description |
| :--- | :--- | :--- |
| `status` | String | The status of the checkout request |
| `message` | String | Information regarding the session creation |
| `sessionId` | String | The unique Stripe session ID |
| `sessionUrl` | String | The URL the client should redirect to for the Stripe hosted checkout |
