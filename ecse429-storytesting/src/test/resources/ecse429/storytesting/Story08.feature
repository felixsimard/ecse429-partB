Feature: Query all incomplete HIGH priority tasks
  As a student,
  I query all incomplete HIGH priority tasks from all my classes,
  to identity my short-term goals.

#  Background:
#    Given the application is running

  Scenario Outline: Query all incomplete HIGH priority tasks

    Given a project with title "<class>"
    And an initial set, "<initial_set>", of tasks with value false for doneStatus, and have category HIGH priority
    When I query the incomplete tasks for this project with category HIGH priority
    Then a set, "<returned_set>", is returned which is identical to the initial set.

    Examples:
      | class      |   initial_set   |  returned_set   |
      | ECSE 429   | 3               | 3               |
      | ECSE 428   | 1               | 1               |
      | COMP 310   | 0               | 0               |