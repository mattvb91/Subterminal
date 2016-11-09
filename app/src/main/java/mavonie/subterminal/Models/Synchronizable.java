package mavonie.subterminal.Models;


import android.content.ContentValues;
import android.database.Cursor;

/**
 * Synchronizable Model
 */
public abstract class Synchronizable extends Model {

    public static final Integer SYNC_COMPLETED = 1;
    public static final Integer SYNC_REQUIRED = 0;

    public static final Integer DELETED_TRUE = 1;
    public static final Integer DELETED_FALSE = 0;

    private Integer synced;
    private Integer deleted;

    private Integer remote_id;

    public static final String COLUMN_SYNCED = "synced";
    public static final String COLUMN_DELETED = "deleted";

    /**
     * Transform the remote fields to the local fields
     */
    private void transformToLocal() {
        this.setId(this.remote_id);
        this.remote_id = null;
    }

    public void populateSynchronizationFromCursor(Cursor cursor) {
        int synced = cursor.getColumnIndexOrThrow(COLUMN_SYNCED);
        int deleted = cursor.getColumnIndexOrThrow(COLUMN_DELETED);

        this.setSynced(cursor.getInt(synced));
        this.setDeleted(cursor.getInt(deleted));
    }

    public Integer getSynced() {
        return synced;
    }

    public void setSynced(Integer synced) {
        this.synced = synced;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public void populateSynchronizationContentValues(ContentValues contentValues) {
        contentValues.put(COLUMN_DELETED, this.getDeleted());
        contentValues.put(COLUMN_SYNCED, this.getSynced());
    }

    public boolean isSynced() {
        return this.getSynced() == SYNC_COMPLETED;
    }

    /**
     * Mark as syncronized and call parent save because we dont want it
     * to get queued again for API update.
     */
    public void markSynced() {
        this.transformToLocal();

        this.setSynced(SYNC_COMPLETED);
        super.save();
    }

    /**
     * Mark as sync required
     *
     * @return boolean
     */
    public boolean save() {
        this.setSynced(SYNC_REQUIRED);
        return super.save();
    }

    @Override
    public boolean delete() {

        //Mark it as deleted first so we have a chance to sync to the server
        if (this.getDeleted() == DELETED_FALSE) {
            this.setDeleted(DELETED_TRUE);
            return super.save();
        } else {
            return super.delete();
        }
    }
}
