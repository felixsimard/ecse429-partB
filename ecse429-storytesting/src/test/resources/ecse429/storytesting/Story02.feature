@Story02

Feature: Adding task to courses
  As a student,
  I add a task to a course to do list,
  so I can remember it.

  Background:
    Given a course with title "gherkin101"

    # Normal Flow
    Scenario Outline: Adding task to course - from the project API
      Given a task with title "<task_title>"
      When I add the task "<task_title>" to the task list of the course "gherkin101"
      Then the task "<task_title>" is in the task list of the course "gherkin101"
      Examples:
        | task_title       |
        | Study Final      |
        | Study Midterm 1  |
        | Study Midterm 2  |

    # Alternate Flow
    Scenario Outline: Adding task to course - from the task API
      Given a task with title "<task_title>"
      When I add "gherkin101" to the the tasksof list of "<task_title>"
      Then the task "<task_title>" is in the task list of the course "gherkin101"
      Examples:
        | task_title       |
        | Study Final      |
        | Study Midterm 1  |
        | Study Midterm 2  |

    # Error Flow
    Scenario Outline: Adding deleted task to course
      Given a task with title "<task_title>"
      And I delete the task with title "<task_title>"
      When I add the task "<task_title>" to the task list of the course "gherkin101"
      Then the returned status code is "<status_code>"
      Then the task "<task_title>" is not in the task list of the course "gherkin101"
      Examples:
        | task_title       | status_code |
        | Study Final      | 404         |
        | Study Midterm 1  | 404         |
        | Study Midterm 2  | 404         |