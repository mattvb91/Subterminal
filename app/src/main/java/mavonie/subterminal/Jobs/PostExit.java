package mavonie.subterminal.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.birbit.android.jobqueue.TagConstraint;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Exit;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Post exits job
 */
public class PostExit extends Job {

    private Exit exit;

    public PostExit(Exit exit) {
        super(new Params(1).requireNetwork().persist().addTags(exit.getJobTag()));
        this.exit = exit;

        //Cancel previous edits
        Subterminal.getJobManager(MainActivity.getActivity())
                .cancelJobsInBackground(null, TagConstraint.ANY, this.exit.getJobTag());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Subterminal.getApi().syncExit(this.exit);
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
