package mavonie.subterminal.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.birbit.android.jobqueue.TagConstraint;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Post exits job
 */
public class SyncModel extends Job {

    private Synchronizable model;

    public SyncModel(Synchronizable model) {
        super(new Params(1).requireNetwork().persist().addTags(model.getJobTag()));
        this.model = model;

        //Cancel previous edits
        Subterminal.getJobManager(MainActivity.getActivity())
                .cancelJobsInBackground(null, TagConstraint.ANY, this.model.getJobTag());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        if (Subterminal.getUser().isLoggedIn()) {

            if (this.model.getDeleted().equals(Synchronizable.DELETED_TRUE)) {
                Subterminal.getApi().deleteModel(this.model);
            } else if (this.model.getSynced().equals(Synchronizable.SYNC_REQUIRED)) {
                Subterminal.getApi().syncModel(this.model);
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
