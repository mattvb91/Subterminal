package mavonie.subterminal.unit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.junit.Before;
import org.junit.Ignore;
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
