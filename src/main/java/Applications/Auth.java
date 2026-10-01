package Applications;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Auth {
    public static Boolean authentification(String pass) {

        String password = "test";
        char[] passHash = BCrypt.withDefaults().hashToChar(12, password.toCharArray());
        BCrypt.Result result = BCrypt.verifyer().verify(pass.toCharArray(),passHash);
        return result.verified;
    }
}