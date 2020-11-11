package ecse429.storytesting;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton context used to pass variables between steps
 */
public class Context {

    private static Context instance = null;

    private Map<String, String> variables;

    public Context() {
        this.variables = new HashMap<String, String>();
    }

    public static Context getContext() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public static void resetContext() {
        instance = null;
    }

    public String get(String key) {
        return this.variables.get(key);
    }

    public void set(String key, String value) {
        this.variables.put(key, value);
    }

}
