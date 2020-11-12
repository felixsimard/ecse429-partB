@ChangeTaskDescription
Feature: Changing task description
		As a student, 
		I want to change a task description, 
		to better represent the work to do.

	Background:
		Given the application is running
		
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