@QueryIncompleteTasks

Feature: Query incomplete tasks
  As a student,
  I query the incomplete tasks for a class I am taking,
  to help manage my time.

#  Background:
#    Given the application is running

  # Normal FLow
  Scenario Outline: Query incomplete tasks

    Given a project with title "<class>"
    And an initial set of tasks connected to the project with "<num_initial_set_false>" tasks that have false as doneStatus value
    And another set of tasks connected to the project with "<num_initial_set_true>" tasks that have true as doneStatus value
    When I query the incomplete tasks for this project
    Then a set is returned which is identical to the initial set of tasks with value false for doneStatus and has "<num_returned_set>" elements.

    Examples:
      | class      |  num_initial_set_false   |  num_initial_set_true    |  num_returned_set   |
      | ECSE 429   | 2                        | 2                        |  2                  |
      | ECSE 428   | 1                        | 0                        |  1                  |
      | COMP 310   | 0                        | 1                        |  0                  |
      | COMP 310   | 0                        | 0                        |  0                  |

#  # Alternate Flow
#  Scenario Outline: Query incomplete tasks from non existant project
#
#    Given a non existent project title "<class>"
#    And an initial set of tasks connected to the project with "<num_initial_set_false>" tasks that have false as doneStatus value
#    And another set of tasks connected to the project with "<num_initial_set_true>" tasks that have true as doneStatus value
#    When I query the incomplete tasks for this project
#    Then an error message is returned
#
#    Examples:
#
  # Error Flow
  Scenario Outline: Query incomplete tasks from non existent project

    Given a non existent project title "<class>"
    When I query the incomplete tasks for this project
    Then an error message is returned

    Examples:
      | class      |
      | ECSE 427   |
      | ECSE 428   |
      | ECSE 429   |