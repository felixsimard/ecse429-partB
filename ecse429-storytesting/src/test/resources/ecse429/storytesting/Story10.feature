@ChangeTaskDescription
Feature: Changing task description
  As a student,
  I want to change a task description,
  to better represent the work to do.

#	Background:
#		Given the application is running

  # Normal Flow
  Scenario Outline: Change description of a current task to a new one
    Given a task with description "<current_task_description>"
    When I update the task description to "<new_task_description>"
    Then the task has description "<resulting_task_description>"
    Examples:
      | current_task_description | new_task_description      | resulting_task_description |
      | clean the front windows  | clean the back windows    | clean the back windows     |
      | watch lecture            | watch ecse429 lecture     | watch ecse429 lecture      |
      | pickup sister            | pickup sister from soccer | pickup sister from soccer  |

  # Alternate Flow (updating description with the same description or an empty one)
  Scenario Outline: Change description of a current task to a new one
    Given a task with description "<current_task_description>"
    When I update the task description to "<new_task_description>"
    Then the task has description "<resulting_task_description>"
    Examples:
      | current_task_description | new_task_description    | resulting_task_description |
      | clean the front windows  | clean the front windows | clean the front windows    |
      | watch lecture            |                         |                            |

  # Error Flow
  Scenario Outline: Change description of a non existent task
    Given a task with description "<current_task_description>"
    When I update the task "<non_existent_task_id>" with description "<other_task_description>"
    Then the returned statusCode is "<status_code>"
    Examples:
      | current_task_description | non_existent_task_id | other_task_description | status_code |
      | clean the front windows  | 10                   | new description        | 400         |
      | watch lecture            | 12                   | new description        | 400         |
      | pickup sister            | 8                    | new description        | 400         |