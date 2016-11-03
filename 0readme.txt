Use Gradle v2.13 to test and run the demo app:
a) Unit tests
gradle clean test

Reports:
- unit tests: build/reports/test-ng/index.html
- code coverage: build/reports/jacoco-code-coverage/index.html

b) Acceptance/Integration tests
gradle clean testCucumber

Reports: build/reports/testCucumber/feature-overview.html

c) Run app
gradle clean run

Swagger API documentation then available at:
http://localhost:8080/swagger-ui.html