package mavonie.subterminal.Skydive.Forms;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import mavonie.subterminal.Forms.BaseForm;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Skydive.Tunnel;
import mavonie.subterminal.Models.Skydive.TunnelSession;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.Adapters.LinkedHashMapAdapter;
import mavonie.subterminal.Utils.Date.DateFormat;
import mavonie.subterminal.Utils.Listeners.DatePickerTextView;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Tunnel Session Form
 */
public class TunnelSessionForm extends BaseForm implements AdapterView.OnItemClickListener {

    private AutoCompleteTextView tunnel;
    private EditText date, length, description;

    private LinkedHashMap<String, String> tunnelNames;
    LinkedHashMapAdapter<String, String> tunnelAdapter;

    @Override
    protected int getParentFragmentId() {
        return R.id.skydiving_nav_tunnels;
    }

    @Override
    protected void assignFormElements(View view) {
        tunnel = (AutoCompleteTextView) view.findViewById(R.id.tunnel_session_edit_tunnel);

        tunnelNames = new Tunnel().getItemsForSelect("name");
        tunnelAdapter = new LinkedHashMapAdapter<String, String>(MainActivity.getActivity().getApplicationContext(), R.layout.item_simple, tunnelNames, LinkedHashMapAdapter.FLAG_FILTER_ON_VALUE);

        tunnel.setAdapter(tunnelAdapter);
        tunnel.setThreshold(2);
        tunnel.setOnItemClickListener(this);

        date = (EditText) view.findViewById(R.id.tunnel_session_edit_date);
        new DatePickerTextView(date);
        date.setText(new DateFormat().format(Calendar.getInstance().getTime()).toString());

        length = (EditText) view.findViewById(R.id.tunnel_session_edit_length);
        description = (EditText) view.findViewById(R.id.tunnel_session_edit_description);

        Button button = (Button) view.findViewById(R.id.session_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });
    }

    @Override
    protected TunnelSession getItem() {
        return (TunnelSession) super.getItem();
    }

    @Override
    public void save() {
        if (validateForm()) {
            if (tunnelEntry != null) {
                getItem().setTunnelId(Integer.parseInt(tunnelEntry.getKey()));
            }

            getItem().setLength(Integer.valueOf(length.getText().toString()));
            getItem().setDate(date.getText().toString());
            getItem().setDescription(description.getText().toString());

            super.save();
        }
    }

    @Override
    protected void updateForm() {
        if (getItem().exists()) {
            Subterminal.setActiveModel(getItem());

            if (getItem().getTunnelId() != null) {
                this.tunnel.setText(getItem().getTunnel().getName());
                this.tunnelEntry = this.tunnelAdapter.getItem(this.tunnelAdapter.findPositionFromKey(getItem().getTunnelId()));
            }

            description.setText(getItem().getDescription());
            date.setText(getItem().getDate());
            length.setText(getItem().getLength().toString());
        }
    }

    @Override
    protected boolean validateForm() {
        if (length.getText().length() == 0) {
            length.setError("Session length required");
            return false;
        }
        return true;
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_tunnel_session_form;
    }

    @Override
    protected String getItemClass() {
        return TunnelSession.class.getCanonicalName();
    }

    private Map.Entry<String, String> tunnelEntry;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tunnelEntry = this.tunnelAdapter.getItem(position);
        tunnel.setText(tunnelEntry.getValue());
    }
}
