package mavonie.subterminal.Models;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mavonie.subterminal.Jobs.DownloadImage;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.Synchronized;
import mavonie.subterminal.Utils.UIHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;


/**
 * Image class
 */
public class Image extends Synchronizable {

    public static final String IMAGE_PATH = "/Subterminal/images";

    Bitmap bitmap;
    String filename;
    int entity_type;
    int entity_id;

    /* DB DEFINITIONS */
    public static final String TABLE_NAME = "image";

    public static final String COLUMN_NAME_FILENAME = "filename";
    public static final String COLUMN_NAME_ENTITY_TYPE = "entity_type";
    public static final String COLUMN_NAME_ENTITY_ID = "entity_id";

    private static Map<String, Integer> dbColumns = null;

    @Override
    public Map<String, Integer> getDbColumns() {
        if (dbColumns == null) {
            dbColumns = new HashMap<>();

            dbColumns.put(COLUMN_NAME_FILENAME, TYPE_TEXT);
            dbColumns.put(COLUMN_NAME_ENTITY_TYPE, TYPE_INTEGER);
            dbColumns.put(COLUMN_NAME_ENTITY_ID, TYPE_INTEGER);

            Synchronizable.setDBColumns(dbColumns);
        }

        return dbColumns;
    }

    public static final int ENTITY_TYPE_EXIT = 0;
    public static final int ENTITY_TYPE_JUMP = 1;
    public static final int ENTITY_TYPE_SKYDIVE = 2;
    public static final int ENTITY_TYPE_SIGNATURE = 3;


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getEntityType() {
        return entity_type;
    }

    public void setEntityType(int entity_type) {
        this.entity_type = entity_type;
    }

    public int getEntityId() {
        return entity_id;
    }

    public void setEntityId(int entity_id) {
        this.entity_id = entity_id;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }


    /**
     * Associate an entity with this image.
     *
     * @param associatedEntity
     */
    public void associateEntity(Model associatedEntity) {
        this.setEntityType(getEntityTypeFromModel(associatedEntity));
        this.setEntityId(associatedEntity.getId());
    }

    /**
     * Figure out what entity we are dealing with.
     *
     * @param associatedEntity
     * @return Model
     */
    private static int getEntityTypeFromModel(Model associatedEntity) {
        if (associatedEntity instanceof Exit) {
            return ENTITY_TYPE_EXIT;
        } else if (associatedEntity instanceof Jump) {
            return ENTITY_TYPE_JUMP;
        } else if (associatedEntity instanceof Skydive) {
            return ENTITY_TYPE_SKYDIVE;
        } else if (associatedEntity instanceof Signature) {
            return ENTITY_TYPE_SIGNATURE;
        }

        return -1;
    }

    public static List<Image> loadImagesForEntity(Model entity) {

        HashMap<String, Object> params = whereParamsForCurrentEntity(entity);

        return new Image().getItems(params);
    }

    /**
     * Gets the first image associated with this entity
     *
     * @param entity
     * @return Image|Null
     */
    public static Image loadThumbForEntity(Model entity) {
        HashMap<String, Object> params = whereParamsForCurrentEntity(entity);
        params.put(FILTER_LIMIT, "1");

        List result = new Image().getItems(params);

        if (result.size() > 0) {
            return (Image) result.get(0);
        }

        return null;
    }

    @NonNull
    private static HashMap<String, Object> whereParamsForCurrentEntity(Model entity) {
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
        return params;
    }

    public String getFullPath() {
        String root = Environment.getExternalStorageDirectory().toString();
        return root + IMAGE_PATH + "/" + this.getFilename();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (this.getId() != ((Image) o).getId()) return false;
        if (entity_type != image.entity_type) return false;
        if (entity_id != image.entity_id) return false;
        if (bitmap != null ? !bitmap.equals(image.bitmap) : image.bitmap != null) return false;
        return filename.equals(image.filename);
    }

    /**
     * Delete all images associated with this entity.
     *
     * @param model
     */
    public static boolean deleteForEntity(Model model) {

        if (getEntityTypeFromModel(model) >= 0) {
            for (Image image : loadImagesForEntity(model)) {
                File file = new File(image.getFullPath());
                boolean res = file.delete();

                if (!res & !image.delete()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @return Uri
     */
    public Uri getUri() {
        File file = new File(this.getFullPath());

        return Uri.fromFile(file);
    }

    //TODO properly name images based on model
    public static Image createFromPath(String originalPath, Model model) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + IMAGE_PATH);
        myDir.mkdirs();

        String fname = generateFilename();

        try {
            copy(new File(originalPath), new File(myDir, fname));

            Image image = new Image();
            image.associateEntity(model);
            image.setFilename(fname);
            image.save();

            return image;

        } catch (IOException e) {
            e.printStackTrace();
            UIHelper.toast(e.getMessage());
        }

        return null;
    }

    @NonNull
    public static String generateFilename() {
        return "Image_" + new Random().nextInt(10000) + new Image().getNextAutoIncrement() + ".jpg";
    }

    private static void copy(File src, File dst) throws IOException {

        verifyStoragePermissions(MainActivity.getActivity());

        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    /**
     * Only need this currently for setting a placeholder image
     *
     * @return
     */
    public static GenericDraweeHierarchy getHierarchy() {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(MainActivity.getActivity().getResources());

        return builder
                .setPlaceholderImage(R.mipmap.ic_image_placeholder)
                .build();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static void verifyStoragePermissions(Activity activity) {

        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static List<Image> getImagesForSync() {
        return new Image().getItems(getSyncRequiredParams());
    }

    /**
     * Create a new ImageRunnable thread.
     */
    public static void downloadImages(List<Image> images, String server_time) {
        ImageRunnable downloadThread = new ImageRunnable(images, server_time);
        downloadThread.run();
    }

    @Override
    public Call getSyncEndpoint() {
        File file = new File(this.getFullPath());
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        return Subterminal.getApi().getEndpoints().uploadImage(imagePart, this.getEntityType(), this.getEntityId(), this.getId());
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
        return Synchronized.PREF_LAST_SYNC_IMAGE;
    }

    /**
     * Runnable for downloading remote images on a seperate thread
     */
    public static class ImageRunnable implements Runnable {

        private List<Image> images;
        private String server_time;

        public ImageRunnable(List<Image> images, String server_time) {
            this.images = images;
            this.server_time = server_time;
        }

        @Override
        public void run() {
            if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(MainActivity.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                for (Image image : this.images) {
                    Subterminal.getJobManager(MainActivity.getActivity()).addJobInBackground(new DownloadImage(image));
                }

                //Make sure we only set the sync time once every download has completed
                Synchronized.setLastSyncPref(Synchronized.PREF_LAST_SYNC_IMAGE, this.server_time);
            } else {
                verifyStoragePermissions(MainActivity.getActivity());
            }
        }
    }
}
