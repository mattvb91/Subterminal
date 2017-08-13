package mavonie.subterminal.unit.Skydive;

import android.util.Pair;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mavonie.subterminal.Models.Model;
import mavonie.subterminal.Models.Skydive.Aircraft;
import mavonie.subterminal.Models.Skydive.Dropzone;
import mavonie.subterminal.Models.Skydive.DropzoneAircraft;
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

        Dropzone dropzone1 = (Dropzone) new Dropzone().getItem(new Pair<>(Model._ID, Integer.toString(dbDropzone.getId())));
        assertEquals(dropzone1, dbDropzone);

        HashMap<String, Object> params = new HashMap<>();
        params.put(Model.FILTER_ORDER_DIR, Model.FILTER_ORDER_DIR_ASC);
        params.put(Model.FILTER_ORDER_FIELD, mavonie.subterminal.Models.Skydive.Dropzone.COLUMN_NAME_NAME);

        List dropzones = new Dropzone().getItems(params);

        assertNotNull(dropzones);
    }


    public static Dropzone createDropzone() {
        Dropzone dropzone = new Dropzone();

        dropzone.setCountry("Ireland");
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

    @Test
    public void attachAircraftTest() {
        Aircraft aircraft = new Aircraft();
        aircraft.setId(1);
        aircraft.setName("Test Aircraft");
        aircraft.save();

        Aircraft dbAircraft = (Aircraft) new Aircraft().getOneById(1);

        Assert.assertEquals(dbAircraft, aircraft);

        Dropzone dropzone = createDropzone();

        DropzoneAircraft dzAircraft = new DropzoneAircraft();
        dzAircraft.setDropzoneId(dropzone.getId());
        dzAircraft.setAircraftId(dbAircraft.getId());
        dzAircraft.save();

        assertEquals(dropzone.getAircraft().size(), 1);

        for (Aircraft aircraft1 : dropzone.getAircraft()) {
            aircraft1.equals(aircraft);
        }
    }

    @Test
    public void updateAircrafts() {

        Aircraft aircraft = new Aircraft();
        aircraft.setId(1);
        aircraft.setName("Test Aircraft");
        aircraft.save();

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2);
        aircraft2.setName("Test Aircraft2");
        aircraft2.save();

        Dropzone dz = createDropzone();

        Assert.assertTrue(dz.getAircraft().size() == 0);

        DropzoneAircraft dzAircraft = new DropzoneAircraft();
        dzAircraft.setAircraftId(aircraft.getId());

        DropzoneAircraft dzAircraft2 = new DropzoneAircraft();
        dzAircraft2.setAircraftId(aircraft2.getId());

        List<DropzoneAircraft> list = new ArrayList<>();
        list.add(dzAircraft);
        list.add(dzAircraft2);

        dz.setDropzoneAircraft(list);
        Dropzone.createOrUpdate(dz);

        Assert.assertTrue(dz.getAircraft().size() == 2);
    }

    @Test
    public void testSetFeatured() {
        Dropzone dz = createDropzone();
        dz.setFeatured(Dropzone.FEATURED_TRUE);
        dz.save();

        Dropzone dz2 = (Dropzone) new Dropzone().getOneById(dz.getId());

        Assert.assertTrue(dz.equals(dz2));
        Assert.assertEquals(dz2.getFeatured(), Dropzone.FEATURED_TRUE);
    }
}
