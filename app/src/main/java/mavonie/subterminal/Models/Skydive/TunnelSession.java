package mavonie.subterminal.Models.Skydive;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Models.Synchronizable;
import retrofit2.Call;


/**
 * Tunnel session model
 */
public class TunnelSession extends Synchronizable {

    private Integer tunnel_id,
            length;

    private String description,
            date;

    //use this to get the current position from the query
    private int row_id;

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

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "skydive_tunnel_session";

    public static final String COLUMN_NAME_TUNNEL_ID = "tunnel_id";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_LENGTH = "length";
    public static final String COLUMN_NAME_DESCRIPTION = "description";

    /* END DB DEFINITIONS*/


    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<String, Integer>();

            dbColumns.put(COLUMN_NAME_TUNNEL_ID, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DATE, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_LENGTH, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_DESCRIPTION, TYPE_TEXT);
            dbColumns.put("row_id", TYPE_OTHER);

            Synchronizable.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

    @Override
    protected void populateCustomFromCursor(Field field, Cursor cursor) {
        switch (field.getName()) {
            case "row_id":
                this.setRowId(cursor.getCount() - cursor.getPosition());
                break;
        }
    }

    public int getRowId() {
        return row_id;
    }

    public void setRowId(int row_id) {
        this.row_id = row_id;
    }

    private Tunnel _tunnel;

    /**
     * Get the associated tunnel
     *
     * @return Tunnel
     */
    public Tunnel getTunnel() {
        if (_tunnel == null) {
            _tunnel = (Tunnel) new Tunnel().getOneById(this.getTunnelId());
        }

        return _tunnel;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public Integer getTunnelId() {
        return tunnel_id;
    }

    public void setTunnelId(Integer tunnel_id) {
        this.tunnel_id = tunnel_id;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TunnelSession that = (TunnelSession) o;

        if (tunnel_id != null ? !tunnel_id.equals(that.tunnel_id) : that.tunnel_id != null)
            return false;
        if (length != null ? !length.equals(that.length) : that.length != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        return date != null ? date.equals(that.date) : that.date == null;
    }
}
