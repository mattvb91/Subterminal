package mavonie.subterminal.Jobs.Skydive;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.birbit.android.jobqueue.TagConstraint;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Skydive.Skydive;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Sync skydive job
 */
public class SyncSkydive extends Job {

    private Skydive skydive;

    public SyncSkydive(Skydive skydive) {
        super(new Params(1).requireNetwork().persist().addTags(skydive.getJobTag()));
        this.skydive = skydive;

        //Cancel previous edits
        Subterminal.getJobManager(MainActivity.getActivity())
                .cancelJobsInBackground(null, TagConstraint.ANY, this.skydive.getJobTag());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        if (Subterminal.getUser().isLoggedIn()) {

            if (this.skydive.getDeleted().equals(Synchronizable.DELETED_TRUE)) {
                Subterminal.getApi().deleteSkydive(this.skydive);
            } else if (this.skydive.getSynced().equals(Synchronizable.SYNC_REQUIRED)) {
                Subterminal.getApi().syncSkydive(this.skydive);
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
