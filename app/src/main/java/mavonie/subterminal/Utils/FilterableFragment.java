package mavonie.subterminal.Utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import java.util.HashMap;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.R;

/**
 * Fragment that allows filtering its contents
 */
public abstract class FilterableFragment extends BaseFragment {

    /**
     * The layout for our filter form
     *
     * @return int
     */
    protected abstract int getPopupLayout();

    /**
     * Open the filter and assign relevant items
     */
    public abstract void populateFilter();

    /**
     * The build up params
     *
     * @return HashMap
     */
    protected abstract HashMap<String, Object> buildFilterParams();

    protected abstract void filterButtonPressed();

    protected PopupWindow popupWindow;

    /**
     * The filter window
     *
     * @return PopupWindow
     */
    public PopupWindow getFilterPopup(final BaseFragment fragment) {

        View popupView = this.getLayoutInflater(null).inflate(getPopupLayout(), null);
        popupWindow = new PopupWindow(popupView,
                DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setContentView(popupView);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(MainActivity.getActivity().getResources(), Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        final Button filterButton = (Button) popupWindow.getContentView().findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterButtonPressed();
            }
        });

        final Button clearButton = (Button) popupWindow.getContentView().findViewById(R.id.filter_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                try {
                    UIHelper.replaceFragment(fragment.getClass().newInstance());
                } catch (java.lang.InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        return popupWindow;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.getActivity().getOptionsMenu().findItem(R.id.action_filter).setVisible(true);
    }
}
