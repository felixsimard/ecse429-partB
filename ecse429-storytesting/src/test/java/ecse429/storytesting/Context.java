package ecse429.storytesting;

import ecse429.storytesting.model.Category;
import ecse429.storytesting.model.Project;
import ecse429.storytesting.model.Todo;

import java.util.*;

/**
 * Singleton context used to pass variables between steps
 */
public class Context {

    private static Context instance = null;

    private Map<String, ContextElement> variables;

    private Map<String, List<ContextElement>> listVariables;

    public Context() {
        this.listVariables = new HashMap<>();
        this.variables = new HashMap<>();
    }

    public static Context getContext() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public static void resetContext() {
        instance.variables.clear();
        instance = null;
    }

    public int get(String key) {
        return this.variables.get(key).id;
    }

    public void set(String key, int id, ContextElement.ElementType type) {
        ContextElement el = new ContextElement(id, type);
        this.variables.put(key, el);
    }

    public List<String> getListVariables(String key) {
        List<ContextElement> elementList = this.listVariables.get(key);
        List<String> result = new ArrayList<>();
        for (ContextElement e: elementList) {
            String id = "" + e.id;
            result.add(id);
        }
        return result;
    }

    public void setListVariables(String key, List value, ContextElement.ElementType type) {
        List<ContextElement> elementList = new ArrayList<>();
        if (type == ContextElement.ElementType.TODO) {
            for (Object obj : value) {
                Todo todo = (Todo) obj;
                ContextElement el = new ContextElement(todo.getId(), type);
                elementList.add(el);
            }
        }
        else if (type == ContextElement.ElementType.PROJECT) {
            for(Object obj: value) {
                Project project = (Project) obj;
                ContextElement el = new ContextElement(project.getId(), type);
                elementList.add(el);
            }
        }
        else if (type == ContextElement.ElementType.CATEGORY) {
            for(Object obj: value) {
                Category category = (Category) obj;
                ContextElement el = new ContextElement(category.getId(), type);
                elementList.add(el);
            }
        }
        this.listVariables.put(key, elementList);
    }

    public List<ContextElement> getAllElementsToDelete() {
        List<ContextElement> result = new ArrayList<>();
        Iterator<Map.Entry<String, ContextElement>> it = this.variables.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ContextElement> pair = it.next();
            if (pair.getValue().type == ContextElement.ElementType.TODO ||
                pair.getValue().type == ContextElement.ElementType.PROJECT ||
                pair.getValue().type == ContextElement.ElementType.CATEGORY) {

                result.add(pair.getValue());
            }
        }
        it.remove(); // avoids a ConcurrentModificationException

        Iterator<Map.Entry<String, List<ContextElement>>> it2 = this.listVariables.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<ContextElement>> pair = it2.next();
            for(ContextElement e: pair.getValue()) {
                if (e.type == ContextElement.ElementType.TODO ||
                        e.type == ContextElement.ElementType.PROJECT ||
                        e.type == ContextElement.ElementType.CATEGORY) {

                    result.add(e);
                }
            }
        }
        it.remove();
        return result;
    }

}
