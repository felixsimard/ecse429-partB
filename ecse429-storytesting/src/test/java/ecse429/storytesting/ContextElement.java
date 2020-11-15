package ecse429.storytesting;


import ecse429.storytesting.model.Category;
import ecse429.storytesting.model.Project;
import ecse429.storytesting.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class ContextElement {

    public int id;
    public Todo todo;
    public Project project;
    public Category category;
    public ElementType type;

    public enum ElementType {
        TODO, CATEGORY, PROJECT, OTHER
    }

    public ContextElement(int id, ElementType type) {
        this.id = id;
        this.type = type;
    }
}
