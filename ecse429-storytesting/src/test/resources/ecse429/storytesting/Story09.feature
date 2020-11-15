Feature: Adjust priority of a task
  As a student,
  I want to adjust the priority of a task,
  to help better manage my time.

#  Background:
#    Given the application is running

  # Normal Flow and Alternate Flows (represented in examples)
  Scenario Outline: Adjust priority of a task

    Given a task with a priority "<initial_priority>"
    And a category called "<new_priority>"
    When I adjust the priority of the task to "<new_priority>"
    Then the task is categorized with the "<new_priority>" and the old link is removed

    Examples:
      | initial_priority | new_priority     |
      | HIGH             | LOW              |
      | MEDIUM           | HIGH             |
      | LOW              | MEDIUM           |
      | LOW              | LOW              |

  # Error Flow
  Scenario Outline: Adjust priority of a non existing task

    Given a task with a priority "<initial_priority>"
    And a category called "<new_priority>"
    When I adjust the priority of the task to "<new_priority>"
    Then the task is categorized with the "<new_priority>" and the old link is removed

    Examples:
      | initial_priority | new_priority     |
      | HIGH             | LOW              |
      | MEDIUM           | HIGH             |
      | LOW              | MEDIUM           |
      | LOW              | LOW              |