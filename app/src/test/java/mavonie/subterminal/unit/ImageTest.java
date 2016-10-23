package mavonie.subterminal.unit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.R;
import mavonie.subterminal.unit.Base.BaseDBUnit;


/**
 * Test the image class
 */
public class ImageTest extends BaseDBUnit {

    Bitmap bmp;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        bmp = BitmapFactory.decodeResource(RuntimeEnvironment.application.getResources(), R.drawable.ic_menu_camera);
    }

    @Test
    public void testWriteToStorage() {
        assertNotNull(Image.writeToStorage(bmp));
    }

    @Test
    public void testCreateFromBitmapAssociated() {
        Exit exit = ExitTest.createExit();

        assertTrue(Image.createFromBitmap(bmp, exit));

        Image thumb = exit.getThumbImage();
        assertNotNull(thumb);

        List<Image> list = Image.loadImagesForEntity(exit);
        assertEquals(list.get(0), thumb);
    }

    /**
     * Make sure we delete all images associated with an entity
     * when it gets deleted.
     */
    @Test
    public void testEntityDeletion() {
        Exit exit = ExitTest.createExit();

        assertTrue(Image.createFromBitmap(bmp, exit));
        assertTrue(Image.createFromBitmap(bmp, exit));

        List<Image> list = Image.loadImagesForEntity(exit);
        assertEquals(list.size(), 2);

        for (Image image : list) {
            assertNotNull(new Image().getOneById(image.getId()));
            File file = new File(image.getFullPath());
            assertTrue(file.exists());
        }

        assertTrue(exit.delete());
        for (Image image : list) {
            assertNull(new Image().getOneById(image.getId()));
            File file = new File(image.getFullPath());
            assertFalse(file.exists());
        }
    }
}
