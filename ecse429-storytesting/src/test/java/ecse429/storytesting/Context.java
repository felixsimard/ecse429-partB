package ecse429.storytesting;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton context used to pass variables between steps
 */
public class Context {

    private static Context instance = null;

    private Map<String, ContextElement> variables;

    public Context() {
        this.variables = new HashMap<String, ContextElement>();
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

    public ContextElement get(String key) {
        return this.variables.get(key);
    }

    public void set(String key, ContextElement value) {
        this.variables.put(key, value);
    }

}
