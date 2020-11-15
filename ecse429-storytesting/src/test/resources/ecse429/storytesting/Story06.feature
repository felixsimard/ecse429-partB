@Story06

Feature: Remove course to do list
  As a student
  I remove a to do list for a class whichI am no longer taking
  to declutter my schedule.

  Background:
    Given created course to do lists
      | title           | completed       | active       | description    |
      | gherkin101Todo  | true            | true         | get this done  |
      | gherkin202Todo  | true            | true         | this is empty  |
    And created tasks
      | title            | doneStatus | description          |
      | assignment1      | true       | my first assignment  |
      | assignment2      | true       | my second assignment |
      | project1         | true       | my first project     |
    And "assignment1" and "assignment2" are added to the course "gherkin101Todo" todo list

#Normal Flow
  Scenario Outline: Remove non empty course to do list
    When I delete the "<title>" to do list
    Then the returned status code is "<statusCode>"
    And the course "<title>" is no longer in the database
    And no tasks are linked to "<title>"

    Examples:
      | title          | statusCode |
      | gherkin101Todo | 200        |

#Alternate Flow
  Scenario Outline: Remove empty course to do list
    When I delete the "<title>" to do list
    Then the returned status code is "<statusCode>"
    And the course "<title>" is no longer in the database

    Examples:
      | title          | statusCode |
      | gherkin202Todo | 200        |

#Error Flow
  Scenario Outline: Remove non existent course to do list
    When I delete a non existent course with id "<courseId>"
    Then the returned status code is "<statusCode>"
    And the returned error message is "<errorMessage>"

    Examples:
      | courseId | statusCode | errorMessage                                    |
      | 1000     | 404        | Could not find any instances with projects/1000 |



