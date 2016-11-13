package mavonie.subterminal.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.birbit.android.jobqueue.TagConstraint;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Jump;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Synchronize Jump job
 */
public class SyncJump extends Job {

    private Jump jump;

    public SyncJump(Jump jump) {
        super(new Params(1).requireNetwork().persist().addTags(jump.getJobTag()));
        this.jump = jump;

        //Cancel previous edits
        Subterminal.getJobManager(MainActivity.getActivity())
                .cancelJobsInBackground(null, TagConstraint.ANY, this.jump.getJobTag());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        if (this.jump.getDeleted().equals(Synchronizable.DELETED_TRUE)) {
            Subterminal.getApi().deleteJump(this.jump);
        } else if (this.jump.getSynced().equals(Synchronizable.SYNC_REQUIRED)) {
            Subterminal.getApi().syncJump(this.jump);
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
