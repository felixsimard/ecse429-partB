Feature: Marking tasks as done
  As a student,
  I mark a task as done on my course to do list,
  so I can track my accomplishments

  Background:
#    Given the application is running
    Given a course with title "gherkin101"
    And created tasks
      | title            | doneStatus | description          |
      | assignment1      | false      | my first assignment  |
      | assignment2      | false      | my second assignment |
      | project1         | true       | my first project     |
    And "assignment1" and "assignment2" are added to the course "gherkin101" todo list

  # Normal Flow
  Scenario Outline: Marking a task as done - from the project API
    When I mark the task "<task_title>" to done
    Then the task "<task_title>" in the task list of the course "gherkin101" is marked as done
    Examples:
      | task_title   |
      | assignment1  |
      | assignment2  |

    # Alternate Flow
  Scenario Outline: Marking a task as done - from the task API
    When I add "gherkin101" to the the tasksof list of "<task_title>"
    When I mark the task "<task_title>" to done
    Then the task "<task_title>" in the task list of the course "gherkin101" is marked as done
    Examples:
      | task_title   |
      | assignment1  |
      | assignment2  |

  # Error Flow
  Scenario Outline: Marking deleted task as done
    Given I delete the task with title "<task_title>"
    When I mark the task "<task_title>" to done
    Then the returned status code is "<status_code>"
    Examples:
      | task_title    | status_code |
      | assignment1   | 404         |
      | assignment2   | 404         |