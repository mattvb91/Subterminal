package mavonie.subterminal.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.birbit.android.jobqueue.TagConstraint;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Suit;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Synchronize Gear job
 */
public class SyncSuit extends Job {

    private Suit suit;

    public SyncSuit(Suit suit) {
        super(new Params(1).requireNetwork().persist().addTags(suit.getJobTag()));
        this.suit = suit;

        //Cancel previous edits
        Subterminal.getJobManager(MainActivity.getActivity())
                .cancelJobsInBackground(null, TagConstraint.ANY, this.suit.getJobTag());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        if (Subterminal.getUser().isLoggedIn()) {

            if (this.suit.getDeleted().equals(Synchronizable.DELETED_TRUE)) {
                Subterminal.getApi().deleteSuit(this.suit);
            } else if (this.suit.getSynced().equals(Synchronizable.SYNC_REQUIRED)) {
                Subterminal.getApi().syncSuit(this.suit);
            }
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
