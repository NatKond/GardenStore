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
| Order     | Manages customer orders         | [Orders.md](src/main/docs/Order.md)        |
| OrderItem | Stores order items details      | [Orders.md](src/main/docs/OrderItem.md)    |

**Tech Stack**

| Technology            | Purpose                                     |
|-----------------------|---------------------------------------------|
| Java 21               | Programming language                        |
| Spring Boot 3.5.3     | Application framework                       |
| Hibernate             | ORM framework                               |
| PostgreSQL            | Database                                    |
| H2 Database           | In-memory DB for testing                    |
| ModelMapper           | Object mapping (DTO ↔ Entity)               |
| Lombok                | Code reduction (getters, setters, builders) |
| Liquibase             | Database initialisation                     |
| Spring Security + JWT | Authentication & authorization              |
| Swagger / OpenAPI     | REST API documentation                      |
| JUnit + Mockito       | Testing                                     |
| Docker                | Deployment & containerization               |
| Git + GitHub          | Version control                             |

# Team

## Natalia Kondratenko
Team Leader
  
*Contributions:*
* Created AppUser entity, created request and response user DTOs and implemented `UserRepository`, `UserService`, `UserController`
* Added Liquibase for database initialization
* Configured ModelMapper in custom converters
* Created unified response handling (`ApiResponse`) and global exception handler
* Created request and response order DTOs, implemented `OrderController`
* Set up authentication with Spring Security + JWT
* Created `ReportController` and `ReportService`
* Integrated Swagger (OpenAPI) for API documentation
* Implemented order status updates via Scheduler
* Added JaCoCo for creating test coverage reports
* Deployed the project


## Alexandra Kriviz
Developer

*Contributions:*
* Created Product entity, created request and response product DTOs and implemented `ProductRepository`, `ProductService`, `ProductController`
* Wrote unit tests  for `ProductService`
* Refactored method names in services and controllers to remove entity names
* Standardized code across similar methods in different classes
* Created Cart, CartItem entities and implemented `CartRepository`, `CartService`
* Wrote unit tests for `CartController`
* Added integration tests for all parameters in the getAll method in ProductIntegrationTest
* Verified that all API endpoints comply with the project documentation


## Mariia Shirokova
Developer

*Contributions:*
* Created Favorite entity, created response favorite DTOs and implemented `FavoriteRepository`, `FavoriteService`, `FavoriteController`
* Wrote unit tests for `FavoriteService` and `FavoriteController`
* Implemented Favorite API: add, delete, and get by user
* Defined JPA relationships between User, Product, Favorite, and Category entities
* Created request and response DTOs and converters, implemented CartController
* Wrote unit tests for `CartService`
* Implemented `PaymentController` to handle order payments
* Added unit tests for `OrderItemService`
* Added unit tests to cover all branches in CartServiceImpl.addItem method
* Added logging to the project in all services


## Dzmitry Rabtsevitch
Developer

*Contributions:*
* Created Category entity, created request and response category DTOs and implemented `CategoryRepository`, `CategoryService`, `CategoryController`
* Wrote unit tests for `ProductController`
* Added abstract test class to store test data
* Created Order, OrderItem entities and implemented `OrderRepository` and `OrderService`
* Wrote unit tests for `OrderController` and `OrderService`
* Added discount and promo functionality to `ProductController` and `ProductService`
* Wrote unit tests for `ScheduledService`
* Wrote unit tests for setDiscount and getProductOfTheDay methods in `ProductService` and `ProductController`
* Implemented logging of input data using Aspect-Oriented Programming (AOP)