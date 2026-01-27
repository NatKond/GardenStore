Feature: User Management

  Scenario: Sign up with valid information
    Given I want to sing in with valid credentials: "alice.johnson@example.com" login and "12345" password
    When I submit login and password
    Then I receive response with JWT Token