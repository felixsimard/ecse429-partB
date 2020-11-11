Feature: Task priority categorization
    As a student,
    I categorize tasks as HIGH, MEDIUM or LOW priority,
    so I can better manage my time.

    # Normal Flow
    Scenario: Categorize task as HIGH priority
        Given a task containing
            | title            | completed | active | description              |
            | ECSE 429 part B  | false     | true   | Complete individual part |
        And a category containing
            | title   | description    |
            | HIGH    | High priority  |
        When I link the above task to the <level> priority category
        Then the task is categorized with the corresponding priority

    # Alternate Flow
    Scenario: Change the category of a task from HIGH to MEDIUM
        Given a task containing
            | title            | completed | active | description              |
            | ECSE 429 part B  | false     | true   | Complete individual part |
        And multiple categories containing
            | title   | description     |
            | HIGH    | High priority   |
            | MEDIUM  | Medium priority |
        And a link between the above task and a high priority category
        When I link the above task to the <level> priority category
        Then the task is categorized with the corresponding priority

    # Error flow
    Scenario: Categorize task with non existent category
        Given a task containing
            | title            | completed | active | description              |
            | ECSE 429 part B  | false     | true   | Complete individual part |
        And a category containing
            | title        | description         |
            | null         | null                |
        When I link the above task to the <level> priority category
        Then the task is categorized with the corresponding priority