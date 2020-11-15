@Story05

Feature: Create a to do list
  As a student
  I create a to do todo list for a new class I am taking
  so I can manage course work

#  Background:
#    Given the application is running

  #Normal and Alternate Flow
    Scenario Outline: Create to do list
      When I create a new to do list with title "<title>", completed status "<completed>", active status "<active>", and description "<description>"
      Then the returned status code is "<statusCode>"
      And "<title>" is created accordingly

      Examples:
        | title           | completed        | active        | description        | statusCode |
        #Normal Flow
        | gherkin101Todo  | false            | true          | get this done      | 201        |
        #Alternate Flow
        |                 |                  |               |                    | 201        |

  #Error Flow
    Scenario Outline: Create a to do list with invalid field
      When I create a new to do list with title "<title>", completed status "<completed>", active status "<active>", and description "<description>"
      Then the returned status code is "<statusCode>"
      And the returned error message is "<errorMessage>"

      Examples:
        | title           | completed        | active        | description        | statusCode | errorMessage                                   |
        | testing202      | no               |               |                    | 400        | Failed Validation: completed should be BOOLEAN |
