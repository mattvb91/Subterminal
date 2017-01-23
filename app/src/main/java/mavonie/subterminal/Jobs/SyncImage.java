package mavonie.subterminal.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.birbit.android.jobqueue.TagConstraint;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Models.Synchronizable;
import mavonie.subterminal.Utils.Subterminal;

/**
 * Synchronize Jump job
 */
public class SyncImage extends Job {

    private Image image;

    public SyncImage(Image image) {
        super(new Params(1).requireNetwork().persist().addTags(image.getJobTag()));
        this.image = image;

        //Cancel previous edits
        Subterminal.getJobManager(MainActivity.getActivity())
                .cancelJobsInBackground(null, TagConstraint.ANY, this.image.getJobTag());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        if (Subterminal.getUser().isLoggedIn()) {

            if (this.image.getDeleted().equals(Synchronizable.DELETED_TRUE)) {
                Subterminal.getApi().deleteImage(this.image);
            } else if (this.image.getSynced().equals(Synchronizable.SYNC_REQUIRED)) {
                Subterminal.getApi().syncImage(this.image);
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
