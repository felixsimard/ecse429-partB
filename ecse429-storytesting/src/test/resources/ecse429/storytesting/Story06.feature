@RemoveTodoList
Feature: Remove course to do list
  As a student
  I remove a to do list for a class whichI am no longer taking
  to declutter my schedule.

  Background:
    Given the application is running

    Scenario Outline: Remove course to do list
      Given created course to do lists
        | title       | completed       | active       | description    |
        |

      When I remove