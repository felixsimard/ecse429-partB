@CreateTodoList
Feature: Create a to do list
  As a student
  I create a to do list for a new class I am taking
  so I can manage course work

#  Background:
#    Given the application is running

    Scenario Outline: Create to do list
      When I create a new to do list with title "<title>", completed status "<completed>", active status "<active>", and description "<description>"
      Then the returned status code is "<statusCode>"

      Examples:
        | title           | completed        | active        | description        | statusCode |
        | gherkin101Todo  | false            | true          | get this done      | 201        |
        |                 |                  |               |                    | 201        |