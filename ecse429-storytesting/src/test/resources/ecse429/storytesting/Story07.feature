Feature: Query incomplete tasks
  As a student,
  I query the incomplete tasks for a class I am taking,
  to help manage my time.

  Background:
    Given the application is running

  Scenario Outline: Query incomplete tasks

    Given a project with title "<class>"
    And an initial set, "<initial_set>", of tasks with value false for doneStatus
    When I query the incomplete tasks for this project
    Then a set, "<returned_set>", is returned which is identical to the initial set.

    Examples:
      | class      |   initial_set   |  returned_set   |
      | ECSE 429   | 3               | 3               |
      | ECSE 428   | 1               | 1               |
      | COMP 310   | 0               | 0               |