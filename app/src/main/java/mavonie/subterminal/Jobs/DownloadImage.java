package mavonie.subterminal.Jobs;

import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mavonie.subterminal.MainActivity;
import mavonie.subterminal.Models.Image;
import mavonie.subterminal.Utils.Subterminal;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mavon on 21/01/17.
 */

public class DownloadImage extends Job {

    private Image image;

    public DownloadImage(Image image) {
        super(new Params(1).requireNetwork().persist().addTags(image.getFilename() + "_DOWNLOAD"));
        this.image = image;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        okhttp3.OkHttpClient client = Subterminal.getApi().getOkHttpClient();

        Request request = new Request.Builder()
                .url(Subterminal.getMetaData(MainActivity.getActivity(), "mavonie.subterminal.API_URL") + "user/image?filename=" + image.getFilename())
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            InputStream inStream = response.body().byteStream(); // Read the data from the stream

            try {
                String fname = Image.generateFilename();

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + Image.IMAGE_PATH);
                myDir.mkdirs();

                OutputStream out = new FileOutputStream(new File(myDir, fname));
                byte[] buf = new byte[1024];
                int len;
                while ((len = inStream.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                inStream.close();

                image.setFilename(fname);
                image.markSynced();

            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Couldnt save image");
            }
        }else{
            throw new IOException("Bad image request");
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.createExponentialBackoff(runCount, 1000);
    }
}
