package mavonie.subterminal.Models;


import android.content.ContentValues;
import android.database.Cursor;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

import io.card.payment.CreditCard;
import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Utils.Subterminal;
import mavonie.subterminal.Utils.UIHelper;

/**
 * Payment class
 */
public class Payment extends Model {

    private io.card.payment.CreditCard creditCard;

    public Payment(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    Model populateFromCursor(Cursor cursor) {
        return null;
    }

    @Override
    void populateContentValues(ContentValues contentValues) {

    }

    @Override
    String getTableName() {
        return null;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    /**
     * Submit and wait for token/send to API
     */
    public void submit() {

        Card card = new Card(
                this.getCreditCard().cardNumber,
                this.getCreditCard().expiryMonth,
                this.getCreditCard().expiryYear,
                this.getCreditCard().cvv
        );

        Stripe stripe = null;
        String publishKey = null;

        publishKey = Subterminal.getMetaData(MainActivity.getActivity().getApplicationContext(), "mavonie.subterminal.STRIPE_PUBLISHABLE_KEY");

        try {
            stripe = new Stripe(publishKey);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        UIHelper.loadSpinner();

        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        // Send token to your server
                        Subterminal.getApi().sendPaymentToken(token);
                    }

                    public void onError(Exception error) {
                        // Show localized error message
                        UIHelper.toast(error.getLocalizedMessage());
                    }
                }
        );
    }
}
