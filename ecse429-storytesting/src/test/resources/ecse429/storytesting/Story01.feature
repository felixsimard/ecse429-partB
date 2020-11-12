Feature: Task priority categorization
    As a student,
    I categorize tasks as HIGH, MEDIUM or LOW priority,
    so I can better manage my time.

    Background:
        Given the application is running


    Scenario Outline: Categorize task with a certain priority level
        Given a task with title "<task_title>"
        And a category with the "<existing_category_priority>"
        When I link the task to the "<category_priority>"
        Then the task is categorized with the "<resulting_priority>"
        Examples:
            | task_title | existing_category_priority | category_priority | resulting_priority |
            | ECSE 429   | HIGH                       | HIGH              | HIGH               |
            | ECSE 429   | MEDIUM                     | MEDIUM            | MEDIUM             |
            | ECSE 429   | HIGH                       | MEDIUM            | none               |


#    # Normal Flow
#    Scenario: Categorize task as HIGH priority
#        Given a task containing
#            | title            | completed | active | description              |
#            | ECSE 429 part B  | false     | true   | Complete individual part |
#        And a category containing
#            | title   | description    |
#            | HIGH    | High priority  |
#        When I link the above task to the <level> priority category
#        Then the task is categorized with the corresponding priority
#
#    # Alternate Flow
#    Scenario: Change the category of a task from HIGH to MEDIUM
#        Given a task containing
#            | title            | completed | active | description              |
#            | ECSE 429 part B  | false     | true   | Complete individual part |
#        And multiple categories containing
#            | title   | description     |
#            | HIGH    | High priority   |
#            | MEDIUM  | Medium priority |
#        And a link between the above task and a high priority category
#        When I link the above task to the <level> priority category
#        Then the task is categorized with the corresponding priority
#
#    # Error flow
#    Scenario: Categorize task with non existent category
#        Given a task containing
#            | title            | completed | active | description              |
#            | ECSE 429 part B  | false     | true   | Complete individual part |
#        And a category containing
#            | title        | description         |
#            | null         | null                |
#        When I link the above task to the <level> priority category
#        Then the task is categorized with the corresponding priority