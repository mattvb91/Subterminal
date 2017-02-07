package mavonie.subterminal.Utils.DB;

import java.util.HashMap;

import mavonie.subterminal.Models.Model;

/**
 * Class to handle putting together bigger hashmaps for querying
 * our models.
 */
public class Query {

    HashMap<String, Object> params = new HashMap<>();
    HashMap<Integer, HashMap> wheres = new HashMap<>();

    public Query() {
    }

    public Query(String field, String value) {
        addWhere(field, value);
    }

    /**
     * Basic where operation
     *
     * @param field
     * @param value
     */
    public void addWhere(String field, String value) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(Model.FILTER_WHERE_FIELD, field);
        param.put(Model.FILTER_WHERE_VALUE, value);
        wheres.put(wheres.size(), param);
    }

    /**
     * Where with operator
     *
     * @param field
     * @param operator
     * @param value
     */
    public void addWhere(String field, String operator, String value) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(Model.FILTER_WHERE_FIELD, field);
        param.put(Model.FILTER_WHERE_VALUE, value);
        param.put(Model.FILTER_WHERE_OPERATOR, operator);
        wheres.put(wheres.size(), param);
    }

    /**
     * Get the full set of parameters
     *
     * @return Hashmap
     */
    public HashMap<String, Object> getParams() {
        if (!wheres.isEmpty()) {
            params.put(Model.FILTER_WHERE, wheres);
        }

        return params;
    }

    /**
     * Order direction based on field
     *
     * @param field
     * @param direction
     */
    public void orderDir(String field, String direction) {
        params.put(Model.FILTER_ORDER_DIR, direction);
        params.put(Model.FILTER_ORDER_FIELD, field);
    }
}
