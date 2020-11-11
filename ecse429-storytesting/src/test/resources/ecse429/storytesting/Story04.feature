@RemoveTask

Feature: Remove task from course to do list
  As a student
  I remove an unnecessary task from my course to do list
  So I can forget about it.


  Scenario Outline: Remove task from course to do list
    Given a course with title "gherkin101"
    And created tasks
      | title            | doneStatus | description          |
      | assignment1      | true       | my first assignment  |
      | assignment2      | true       | my second assignment |
      | project1         | true       | my first project     |
    And "assignment1" and "assignment2" are added to the course "gherkin101" todo list
    When I remove "<taskTitle>" from the course "gherkin101" todo list
    Then the returned statusCode is "<statusCode>"

    Examples:
      | taskTitle        | statusCode |
      | assignment1      | 200        |
      | assignment2      | 200        |
      | project1         | 404        |
