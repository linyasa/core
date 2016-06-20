package com.dotmarketing.cms.login;

import com.dotmarketing.cms.login.factories.LoginFactory;
import com.dotmarketing.cms.login.struts.LoginForm;
import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * Encapsulates the login services
 * This class is just a wrapper to encapsulate the {@link com.dotmarketing.cms.login.factories.LoginFactory}
 * This approach provides the ability to inject, proxy, mock, use diff implementation based on a contract etc.
 * @author jsanca
 */
public interface LoginService extends Serializable {

    default boolean doLogin(final LoginForm form,
                            final HttpServletRequest request,
                            final HttpServletResponse response) throws NoSuchUserException {

        return LoginFactory.doLogin(form, request, response);
    }

    default boolean doCookieLogin(final String encryptedId, final HttpServletRequest request, final HttpServletResponse response) {

        return LoginFactory.doCookieLogin(encryptedId, request, response);
    }

    default boolean doLogin(final String userName, final String password,
                            final boolean rememberMe, final HttpServletRequest request,
                                          final HttpServletResponse response) throws NoSuchUserException {

        return LoginFactory.doLogin(userName, password, rememberMe, request, response);
    }

    default boolean doLogin(final String userName, final String password,
                            final boolean rememberMe, final HttpServletRequest request,
                            final HttpServletResponse response,
                            final boolean skipPasswordCheck) throws NoSuchUserException {

        return LoginFactory.doLogin(userName, password, rememberMe,
                request, response, skipPasswordCheck);
    }

    default boolean doLogin(final String userName, final String password) throws NoSuchUserException {

        return LoginFactory.doLogin(userName, password);
    }

    default void doLogout(final HttpServletRequest request, final HttpServletResponse response) {

        LoginFactory.doLogout(request, response);
    }

    default boolean passwordMatch(final String password, final User user) {

        return passwordMatch(password, user);
    }
} // E:O:F:LoginService.
