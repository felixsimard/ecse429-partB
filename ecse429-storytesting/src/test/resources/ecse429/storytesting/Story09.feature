Feature: Adjust priority of a task
  As a student,
  I want to adjust the priority of a task,
  to help better manage my time.

  Background:
    Given the application is running

  Scenario Outline: Adjust priority of a task

    Given a task with a priority "<initial_priority>"
    And a category with the "<existing_category_priority>"
    When I link the task to the "<new_priority>"
    Then the task is categorized with the "<resulting_priority>"

    Examples:
      | initial_priority | existing_category_priority | new_priority     | resulting_priority |
      | HIGH             | LOW                        | LOW              | LOW                |
      | MEDIUM           | HIGH                       | HIGH             | HIGH               |
      | LOW              | MEDIUM                     | MEDIUM           | MEDIUM             |