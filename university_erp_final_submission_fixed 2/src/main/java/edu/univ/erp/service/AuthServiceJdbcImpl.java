package edu.univ.erp.service;

import edu.univ.erp.auth.PasswordHasher;
import edu.univ.erp.data.ERPDaoJdbcImpl;
import edu.univ.erp.domain.UserAuth;

public class AuthServiceJdbcImpl implements AuthService {
    private final ERPDaoJdbcImpl dao;
    private final PasswordHasher hasher;

    public AuthServiceJdbcImpl(ERPDaoJdbcImpl dao, PasswordHasher hasher) {
        this.dao = dao; this.hasher = hasher;
    }

    @Override
    public UserAuth login(String username, String plainPassword) throws Exception {
        UserAuth u = dao.getUserAuthByUsername(username);
        if(u==null) throw new Exception("incorrect username or password");
        if(!hasher.verify(plainPassword, u.getPasswordHash())) throw new Exception("incorrect username or password");
        // update last login (optional)
        return u;
    }

    @Override
    public void changePassword(long userId, String oldPass, String newPass) throws Exception {
        throw new UnsupportedOperationException("changePassword not implemented yet");
    }
}
