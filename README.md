# 🗂️ Trello REST API — Automated Testing Framework

> **ITI Graduation Project** — A BDD-based API test automation framework for the [Trello REST API](https://developer.atlassian.com/cloud/trello/rest/), built with **Rest Assured**, **Cucumber**, and **JUnit**.

---

## 📌 Overview

This project provides end-to-end automated testing for the Trello REST API, covering full **CRUD** (Create, Read, Update, Delete) operations on the four core Trello resources:

| Resource      | Create | Read | Update | Delete / Archive | Negative | E2E Lifecycle |
|:--------------|:------:|:----:|:------:|:----------------:|:--------:|:-------------:|
| **Board**     | ✅     | ✅   | ✅     | ✅               | ✅       | ✅            |
| **List**      | ✅     | ✅   | ✅     | ✅ (Archive)     | ✅       | ✅            |
| **Card**      | ✅     | ✅   | ✅     | ✅               | ✅       | ✅            |
| **Checklist** | ✅     | ✅   | ✅     | ✅               | ✅       | ✅            |

Each resource has its own `.feature` file written in **Gherkin** syntax, making tests readable and maintainable.

---

## 🛠️ Tech Stack

| Technology                  | Version  | Purpose                                    |
|:----------------------------|:---------|:-------------------------------------------|
| **Java**                    | 21       | Programming language                       |
| **Maven**                   | —        | Build & dependency management              |
| **Rest Assured**            | 5.5.6    | HTTP client for API testing                |
| **Cucumber (Java + JUnit)** | 7.31.0   | BDD framework (Gherkin feature files)      |
| **JUnit 4**                 | —        | Test runner                                |
| **Log4j 2**                 | 2.25.2   | Structured logging (via SLF4J bridge)      |
| **Jackson Databind**        | 2.20.1   | JSON serialization / deserialization        |
| **Cucumber Reporting**      | 5.0.0    | HTML report generation from JSON results   |

---

## 📁 Project Structure

```
TrelloRestAssured/
├── pom.xml                          # Maven config & dependencies
├── src/test/java/
│   ├── features/                    # Gherkin feature files
│   │   ├── Board.feature            # Board CRUD scenarios
│   │   ├── List.feature             # List CRUD scenarios
│   │   ├── Card.feature             # Card CRUD scenarios
│   │   ├── Checklist.feature        # Checklist CRUD scenarios
│   │   └── CleanAll.feature         # Utility: delete all boards
│   ├── stepDefinitions/
│   │   ├── StepDefinition.java      # Cucumber step implementations
│   │   └── Hooks.java               # @AfterAll cleanup hook
│   ├── resources/
│   │   ├── Utils.java               # Request spec builder & helpers
│   │   ├── APIResources.java        # Enum mapping for API endpoints
│   │   ├── TestDataBuild.java       # Test data builder (extensible)
│   │   └── global.properties        # Base URL, API key & token
│   ├── logs/
│   │   └── LogsManager.java         # Centralized Log4j 2 wrapper
│   └── Options/
│       └── TestRunner.java          # Cucumber-JUnit test runner
├── src/test/resources/
│   └── log4j2.properties            # Log4j 2 configuration
└── target/
    └── jsonReports/                 # Cucumber JSON reports (generated)
```

---

## ⚙️ Key Components

### 🔹 Feature Files (`features/`)
Written in **Gherkin** (`.feature`) , each file covers a Trello resource with five scenario types:
- **Create** — Creates a resource and verifies it is persisted via a GET call.
- **Update** — Updates the resource name and verifies the change.
- **Delete** — Deletes (or archives) the resource and verifies a `404` on re-fetch.
- **E2E Lifecycle** — Runs the full Create → Update → Delete flow in a single scenario.
- **Negative** — Sends an empty payload and asserts a `400` error response.

### 🔹 Step Definitions (`StepDefinition.java`)
Implements all Cucumber steps using Rest Assured. Key design decisions:
- **Dynamic resource routing** — A single set of parameterized steps (e.g., `I create a "{type}" with name "{name}"`) handles all four resource types.
- **Parent-child linking** — Automatically attaches parent IDs (e.g., `idBoard` for lists, `idList` for cards) when creating child resources.
- **State management** — Static variables track the current active entity ID for each resource type.

### 🔹 Utils (`Utils.java`)
Builds a reusable `RequestSpecification` with:
- Base URI from `global.properties`
- API key & token authentication
- Content-Type `application/json`
- Request/response logging filters

### 🔹 API Resources Enum (`APIResources.java`)
Maps resource types to their API paths:
```java
BOARD("/boards"), LIST("/lists"), CARD("/cards"), CHECKLIST("/checklists")
```
Provides helper methods: `getResource()`, `getResourceWithId()`, `getResourceArchive()`.

### 🔹 Hooks (`Hooks.java`)
Runs an `@AfterAll` hook that deletes all boards after the test suite completes, ensuring a clean Trello workspace.

### 🔹 Logs Manager (`LogsManager.java`)
A centralized logging utility wrapping Log4j 2 with convenience methods (`info`, `warn`, `error`, `debug`, `fatal`) that auto-detect the calling class.

---

## 🚀 Getting Started

### Prerequisites
- **Java 21** (or later)
- **Maven 3.x**
- A **Trello account** with an API key and token ([Generate here](https://trello.com/power-ups/admin))

### 1. Clone the Repository
```bash
git clone https://github.com/Omar-Abdelaziz-Fahmey/TrelloRestAssured.git
cd TrelloRestAssured
```

### 2. Configure API Credentials
Edit `src/test/java/resources/global.properties`:
```properties
baseUrl = https://api.trello.com/1
key    = YOUR_TRELLO_API_KEY
token  = YOUR_TRELLO_API_TOKEN
```

> ⚠️ **Never commit real API keys or tokens to a public repository.** Use environment variables or a `.gitignore`'d file for production setups.

### 3. Run All Regression Tests
```bash
mvn test
```
This runs the `TestRunner` class, which executes all scenarios tagged `@Regression`.

### 4. Run a Specific Feature
Use Cucumber tag filters:
```bash
mvn test -Dcucumber.filter.tags="@BoardCRUD"      # Board tests only
mvn test -Dcucumber.filter.tags="@CardCRUD"        # Card tests only
mvn test -Dcucumber.filter.tags="@ListCRUD"        # List tests only
mvn test -Dcucumber.filter.tags="@ChecklistCRUD"   # Checklist tests only
mvn test -Dcucumber.filter.tags="@CleanUp"         # Clean all boards
```

### 5. Generate HTML Report
```bash
mvn verify
```
The Cucumber HTML report is generated in `target/cucumber-html-reports/`.

---

## 🏷️ Test Tags Reference

| Tag                      | Scope                              |
|:-------------------------|:-----------------------------------|
| `@Regression`            | All test scenarios                 |
| `@BoardCRUD`             | All Board scenarios                |
| `@ListCRUD`              | All List scenarios                 |
| `@CardCRUD`              | All Card scenarios                 |
| `@ChecklistCRUD`         | All Checklist scenarios            |
| `@CreateBoard` / `@CreateList` / `@CreateCard` / `@CreateChecklist` | Individual Create tests |
| `@UpdateBoard` / `@UpdateList` / `@UpdateCard` / `@UpdateChecklist` | Individual Update tests |
| `@DeleteBoard` / `@DeleteList` / `@DeleteCard` / `@DeleteChecklist` | Individual Delete tests |
| `@E2EBoardLifecycle` / `@E2EListLifecycle` / `@E2ECardLifecycle` / `@E2EChecklistLifecycle` | End-to-end lifecycle tests |
| `@Negative`              | Negative / error-handling tests    |
| `@CleanUp`               | Delete all boards utility          |

---

## 📊 Reporting

- **Console** — Cucumber `pretty` plugin provides colored console output.
- **JSON** — Raw results saved to `target/jsonReports/e2e-report.json`.
- **HTML** — Rich HTML report generated via `maven-cucumber-reporting` plugin after `mvn verify`.
- **Logs** — Detailed request/response logs written to `logging.txt` and structured application logs via Log4j 2.

---

## 👤 Author

**Omar Abdelaziz Fahmey**

- ITI Graduation Project
- [GitHub](https://github.com/Omar-Abdelaziz-Fahmey)
