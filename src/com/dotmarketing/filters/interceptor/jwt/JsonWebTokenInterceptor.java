package com.dotmarketing.filters.interceptor.jwt;

import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.UserAPI;
import com.dotmarketing.cms.login.LoginService;
import com.dotmarketing.cms.login.LoginServiceFactory;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.filters.interceptor.WebInterceptor;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.util.jwt.DotCMSSubjectBean;
import com.dotmarketing.util.jwt.JWTBean;
import com.dotmarketing.util.jwt.JsonWebTokenFactory;
import com.dotmarketing.util.jwt.JsonWebTokenService;
import com.dotmarketing.util.marshal.MarshalFactory;
import com.dotmarketing.util.marshal.MarshalUtils;
import com.dotmarketing.util.security.Encryptor;
import com.dotmarketing.util.security.EncryptorFactory;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.ejb.CompanyLocalManager;
import com.liferay.portal.ejb.CompanyLocalManagerFactory;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.util.CookieKeys;
import com.liferay.portal.util.PortalUtil;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
/**
 * This Interceptor is useful to active the remember me using jwt
 * It is going to look for a cookie and try to get the access token from it.
 *
 * Usually the cookie should runs under https, but you can avoid https by using the JSON_WEB_TOKEN_ALLOW_HTTP property in true.
 * @author jsanca
 */
@SuppressWarnings("serial")
public class JsonWebTokenInterceptor implements WebInterceptor {


    public static final String JSON_WEB_TOKEN_ALLOW_HTTP = "json.web.token.allowhttp";

    private JsonWebTokenService jsonWebTokenService =
            JsonWebTokenFactory.getInstance().getJsonWebTokenService();

    private MarshalUtils marshalUtils =
            MarshalFactory.getInstance().getMarshalUtils();

    private UserAPI userAPI = null;

    private CompanyLocalManager companyLocalManager =
            CompanyLocalManagerFactory.getManager();

    private Encryptor encryptor =
            EncryptorFactory.getInstance().getEncryptor();

    private LoginService loginService =
            LoginServiceFactory.getInstance().getLoginService();

    @Override
    public void destroy() {

    }

    @Override
    public void init() {

    }

    @Override
    public boolean intercept(final HttpServletRequest req, final HttpServletResponse res) throws IOException {

        if (!this.isLoggedIn(req)) {

            if (Config.getBooleanProperty(JSON_WEB_TOKEN_ALLOW_HTTP, false) || this.isHttpSecure(req)) {

                try {
                    
                    this.processJwtCookie(res, req);
                } catch (Exception e) {

                    if (Logger.isErrorEnabled(JsonWebTokenInterceptor.class)) {

                        Logger.error(JsonWebTokenInterceptor.class,
                                e.getMessage(), e);
                    }
                }
            }
        }

        return true;
    }

    protected boolean isLoggedIn (final HttpServletRequest req) {

        return null != PortalUtil.getUserId(req);
    }

    protected void processJwtCookie(final HttpServletResponse response,
                                    final HttpServletRequest request) {

        final String jwtAccessToken =
                UtilMethods.getCookieValue(
                        HttpServletRequest.class.cast(request).getCookies(),
                        CookieKeys.JWT_ACCESS_TOKEN);

        if (null != jwtAccessToken) {

            this.parseJwtToken(jwtAccessToken, response, request);
        }
    }

    protected void parseJwtToken(final String jwtAccessToken,
                                 final HttpServletResponse response,
                                 final HttpServletRequest request) {

        final JWTBean jwtBean =
                this.jsonWebTokenService.parseToken(jwtAccessToken);

        if (null != jwtBean) {

            final long millis = jwtBean.getTtlMillis();

            if (this.stillValid (millis)) {
// todo: handle exceptions here
                this.processSubject(jwtBean, response, request);
            }
        }
    }

    protected boolean stillValid(final long userMillis) {

        // this means the userMillis still being valid (in the future)
        return userMillis - System.currentTimeMillis() > 0;
    }

    protected void processSubject(final JWTBean jwtBean,
                                  final HttpServletResponse response,
                                  final HttpServletRequest request) {


        final DotCMSSubjectBean dotCMSSubjectBean =
                this.marshalUtils.unmarshal(jwtBean.getSubject(), DotCMSSubjectBean.class);

        if (null != dotCMSSubjectBean) {

            this.performAuthentication (dotCMSSubjectBean, response, request);
        }
    }

    protected void performAuthentication(final DotCMSSubjectBean subjectBean,
                                         final HttpServletResponse response,
                                         final HttpServletRequest request) {

        final Company company;
        final String userId;

        try {

            company = this.getCompany(subjectBean.getCompanyId());

            userId = this.encryptor.decrypt(company.getKeyObj(),
                    subjectBean.getUserId()); // encrypt the user id.

            // todo: if there is a custom implementation to handle the authentication use it
            this.performDefaultAuthentication
                    (company, userId, subjectBean.getLastModified(),
                            response, request);
        } catch (Exception e) {

            if (Logger.isErrorEnabled(JsonWebTokenInterceptor.class)) {

                Logger.error(JsonWebTokenInterceptor.class,
                        e.getMessage(), e);
            }
        }
    }

    protected Company getCompany (final String companyId) throws SystemException, PortalException {

        return this.companyLocalManager.getCompany(companyId);
    }

    protected void performDefaultAuthentication(final Company company,
                                                final String userId,
                                                final Date lastModified,
                                                final HttpServletResponse response,
                                                final HttpServletRequest request) throws SystemException, PortalException, DotSecurityException, DotDataException {

        final User user = this.getUserAPI().loadUserById(userId);

        if (null != user) {

            // the login haven't change since we created the web token
            if (0 == user.getModificationDate().compareTo(lastModified)) {

                this.loginService.
                        doCookieLogin(this.encryptor.encryptString(userId), request, response);
            }
        }
    }

    /**
     * Determine if the request is running under https protocol
     * @param req {@link ServletRequest}
     * @return boolean
     */
    protected boolean isHttpSecure(final ServletRequest req) {

        return req.isSecure();
    } // isHttpSecure.

    public void setJsonWebTokenService(final JsonWebTokenService jsonWebTokenService) {
        this.jsonWebTokenService = jsonWebTokenService;
    }

    public void setMarshalUtils(final MarshalUtils marshalUtils) {
        this.marshalUtils = marshalUtils;
    }

    public void setUserAPI(final UserAPI userAPI) {
        this.userAPI = userAPI;
    }

    private synchronized UserAPI getUserAPI () {

        if (null == this.userAPI) {

            this.userAPI =
                    APILocator.getUserAPI();
        }

        return this.userAPI;
    }

    public void setCompanyLocalManager(final CompanyLocalManager companyLocalManager) {
        this.companyLocalManager = companyLocalManager;
    }

    public void setEncryptor(final Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    public void setLoginService(final LoginService loginService) {
        this.loginService = loginService;
    }
} // E:O:F:JsonWebTokenInterceptor.
