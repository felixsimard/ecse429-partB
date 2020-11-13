Feature: Task priority categorization
    As a student,
    I categorize tasks as HIGH, MEDIUM or LOW priority,
    so I can better manage my time.

    Background:
        Given a task with title "ECSE429"

    # Normal flow
    Scenario Outline: Categorize task with a certain priority level
        Given a category with the title "<existing_priority>"
        When I link the task to the category with title "<category_priority>"
        Then the task is categorized with the title "<category_priority>"
        Examples:
            | existing_priority | category_priority |
            | HIGH              | HIGH              |
            | MEDIUM            | MEDIUM            |
            | LOW               | LOW               |

    # Alternative Flow
    Scenario Outline: Change category task with a certain priority level
        Given a category with the title "<priority1>"
        And a category with the title "<priority2>"
        When I link the task to the category with title "<priority1>"
        And I link the task to the category with title "<priority1>"
        Then the task is categorized with the title "<priority2>"
        Examples:
            | priority1 | priority2 |
            | HIGH      | MEDIUM    |
            | MEDIUM    | LOW       |
            | LOW       | HIGH      |

    # Error Flow
    Scenario Outline: Categorize task with a non existent priority level
        Given a category with the title "<priority1>"
        When I link the task to the category with title "<priority2>"
        Then the returned statusCode is "<status_code>"
        Examples:
            | priority1 | priority2 | status_code |
            | HIGH      | MEDIUM    | 400         |
            | MEDIUM    | LOW       | 400         |
            | LOW       | HIGH      | 400         |