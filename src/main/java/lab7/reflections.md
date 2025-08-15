this lab used the DateService interface and dependency injection, so the 
searchRecipes don't depend in other data source. so the search logic and storage are 
seperated, letting polymorphism to make choices between
real services and mock data for the test. DataService has one method
and allows the program to quickly create test data using
a lambda.