package mavonie.subterminal.Models;


import android.content.ContentValues;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.Models.Skydive.Rig;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Synchronizable Model
 */
public abstract class Synchronizable extends Model {

    public static final Integer SYNC_COMPLETED = 1;
    public static final Integer SYNC_REQUIRED = 0;

    public static final Integer DELETED_TRUE = 1;
    public static final Integer DELETED_FALSE = 0;

    private Integer synced = SYNC_REQUIRED;
    private Integer deleted = DELETED_FALSE;

    protected Integer remote_id;

    public static final String COLUMN_SYNCED = "synced";
    public static final String COLUMN_DELETED = "deleted";

    public static void setDBColumns(Map<String, Integer> dbColumns) {
        dbColumns.put(COLUMN_SYNCED, TYPE_INTEGER);
        dbColumns.put(COLUMN_DELETED, TYPE_INTEGER);

        Model.setDBColumns(dbColumns);
    }

    public Integer getSynced() {
        return synced;
    }

    public void setSynced(Integer synced) {
        this.synced = synced;
    }

    public Integer getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public boolean isSynced() {
        return this.getSynced().equals(SYNC_COMPLETED);
    }

    /**
     * Mark as syncronized and call parent save because we dont want it
     * to get queued again for API update.
     */
    public void markSynced() {
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
        boolean res = super.save();

        //Sync if user is logged in and premium
        if (Subterminal.getUser().isLoggedIn() && Subterminal.getUser().isPremium() && !Subterminal.isTesting()) {
            this.addSyncJob();
        }

        return res;
    }


    @Override
    protected void populateContentValues(ContentValues contentValues) {

        //Check if this was a remote call and associate it with its original id
        if (this.remote_id != null) {
            contentValues.put(_ID, this.remote_id);

            if (this._id == 0) {
                this._id = this.remote_id;
            }
        }

        super.populateContentValues(contentValues);
    }

    @Override
    public boolean delete() {

        //Mark it as deleted first so we have a chance to sync to the server
        if (this.getDeleted() == DELETED_FALSE) {
            this.setDeleted(DELETED_TRUE);
            boolean res = super.save();

            if (Subterminal.getUser().isPremium() && !Subterminal.isTesting()) {
                this.addSyncJob();
            }

            return res;
        } else {
            return super.delete();
        }
    }

    public abstract void addSyncJob();

    /**
     * Used to identify in a job queue
     *
     * @return String
     */
    public String getJobTag() {
        return this.getClass().getCanonicalName() + this.getId();
    }

    /**
     * Force sync all items up to the server.
     */
    public static void forceSyncAll() {

        HashMap<String, Object> whereGlobalIdNull = new HashMap<>();
        whereGlobalIdNull.put(Model.FILTER_WHERE_FIELD, Exit.COLUMN_NAME_GLOBAL_ID);
        whereGlobalIdNull.put(Model.FILTER_WHERE_VALUE, null);


        List<Exit> exits = new Exit().getItems(whereGlobalIdNull);
        for (Exit exit : exits) {
            exit.save();
        }

        List<Gear> gears = new Gear().getItems(null);
        for (Gear gear : gears) {
            gear.save();
        }

        List<Suit> suits = new Suit().getItems(null);
        for (Suit suit : suits) {
            suit.save();
        }

        List<Jump> jumps = new Jump().getItems(null);
        for (Jump jump : jumps) {
            jump.save();
        }

        syncEntities();
    }

    /**
     * Sync at startup. Make sure the sync order is. Do this on a seperate thread
     * Exits -> Gear -> Jump
     */
    public static void syncEntities() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for (Exit exit : Exit.getExitsForSync()) {
                    exit.addSyncJob();
                }

                for (Exit exit : Exit.getExitsForDelete()) {
                    exit.addSyncJob();
                }

                for (Gear gear : Gear.getGearForSync()) {
                    gear.addSyncJob();
                }

                for (Gear gear : Gear.getGearForDelete()) {
                    gear.addSyncJob();
                }

                for (Suit suit : Suit.getSuitsForSync()) {
                    suit.addSyncJob();
                }

                for (Suit suit : Suit.getSuitsForDelete()) {
                    suit.addSyncJob();
                }

                for (Jump jump : Jump.getJumpsForSync()) {
                    jump.addSyncJob();
                }

                for (Jump jump : Jump.getJumpsForDelete()) {
                    jump.addSyncJob();
                }

                for (Rig rig : Rig.getRigsForSync()) {
                    rig.addSyncJob();
                }

                for (Rig rig : Rig.getRigsForDelete()) {
                    rig.addSyncJob();
                }

                for (Skydive skydive : Skydive.getSkydivesForSync()) {
                    skydive.addSyncJob();
                }

                for (Skydive skydive : Skydive.getSkydivesForDelete()) {
                    skydive.addSyncJob();
                }

                for (Image image : Image.getImagesForSync()) {
                    image.addSyncJob();
                }
            }
        });
    }

    /**
     * Return all items that are not deleted
     *
     * @return List
     */
    public List<Model> getActiveItems() {
        return super.getItems(getActiveParams());
    }

    public static HashMap<String, Object> getActiveParams() {
        HashMap<String, Object> whereNotDeleted = new HashMap<>();
        whereNotDeleted.put(Model.FILTER_WHERE_FIELD, COLUMN_DELETED);
        whereNotDeleted.put(Model.FILTER_WHERE_VALUE, DELETED_FALSE.toString());

        return whereNotDeleted;
    }

    public static HashMap<String, Object> getSyncRequiredParams() {
        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> whereSyncRequired = new HashMap<>();
        whereSyncRequired.put(Model.FILTER_WHERE_FIELD, COLUMN_SYNCED);
        whereSyncRequired.put(Model.FILTER_WHERE_VALUE, SYNC_REQUIRED);

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereSyncRequired);

        params.put(Model.FILTER_WHERE, wheres);

        return params;
    }

    public static HashMap<String, Object> getDeleteRequiredParams() {
        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> whereDeleteRequired = new HashMap<>();
        whereDeleteRequired.put(Model.FILTER_WHERE_FIELD, COLUMN_DELETED);
        whereDeleteRequired.put(Model.FILTER_WHERE_VALUE, DELETED_TRUE);

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereDeleteRequired);

        params.put(Model.FILTER_WHERE, wheres);

        return params;
    }
}
