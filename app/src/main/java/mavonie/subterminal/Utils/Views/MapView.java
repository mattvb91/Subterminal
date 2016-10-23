package mavonie.subterminal.Utils.Views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.gms.maps.GoogleMapOptions;

/**
 * Created by mavon on 14/10/16.
 */

public class MapView extends com.google.android.gms.maps.MapView {

    public MapView(Context context) {
        super(context);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MapView(Context context, GoogleMapOptions googleMapOptions) {
        super(context, googleMapOptions);
    }

    /**
     * this is required to override the up scroll behaviour
     * as the map is displayed inside a scrollLayout
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Disallow ScrollView to intercept touch events.
                this.getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_UP:
                // Allow ScrollView to intercept touch events.
                this.getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }

        // Handle MapView's touch events.
        super.dispatchTouchEvent(ev);
        return true;
    }
}
