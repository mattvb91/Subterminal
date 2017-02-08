package mavonie.subterminal.Utils.Listeners;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Utils.Date.DateFormat;


/**
 * Date picker to handle all date associated events for a textview
 */
public class DatePickerTextView {

    TextView textView;

    static Calendar calendar = Calendar.getInstance();
    static DateFormat df = new DateFormat();

    public DatePickerTextView(final TextView textView) {
        this.textView = textView;
        this.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        textView.setText(df.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

}
