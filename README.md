# Garden Store

_A graduation project by **CODE_CATS** team_

![CODE_CATS Banner](src/main/docs/logo.png)

## About the Project

**Links:**

- [Documentation (Project requirements)](https://docs.google.com/document/d/1Xn41eFhdYAJVYzRucsNwpbLJ5lNxdvpfx__SZf5DwXA/edit?tab=t.0)
- [REST API Endpoints](https://confirmed-baron-2e5.notion.site/REST-API-f186cf63a46c4020b2237f73093922ab)
- [JIRA (Backlog & Tasks)](https://natzubova.atlassian.net/jira/software/projects/GSP/boards/1)
- [Deployed Version](http://51.20.105.119:8080/swagger-ui/index.html#/)

**Description:**  
This graduation project is a backend application built with **Java Spring Boot** for an online store specializing in home and garden products.  
The application allows customers to browse the catalog, add items to the cart, place orders, and track their order status in real time.  
For administrators it provides tools to manage the product catalog, promotions, and sales analytics.

**Main Page Screenshot**

![Main Page Screenshot](src/main/docs/main-page.png)

**Core Features**

- 👤 **User Account Management** – registration, authentication, and profile handling
- 🛠️ **Product Catalog Management** (for administrators) – add, edit, and remove products
- 🔎 **Advanced Product Filtering and Sorting** – improved search and navigation experience
- ⭐ **Favorites System** – users can add items to favorites and view a personalized list
- 🛒 **Shopping Cart** – add products to the cart and manage cart contents
- 📦 **Order Management** – order placement, real-time status tracking
- 💳 **Mock Payment System** – order payments
- 🎁 **Promotions & Discounts** – create and manage special offers and discounts
- 📊 **Reporting & Analytics** – generate sales reports


**Entities Overview**

| Entity    | Description                     | Details                                    |
|-----------|---------------------------------|--------------------------------------------|
| User      | Manages user accounts           | [Users.md](src/main/docs/User.md)          |
| Category  | Groups products into categories | [Categories.md](src/main/docs/Category.md) |
| Product   | Stores product details          | [Products.md](src/main/docs/Product.md)    |
| Favorite  | Manages user favorite products  | [Favorites.md](src/main/docs/Favorite.md)  |
| Cart      | Shopping cart with items        | [Cart.md](src/main/docs/Cart.md)           |
| CartItem  | Stores cart items details       | [Cart.md](src/main/docs/CartItem.md)       |
| Order     | Customer orders and reports     | [Orders.md](src/main/docs/Order.md)        |
| OrderItem | Stores order items details      | [Orders.md](src/main/docs/OrderItem.md)    |

**Tech Stack**

| Technology            | Purpose                                       |
|-----------------------|-----------------------------------------------|
| Java 21               | Programming language                          |
| Spring Boot 3.5.3     | Application framework                         |
| Hibernate             | ORM framework                                 |
| PostgreSQL            | Database                                      |
| H2 Database           | In-memory DB for testing                      |
| ModelMapper           | Object mapping (DTO ↔ Entity)                 |
| Lombok                | Code reduction (getters, setters, builders)   |
| Liquibase             | Database versioning and migration management) |
| Spring Security + JWT | Authentication & authorization                |
| Swagger / OpenAPI     | REST API documentation                        |
| JUnit + Mockito       | Testing                                       |
| Docker                | Deployment & containerization                 |
| Git + GitHub          | Version control                               |

# Team CODE_CATS

## Natalia Kondratenko
  **Role** Team Leader
  *Contributions:*
1. [x] Created AppUser entity, created request and response user DTOs and implemented `UserRepository`, `UserService`, `UserController`
2. [x] Added Liquibase for database initialization
3. [x] Configured ModelMapper in custom converters
4. [x] Created unified response handling (`ApiResponse`) and global exception handler
5. [x] Created request and response order DTOs, implemented `OrderController`
6. [x] Set up authentication with Spring Security + JWT
7. [x] Created `ReportController` and `ReportService`
8. [x] Integrated Swagger (OpenAPI) for API documentation
9. [x] Implemented order status updates via Scheduler
10. [x] Added JaCoCo for creating test coverage reports
11. [x] Deployed the project


## Alexendra Kriviz
  **Role** Developer
  *Contributions:*
1. [x] Created Product entity, created request and response product DTOs and implemented `ProductRepository`, `ProductService`, `ProductController`
2. [x] Wrote unit tests  for `ProductService`
3. [x] Refactored method names in services and controllers to remove entity names
4. [x] Standardized code across similar methods in different classes
5. [x] Created Cart, CartItem entities and implemented `CartRepository`, `CartService`
6. [x] Wrote unit tests for `CartController`
7. [x] Added integration tests for all parameters in the getAll method in ProductIntegrationTest
8. [x] Verified that all API endpoints comply with the project documentation


## Maria Shirokova
  **Role** Developer
  *Contributions:*
1. [x] Created Favorite entity, created response favorite DTOs and implemented `FavoriteRepository`, `FavoriteService`, `FavoriteController`
2. [x] Wrote unit tests for `FavoriteService` and `FavoriteController`
3. [x] Implement Favorite API: add, delete, and get by user
4. [x] Defined JPA relationships between User, Product, Favorite, and Category entities
5. [x] Created request and response DTOs and converters, implemented CartController
6. [x] Write unit tests for `CartService`
7. [x] Implemented `PaymentController` to handle order payments
8. [x] Add unit tests for `OrderItemService`
9. [x] Add unit tests to cover all branches in CartServiceImpl.addItem method
10. [x] Add logging to the project in all services


## Dmitrij Rabtsevitch
  **Role** Developer
  *Contributions:*
1. [x] Created Category entity, created request and response category DTOs and implemented `CategoryRepository`, `CategoryService`, `CategoryController`
2. [x] Wrote unit tests for `ProductController`
3. [x] Added abstract test class to store test data
4. [x] Created Order, OrderItem entities and implemented `OrderRepository` and `OrderService`
5. [x] Wrote unit tests for `OrderController` and `OrderService`
6. [x] Added discount and promo functionality to `ProductController` and `ProductService`
7. [x] Wrote unit tests for `ScheduledService`
8. [x] Wrote unit tests for setDiscount and getProductOfTheDay methods in `ProductService` and `ProductController`
9. [x] Implement logging of input data using Aspect-Oriented Programming (AOP)