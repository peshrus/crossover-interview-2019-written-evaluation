
# Project Assessment

Cross-Library is a web application for a local community library and is developed by a startup company. Cross-Library allows its community people to register as members. Librarian can issue books to a registered member only. Only one book can be issued in one transaction. As book issuance is possible only from the authorized terminal so no need to have separate authentication and authorization of librarian.

# Notes

* Each member should have a valid unique email address. No two members can have the same email address.
* Member is not allowed to issue a book which is already issued to someone and should return HTTP Status code 403.
* Member trying to issue a book which does not exist in our database, API should return HTTP Status code 404.
* After returning the book and completing the transaction by updating date of return, Any subsequent request to return for the same transaction-id should return HTTP Status Code 403. Valid value of Date Of Return field means books are returned.
* When issuing a book to a member, the application should update  date of issuance and on return, dateOfReturn should be recorded automatically
* Frontend application is out of our scope. It is a separate, fully-functioning application handled by another team, so we do not want to modify API signatures.

# Tasks

* Increase unit test coverage to reach 60%, achieving more than 60% will only consume your valuable time without an extra score.
* Cross-Library APIs are developed by the inexperienced developer and contains functional/logical bugs and lacks implementation for many important requirements mentioned above. You need to find those issues and fix them.
* Implement a new API which returns top 5 members who completed the maximum number of transactions(issued/returned books) within the given duration. Completed transaction means that date of issuance and date of return are within the search range. API should return member name, a number of books issued/returned in this duration. Signature of API is already defined in code and pagination is not needed.
* Implement another new feature where API should reject issuance of more than 5 books at a given time. If a member already has 5 books issued on his name, and try to issue another API should return HTTP Status code 403.
* Implement validation on name field in member table to allow names with the length of 2 to 100 and should always start with an alphabet. Please do not add validations on all other fields.

We'll be evaluating your submission from the following perspectives:
* Code quality and best practices for new code changes.
* Implementation of new features
* Bug fixes
* Unit Tests
    
Prerequisites: Any IDE(e.g Eclipse), Gradle , GIT, Java 8, MySQL 5.6+

# Development Environment:

Cross-Library applications require MySQL database to store its data. Make sure to update application.properties with spring.datasource.URL(change hostname only), spring.datasource.username, and  spring.datasource.password. You are free to choose MySQL service in a cloud or installed on the local machine or MySQL docker container.

The Cross-Library application uses liquibase for Database changes. In case you need to update the Database, please create a new changeset file in resources/db.changelog folder and include the newly created file in db.changelog-master.xml
    For more details on liquibase follow https://www.liquibase.org/documentation/changeset.html 
Cross-Library Application:
    To start the application run CrossLibraryApplication.java main method from your IDE.

#Production Environment:
This is how we are going to run and evaluate your submission, so please make sure to run below steps before submitting your answer.

* Make sure to run unit tests, check code coverage, ensure the application is compiling and all dependencies are included.
* Commit everything using (git add --all && git commit -m "My submission")
* Create patch file with name without spaces 'cross-library-java_<YourNameHere>.patch', using the specified tag as the starting point (git format-patch initial-commit --stdout > cross-library-java_<yournamehere>.patch)
* Store your file in a shared location where Crossover team can access and download it for evaluation. and add your sharable link in the answer field of this question.