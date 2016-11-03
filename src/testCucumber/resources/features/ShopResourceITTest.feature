@restApiIntegration
Feature: Shop Resource Integration Test

  Scenario: Add shop
    When client requests POST /api/shops with json data:
    """
    {
      "shopName": "Shop1",
      "shopAddress": {
          "number": "241 Old St",
          "postCode": "EC1V 9EY"
      }
    }
    """
    Then response code should be 201
    And header "Location" should be present with value "http://localhost/api/shops/Shop1"

  Scenario: Find nearest shop
    When client requests POST /api/shops with json data:
    """
    {
      "shopName": "Shop2",
      "shopAddress": {
          "number": "49 City Rd",
          "postCode": "EC1Y 1AU"
      }
    }
    """
    Then response code should be 201
    And client requests POST /api/shops with json data:
    """
    {
      "shopName": "Shop3",
      "shopAddress": {
          "number": "111 New Bond St",
          "postCode": "W1S 1DP"
      }
    }
    """
    Then response code should be 201
    And client requests GET /api/shops?customerLongitude=-0.14&customerLatitude=51.5
    Then response code should be 200
    And result json should be:
    """
    {
      "shopName": "Shop3",
      "shopAddress": {
          "number": "111 New Bond St",
          "postCode": "W1S 1DP"
      },
      "shopLongitude": -0.1456238,
      "shopLatitude": 51.5130642
    }
    """