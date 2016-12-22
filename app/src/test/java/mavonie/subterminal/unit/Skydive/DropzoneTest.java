package mavonie.subterminal.unit.Skydive;

import android.util.Pair;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.unit.Base.BaseDBUnit;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Dropzone unit tests
 */
public class DropzoneTest extends BaseDBUnit {

    @Test
    public void saveToDb() {

        Dropzone dropzone = createDropzone();
        assertTrue(dropzone.exists());

        Dropzone dbDropzone = (Dropzone) new Dropzone().getOneById(dropzone.getId());

        Assert.assertTrue(dropzone.equals(dbDropzone));

        Dropzone dropzone1 = (Dropzone) new Dropzone().getItem(new Pair<String, String>(Model._ID, Integer.toString(dbDropzone.getId())));
        assertEquals(dropzone1, dbDropzone);

        HashMap<String, Object> params = new HashMap<>();
        params.put(Model.FILTER_ORDER_DIR, Model.FILTER_ORDER_DIR_ASC);
        params.put(Model.FILTER_ORDER_FIELD, mavonie.subterminal.Models.Skydive.Dropzone.COLUMN_NAME_NAME);

        List dropzones = new Dropzone().getItems(params);

        assertNotNull(dropzones);
    }


    public static Dropzone createDropzone() {
        Dropzone dropzone = new Dropzone();

        dropzone.setAddressCountry("Ireland");
        dropzone.setName("Irish Parachute Club");
        dropzone.setDescription("Description");
        dropzone.setGlobalId("irish_parachute_club");
        dropzone.setEmail("info@skydive.ie");
        dropzone.setPhone("00353 938 35663");
        dropzone.setWebsite("http://skydive.ie");
        dropzone.setLatitude(53.2506);
        dropzone.setLongtitude(-7.1174);

        dropzone.save();

        return dropzone;
    }
}
