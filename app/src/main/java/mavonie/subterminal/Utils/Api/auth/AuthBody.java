package mavonie.subterminal.Utils.Api.auth;

/**
 * AuthBody class for retrofit endpoint
 */
public class AuthBody {

    private String email;
    private String password;

    public AuthBody(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
