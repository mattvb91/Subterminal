package mavonie.subterminal.Models;

import android.content.pm.PackageManager;

import ie.mavon.sqlitemodel.SQLiteModel;
import mavonie.subterminal.DB.DatabaseHandler;
import mavonie.subterminal.DB.VersionUtils;
import mavonie.subterminal.MainActivity;

/**
 * Base Model
 * Handle all core functionality like retrieving/saving a model
 */
abstract public class Model extends SQLiteModel {

    /**
     * Declare the db instance once
     */
    public Model() {
        if (_db == null) {
            try {
                _db = new DatabaseHandler(
                        MainActivity.getActivity().getApplicationContext(),
                        "database",
                        null,
                        VersionUtils.getVersionCode(MainActivity.getActivity().getApplicationContext())
                );
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
