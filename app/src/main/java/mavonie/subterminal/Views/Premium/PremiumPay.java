package mavonie.subterminal.Views.Premium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Payment;
import mavonie.subterminal.R;
import mavonie.subterminal.Utils.BaseFragment;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;


/**
 * Premium view
 */
public class PremiumPay extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_premium_pay, container, false);

        final Payment payment = (Payment) Subterminal.getActiveModel();

        TextView card = (TextView) view.findViewById(R.id.pay_confirm_card);
        card.setText(payment.getCreditCard().getRedactedCardNumber());

        TextView validThrough = (TextView) view.findViewById(R.id.pay_confirm_valid);
        validThrough.setText(payment.getCreditCard().expiryMonth + "/" + payment.getCreditCard().expiryYear);

        TextView cvv = (TextView) view.findViewById(R.id.pay_confirm_cvv);
        cvv.setText(payment.getCreditCard().cvv);

        UIHelper.getAddButton().hide();

        Button confirmButton = (Button) view.findViewById(R.id.pay_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment.submit();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.getActivity().setTitle(R.string.get_premium);
    }

    @Override
    protected String getItemClass() {
        return null;
    }
}
