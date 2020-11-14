@RemoveTask

Feature: Remove task from course to do list
  As a student
  I remove an unnecessary task from my course to do list
  So I can forget about it.

  Background:
#    Given the application is running
    Given a course with title "gherkin101"
    And created tasks
      | title            | doneStatus | description          |
      | assignment1      | true       | my first assignment  |
      | assignment2      | true       | my second assignment |
      | project1         | true       | my first project     |
    And "assignment1" and "assignment2" are added to the course "gherkin101" todo list

# Normal and Error Flow Outline
  Scenario Outline: Remove task from course to do list - from project API
    When I remove "<taskTitle>" from the tasks list of "gherkin101" todo list
    Then the returned status code is "<statusCode>"
    And the relationship between "<taskTitle>" and the course "gherkin101" is destroyed

    Examples:
      | taskTitle        | statusCode |
      # Normal Flow
      | assignment1      | 200        |
      # Error Flow
      | project1         | 404        |

# Alternate Flow Outline
  Scenario Outline: Remove task from course to do list - from todo API
    When I remove "gherkin101" from the tasksof list of "<taskTitle>"
    Then the returned status code is "<statusCode>"
    And the relationship between "<taskTitle>" and the course "gherkin101" is destroyed

  Examples:
    | taskTitle        | statusCode |
    | assignment2      | 200        |