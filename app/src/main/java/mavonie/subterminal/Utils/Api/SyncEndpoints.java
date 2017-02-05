package mavonie.subterminal.Utils.Api;

import java.util.List;

import mavonie.subterminal.Models.Synchronizable;
import retrofit2.Call;

/**
 * Every synchronizable model has these calls associated
 * with it.
 */
public interface SyncEndpoints<A extends Synchronizable> {

    /**
     * Create or update an entity
     */
    Call<A> getSyncEndpoint();

    /**
     * Delete an entity
     */
    Call<Void> getDeleteEndpoint();

    /**
     * Download entities
     */
    Call<List<A>> getDownloadEndpoint();

    /**
     * The identifier for the synchronization
     *
     * @return String
     */
    String getSyncIdentifier();
}
