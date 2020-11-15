@QueryIncompleteHighPriorityTasks

Feature: Query all incomplete HIGH priority tasks
  As a student,
  I query all incomplete HIGH priority tasks from all my classes,
  to identity my short-term goals.

#  Background:
#    Given the application is running

  # Normal Flow and Alternate Flows (Represented in examples)
  Scenario Outline: Query all incomplete HIGH priority tasks

    Given "<num_projects>" projects exists
    And a category exists with the title HIGH
    And an initial set of tasks connected to any of the projects with "<num_initial_HIGH>" tasks that have false as doneStatus value, and are connected to the HIGH category
    And another set of tasks connected to any of the projects with "<num_initial_none>" tasks that are not connected to the HIGH category
    When I query the incomplete tasks with category HIGH priority and doneStatus=false
    Then a set is returned that is identical to the initial tasks with doneStatus=false and are connected to the HIGH priority and has "<num_returned_set>" elements.

    Examples:
      | num_projects  |  num_initial_HIGH        |  num_initial_none        |  num_returned_set   |
      | 1             | 2                        | 2                        |  2                  |
      | 2             | 1                        | 0                        |  1                  |
      | 3             | 0                        | 1                        |  0                  |
      | 0             | 0                        | 0                        |  0                  |

  # Error Flow
  Scenario Outline: Query incomplete tasks from non existent project

    Given "<num_projects>" projects exists
    And a non existent category HIGH
    And an initial set of tasks connected to any of the projects
    When I query the incomplete tasks with category HIGH priority and doneStatus=false
    Then an error message is returned

    Examples:
      | num_projects    |
      | 2               |
      | 0               |