package mavonie.subterminal.Utils.Date;

import java.text.SimpleDateFormat;

/**
 * Created by mavon on 17/10/16.
 */

public class DateFormat extends SimpleDateFormat {

    public DateFormat() {
        super.applyPattern("yyyy-MM-dd");
    }
}
