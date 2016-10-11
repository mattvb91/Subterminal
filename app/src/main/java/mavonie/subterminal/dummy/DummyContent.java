package mavonie.subterminal.dummy;

import java.util.ArrayList;
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
        return new Gear(position, "Gear " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
