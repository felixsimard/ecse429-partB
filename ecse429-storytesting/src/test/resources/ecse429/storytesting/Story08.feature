@QueryIncompleteHighPriorityTasks

Feature: Query all incomplete HIGH priority tasks
  As a student,
  I query all incomplete HIGH priority tasks from all my classes,
  to identity my short-term goals.

#  Background:
#    Given the application is running

  # Normal Flow
  Scenario Outline: Query all incomplete HIGH priority tasks

    Given a project with title "<class>"
    And a category with the title HIGH
    And an initial set of tasks connected to the project that have false as doneStatus value, and are connected to the HIGH category
    And an initial set of tasks connected to the project that have false as doneStatus value, and are not connected to the HIGH category
    When I query the incomplete tasks for this project with category HIGH priority and doneStatus=false
    Then a set is returned that is identical to the initial tasks with doneStatus=false and are connected to the "<existing_priority>"

    Examples:
      | class      |   existing_priority   |
      | ECSE 429   | HIGH                  |
      | ECSE 428   | MEDIUM                |
      | COMP 310   | LOW                   |