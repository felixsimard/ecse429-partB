@Story04

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

# Normal Flow
  Scenario Outline: Remove task from course to do list - from project API
    When I remove "<taskTitle>" from the tasks list of "gherkin101" todo list
    Then the returned status code is "<statusCode>"
    And the relationship between "<taskTitle>" and the course "gherkin101" is destroyed

    Examples:
      | taskTitle        | statusCode |
      # Normal Flow
      | assignment1      | 200        |
      | assignment2      | 200        |

# Alternate Flow
  Scenario Outline: Remove task from course to do list - from todo API
    When I remove "gherkin101" from the tasksof list of "<taskTitle>"
    Then the returned status code is "<statusCode>"
    And the relationship between "<taskTitle>" and the course "gherkin101" is destroyed

  Examples:
    | taskTitle        | statusCode |
    | assignment2      | 200        |

# Error Flow
  Scenario Outline: Remove task from course list that it is not associated with
    When I remove "<taskTitle>" from the tasks list of "gherkin101" todo list
    Then the returned status code is "<statusCode>"
    And the error message is "<errorMessage>" with "<taskTitle>" and "gherkin101"

    Examples:
      | taskTitle | statusCode | errorMessage                                                      |
      | project1  | 404        | Could not find any instances with projects/projectId/tasks/taskId |