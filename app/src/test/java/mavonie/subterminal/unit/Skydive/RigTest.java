package mavonie.subterminal.unit.Skydive;

import org.junit.Assert;
import org.junit.Test;

import mavonie.subterminal.Models.Skydive.Rig;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.unit.Base.BaseDBUnit;

/**
 * Dropzone unit tests
 */
public class RigTest extends BaseDBUnit {

    @Test
    public void createRigTest() {
        Rig rig = createRig();

        Assert.assertTrue(rig.equals(new Rig().getOneById(rig.getId())));
    }

    public static Rig createRig() {
        Rig rig = new Rig();

        rig.setContainerModel("Container Model");
        rig.setContainerDateInUse("2009-09-01");
        rig.setContainerManufacturer("Container Manufacturer");
        rig.setContainerSerial("Container seerial");

        rig.setMainModel("Main Model");
        rig.setMainDateInUse("2008-09-01");
        rig.setMainManufacturer("Main Manufacturer");
        rig.setMainSerial("Main seerial");

        rig.setReserveModel("Reserve Model");
        rig.setReserveDateInUse("2007-09-01");
        rig.setReserveManufacturer("Reserve Manufacturer");
        rig.setReserveSerial("Reserve seerial");

        rig.setAadModel("AAD Model");
        rig.setAadDateInUse("2007-09-01");
        rig.setAadManufacturer("AAD Manufacturer");
        rig.setAadSerial("AAD seerial");
        rig.save();

        return rig;
    }

    @Test
    public void deleteRigUpdatedSkydivesTest() {
        Rig rig = createRig();

        Skydive skydive = SkydiveTest.createSkydive();
        skydive.setRigId(rig.getId());
        skydive.save();

        Skydive dbSkydive = (Skydive) new Skydive().getOneById(skydive.getId());
        Assert.assertTrue(dbSkydive.getRigId().equals(rig.getId()));

        Skydive skydive2 = SkydiveTest.createSkydive();
        skydive2.setRigId(rig.getId());
        skydive2.save();

        Skydive dbSkydive2 = (Skydive) new Skydive().getOneById(skydive2.getId());
        Assert.assertTrue(dbSkydive2.getRigId().equals(rig.getId()));

        rig.delete();

        dbSkydive = (Skydive) new Skydive().getOneById(skydive.getId());
        Assert.assertNull(dbSkydive.getRigId());

        dbSkydive2 = (Skydive) new Skydive().getOneById(skydive2.getId());
        Assert.assertNull(dbSkydive2.getRigId());
    }
}
