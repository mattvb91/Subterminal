package mavonie.subterminal.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.birbit.android.jobqueue.TagConstraint;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Gear;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Synchronize Gear job
 */
public class SyncGear extends Job {

    private Gear gear;

    public SyncGear(Gear gear) {
        super(new Params(1).requireNetwork().persist().addTags(gear.getJobTag()));
        this.gear = gear;

        //Cancel previous edits
        Subterminal.getJobManager(MainActivity.getActivity())
                .cancelJobsInBackground(null, TagConstraint.ANY, this.gear.getJobTag());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        if (this.gear.getDeleted().equals(Synchronizable.DELETED_TRUE)) {
            Subterminal.getApi().deleteGear(this.gear);
        } else if (this.gear.getSynced().equals(Synchronizable.SYNC_REQUIRED)) {
            Subterminal.getApi().syncGear(this.gear);
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
