package mavonie.subterminal.Models.Skydive;

import java.util.HashMap;
import java.util.Map;

import mavonie.subterminal.Models.Model;

/**
 * Aircraft Model
 */
public class Aircraft extends Model {

    private int id; //THIS IS ONLY USED TO SYNC TO THE REMOTE ID

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "skydive_aircraft";

    public static final String COLUMN_NAME_NAME = "name";

    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<String, Integer>();

            dbColumns.put(COLUMN_NAME_NAME, TYPE_TEXT);

            Model.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public static void createOrUpdate(Aircraft aircraft) {

        Aircraft dbAircraft = (Aircraft) new Aircraft().getOneById(aircraft.id);

        //Check if it equals
        if (dbAircraft != null) {
            if (!dbAircraft.equals(aircraft)) {
                //Update if it doesnt
                aircraft.setId(dbAircraft.getId());
                aircraft.save();
            }
        } else {
            aircraft.setId(aircraft.id);
            aircraft.save();
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aircraft aircraft = (Aircraft) o;

        if (id != aircraft.id) return false;
        return name != null ? name.equals(aircraft.name) : aircraft.name == null;

    }
}
