Feature: Task priority categorization
    As a student,
    I categorize tasks as HIGH, MEDIUM or LOW priority,
    so I can better manage my time.

    # Normal Flow
    Scenario Outline: Categorize task as HIGH priority
        Given a task containing
            | title            | completed | active | description              |
            | ECSE 429 part B  | false     | true   | Complete individual part |
        And a category containing
            | title   | description    |
            | HIGH    | High priority  |
        When I link the above task to the above category
        Then the task is categorized with the corresponding priority

        Examples:
            |  |