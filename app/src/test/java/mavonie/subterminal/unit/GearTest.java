package mavonie.subterminal.unit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mavonie.subterminal.Models.Gear;

import static org.junit.Assert.*;

/**
 * Created by mavon on 12/10/16.
 */
public class GearTest {

    Gear gear;

    @Before
    public void setUp() throws Exception {
        this.gear = new Gear();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getContainerManufacturer() throws Exception {
        setContainerManufacturer();
        assertSame(gear.getContainerManufacturer(), "Manufacturer");
    }

    @Test
    public void setContainerManufacturer() throws Exception {
        this.gear.setContainerManufacturer("Manufacturer");
    }

    @Test
    public void getContainerType() throws Exception {

    }

    @Test
    public void setContainerType() throws Exception {

    }

    @Test
    public void getContainerSerial() throws Exception {

    }

    @Test
    public void setContainerSerial() throws Exception {

    }

    @Test
    public void getContainerDateInUse() throws Exception {

    }

    @Test
    public void setContainerDateInUse() throws Exception {

    }

    @Test
    public void getCanopyManufacturer() throws Exception {

    }

    @Test
    public void setCanopyManufacturer() throws Exception {

    }

    @Test
    public void getCanopyType() throws Exception {

    }

    @Test
    public void setCanopyType() throws Exception {

    }

    @Test
    public void getCanopySerial() throws Exception {

    }

    @Test
    public void setCanopySerial() throws Exception {

    }

    @Test
    public void getCanopyDateInUse() throws Exception {

    }

    @Test
    public void setCanopyDateInUse() throws Exception {

    }

    @Test
    public void getId() throws Exception {

    }

    @Test
    public void save() throws Exception {

    }

}