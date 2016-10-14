package mavonie.subterminal.Forms;


import android.view.View;

import mavonie.subterminal.R;
import mavonie.subterminal.models.Exit;

public class ExitForm extends BaseForm {

    @Override
    protected String getItemClass() {
        return Exit.class.getCanonicalName();
    }

    @Override
    protected int getParentFragmentId() {
        return R.id.nav_exits;
    }

    @Override
    protected void assignFormElements(View view) {

    }

    @Override
    protected void updateForm() {

    }

    @Override
    protected boolean validateForm() {
        return false;
    }

    @Override
    protected int getLayoutName() {
        return R.layout.fragment_exit_form;
    }
}
