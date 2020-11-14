package ecse429.storytesting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton context used to pass variables between steps
 */
public class Context {

    private static Context instance = null;

    private Map<String, Integer> variables;

    private Map<String, List> listVariables;

    public Context() {
        this.variables = new HashMap<String, Integer>();
        this.listVariables = new HashMap<String, List>();
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

    public int get(String key) {
        return this.variables.get(key);
    }

    public void set(String key, int value) {
        this.variables.put(key, value);
    }

    public List getListVariables(String key) {
        return this.listVariables.get(key);
    }

    public void setListVariables(String key, List value) {
        this.listVariables.put(key, value);
    }

}
