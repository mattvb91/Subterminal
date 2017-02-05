package mavonie.subterminal.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Models.Skydive.Skydive;
import retrofit2.Call;

/**
 * Signature Model
 */
public class Signature extends Synchronizable {

    private Integer entity_id,
            entity_type;

    private String print_name;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "signature";

    public static final String COLUMN_NAME_ENTITY_ID = "entity_id";
    public static final String COLUMN_NAME_ENTITY_TYPE = "entity_type";
    public static final String COLUMN_NAME_ENTITY_PRINT_NAME = "print_name";
    /* END DB DEFINITIONS */

    public final static int TYPE_SKYDIVE = 1;
    public final static int TYPE_BASE = 2;

    @Override
    public void addSyncJob() {

    }

    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<String, Integer>();

            dbColumns.put(COLUMN_NAME_ENTITY_ID, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_ENTITY_TYPE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_ENTITY_PRINT_NAME, TYPE_TEXT);

            Synchronizable.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public Integer getEntityId() {
        return entity_id;
    }

    public void setEntityId(Integer entity_id) {
        this.entity_id = entity_id;
    }

    public Integer getEntityType() {
        return entity_type;
    }

    public void setEntityType(Integer entity_type) {
        this.entity_type = entity_type;
    }

    public String getPrintName() {
        return print_name;
    }

    public void setPrintName(String print_name) {
        this.print_name = print_name;
    }

    /**
     * Associate an entity with this entry
     *
     * @param activeModel
     */
    public void associateEntity(Model activeModel) {
        this.setEntityType(getEntityTypeFromModel(activeModel));
        this.setEntityId(activeModel.getId());
    }

    /**
     * Figure out what entity we are dealing with.
     *
     * @param associatedEntity
     * @return Model
     */
    public static int getEntityTypeFromModel(Model associatedEntity) {
        if (associatedEntity instanceof Jump) {
            return TYPE_BASE;
        } else if (associatedEntity instanceof Skydive) {
            return TYPE_SKYDIVE;
        }

        return -1;
    }

    /**
     * Get the image associated with this signature
     *
     * @return
     */

    private Image _image = null;

    public Image getImage() {
        if (_image == null) {
            _image = Image.loadThumbForEntity(this);
        }

        return _image;
    }

    @Override
    public Call getSyncEndpoint() {
        return null;
    }

    @Override
    public Call<Void> getDeleteEndpoint() {
        return null;
    }

    @Override
    public Call<List> getDownloadEndpoint() {
        return null;
    }

    @Override
    public String getSyncIdentifier() {
        return null;
    }
}
