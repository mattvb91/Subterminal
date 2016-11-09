package mavonie.subterminal.Utils.Date;

import java.text.SimpleDateFormat;

/**
 * Created by mavon on 17/10/16.
 */

public class DateFormat extends SimpleDateFormat {

    public DateFormat() {
        super.applyPattern("yyyy-MM-dd");
    }

    public static String dateTimeNow() {
        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(dt);
    }
}
