package mavonie.subterminal.Jobs.Skydive;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.birbit.android.jobqueue.TagConstraint;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Skydive.Rig;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Sync rig job
 */
public class SyncRig extends Job {

    private Rig rig;

    public SyncRig(Rig rig) {
        super(new Params(1).requireNetwork().persist().addTags(rig.getJobTag()));
        this.rig = rig;

        //Cancel previous edits
        Subterminal.getJobManager(MainActivity.getActivity())
                .cancelJobsInBackground(null, TagConstraint.ANY, this.rig.getJobTag());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        if (Subterminal.getUser().isLoggedIn()) {

            if (this.rig.getDeleted().equals(Synchronizable.DELETED_TRUE)) {
                Subterminal.getApi().deleteRig(this.rig);
            } else if (this.rig.getSynced().equals(Synchronizable.SYNC_REQUIRED)) {
                Subterminal.getApi().syncRig(this.rig);
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
