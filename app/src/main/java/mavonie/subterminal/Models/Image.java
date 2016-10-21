package mavonie.subterminal.Models;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by mavon on 20/10/16.
 */

public class Image extends Model {

    private static final String IMAGE_PATH = "/Subterminal/images";

    Bitmap bitmap;
    String filename;
    int entity_type;
    int entity_id;
    int synced;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "image";

    public static final String COLUMN_NAME_FILENAME = "filename";
    public static final String COLUMN_NAME_ENTITY_TYPE = "entity_type";
    public static final String COLUMN_NAME_ENTITY_ID = "entity_id";
    public static final String COLUMN_NAME_SYNCED = "synced";

    public static final int ENTITY_TYPE_EXIT = 0;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getEntity_type() {
        return entity_type;
    }

    public void setEntity_type(int entity_type) {
        this.entity_type = entity_type;
    }

    public int getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(int entity_id) {
        this.entity_id = entity_id;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public static String writeToStorage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + IMAGE_PATH);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            return fname;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    Model populateFromCursor(Cursor cursor) {

        try {
            Image image = new Image();

            int idIndex = cursor.getColumnIndexOrThrow(_ID);
            int idEntityType = cursor.getColumnIndexOrThrow(COLUMN_NAME_ENTITY_TYPE);
            int idEntityId = cursor.getColumnIndexOrThrow(COLUMN_NAME_ENTITY_ID);
            int idFilename = cursor.getColumnIndexOrThrow(COLUMN_NAME_FILENAME);
            int idSynced = cursor.getColumnIndexOrThrow(COLUMN_NAME_SYNCED);

            image.setId(cursor.getInt(idIndex));
            image.setEntity_id(cursor.getInt(idEntityId));
            image.setEntity_type(cursor.getInt(idEntityType));
            image.setFilename(cursor.getString(idFilename));
            image.setSynced(cursor.getInt(idSynced));

            return image;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    void populateContentValues(ContentValues contentValues) {
        contentValues.put(COLUMN_NAME_FILENAME, this.getFilename());
        contentValues.put(COLUMN_NAME_ENTITY_ID, this.getEntity_id());
        contentValues.put(COLUMN_NAME_ENTITY_TYPE, this.getEntity_type());
        contentValues.put(COLUMN_NAME_SYNCED, this.getSynced());
    }

    @Override
    String getTableName() {
        return TABLE_NAME;
    }

    public static boolean createFromBitmap(Bitmap bitMap, Model associatedEntity) {

        String path = writeToStorage(bitMap);

        if (path != null) {
            Image image = new Image();
            image.setEntity_type(getEntityTypeFromModel(associatedEntity));
            image.setEntity_id(associatedEntity.getId());
            image.setFilename(path);
            return image.save();
        }

        return false;
    }

    private static int getEntityTypeFromModel(Model associatedEntity) {
        if (associatedEntity instanceof Exit) {
            return ENTITY_TYPE_EXIT;
        }

        return ENTITY_TYPE_EXIT;
    }

    public static List<Image> loadImagesForEntity(Model entity) {

        HashMap<String, Object> params = new HashMap<>();

        HashMap<String, Object> whereId = new HashMap<>();
        whereId.put(Model.FILTER_WHERE_FIELD, COLUMN_NAME_ENTITY_ID);
        whereId.put(Model.FILTER_WHERE_VALUE, entity.getId());

        HashMap<String, Object> whereEntityType = new HashMap<>();
        whereEntityType.put(Model.FILTER_WHERE_FIELD, COLUMN_NAME_ENTITY_TYPE);
        whereEntityType.put(Model.FILTER_WHERE_VALUE, Integer.toString(getEntityTypeFromModel(entity)));

        HashMap<Integer, HashMap> wheres = new HashMap<>();
        wheres.put(wheres.size(), whereEntityType);
        wheres.put(wheres.size(), whereId);

        params.put(Model.FILTER_WHERE, wheres);

        return new Image().getItems(params);
    }

    public String getFullPath() {
        String root = Environment.getExternalStorageDirectory().toString();
        return root + IMAGE_PATH + "/" + this.getFilename();
    }

    public static Bitmap decodeSampledBitmapFromResource(String path,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
