package mavonie.subterminal.unit;

import android.net.Uri;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.R;
import mavonie.subterminal.unit.Base.BaseDBUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


/**
 * Test the image class
 */
@Ignore
public class ImageTest extends BaseDBUnit {


    @Test
    public void testWriteToStorage() {
        Exit exit = ExitTest.createExit();

        Uri path = Uri.parse("android.resource://mavonie.subterminal/" + R.drawable.ic_menu_camera);
        assertNotNull(Image.createFromPath("file://" + path.toString(), exit));
    }

    @Test
    public void testCreateFromBitmapAssociated() {
        Exit exit = ExitTest.createExit();

        Uri path = Uri.parse("android.resource://mavonie.subterminal/" + R.drawable.ic_menu_camera);

        assertNotNull(Image.createFromPath(path.toString(), exit));

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

        Uri path = Uri.parse("android.resource://mavonie.subterminal/" + R.drawable.ic_menu_camera);

        assertNotNull(Image.createFromPath(path.toString(), exit));
        assertNotNull(Image.createFromPath(path.toString(), exit));

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
