package mavonie.subterminal.dummy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mavonie.subterminal.models.Gear;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Gear> ITEMS = new ArrayList<Gear>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Gear> ITEM_MAP = new HashMap<String, Gear>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Gear item) {
        ITEMS.add(item);
        ITEM_MAP.put(Integer.toString(item.getId()), item);
    }

    private static Gear createDummyItem(int position) {
        Gear gear = new Gear(position, "Manufacturer " + position, "Container_" + position);
        gear.setContainerDateInUse(new Date());
        gear.setContainerType("Container type");
        gear.setCanopyType("Canopy type");
        gear.setCanopyManufacturer("Canopy Manufacturer");
        gear.setCanopySerial("Canopy Serial" + position);
        gear.setCanopyDateInUse(new Date());

        return gear;
    }
}
