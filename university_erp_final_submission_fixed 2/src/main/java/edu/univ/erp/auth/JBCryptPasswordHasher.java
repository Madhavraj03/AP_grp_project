package edu.univ.erp.auth;

import org.mindrot.jbcrypt.BCrypt;

public class JBCryptPasswordHasher implements PasswordHasher {
    @Override
    public String hash(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(12));
    }
    @Override
    public boolean verify(String plain, String hash) {
        if(hash == null) return false;
        return BCrypt.checkpw(plain, hash);
    }
}
