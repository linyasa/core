package com.dotmarketing.filters.interceptor.jwt;

import com.dotmarketing.business.APILocator;
import com.dotmarketing.cms.factories.PublicEncryptionFactory;
import com.dotmarketing.cms.login.factories.LoginFactory;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.filters.interceptor.WebInterceptor;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.util.WebKeys;
import com.dotmarketing.util.jwt.DotCMSSubjectBean;
import com.dotmarketing.util.jwt.JWTBean;
import com.dotmarketing.util.jwt.JsonWebTokenFactory;
import com.dotmarketing.util.jwt.JsonWebTokenService;
import com.dotmarketing.util.marshal.MarshalFactory;
import com.dotmarketing.util.marshal.MarshalUtils;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.ejb.CompanyLocalManagerUtil;
import com.liferay.portal.ejb.UserLocalManagerUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.util.CookieKeys;
import com.liferay.util.Encryptor;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * This Interceptor is useful to active the remember me using jwt
 * @author jsanca
 */
public class JsonWebTokenInterceptor implements WebInterceptor {


    public static final String JSON_WEB_TOKEN_ALLOW_HTTP = "json.web.token.allowhttp";

    @Override
    public void destroy() {

    }

    @Override
    public void init() {

    }

    @Override
    public boolean intercept(final ServletRequest req, final ServletResponse res) throws IOException {

        if (Config.getBooleanProperty(JSON_WEB_TOKEN_ALLOW_HTTP, false) || this.isHttpSecure (req)) {

            this.processJwtCookie(
                    HttpServletResponse.class.cast(req),
                    HttpServletRequest.class.cast(res));
        }

        return true;
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

        final JsonWebTokenService jsonWebTokenService =
                JsonWebTokenFactory.getInstance().getJsonWebTokenService();

        final JWTBean jwtBean =
                jsonWebTokenService.parseToken(jwtAccessToken);

        if (null != jwtBean) {

            final long millis = jwtBean.getTtlMillis();

            if (this.stillValid (millis)) {

                this.processSubject(jwtBean, response, request);
            }
        }
    }

    protected boolean stillValid(final long userMillis) {

        // this means the userMillis still being valid (in the future)
        return System.currentTimeMillis() - userMillis > 0;
    }

    protected void processSubject(final JWTBean jwtBean,
                                  final HttpServletResponse response,
                                  final HttpServletRequest request) {

        final MarshalUtils marshalUtils =
                MarshalFactory.getInstance().getMarshalUtils();

        final DotCMSSubjectBean dotCMSSubjectBean =
                marshalUtils.unmarshal(jwtBean.getSubject(), DotCMSSubjectBean.class);

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

            company = CompanyLocalManagerUtil.getCompany
                    (subjectBean.getCompanyId());

            userId = Encryptor.decrypt(company.getKeyObj(),
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

    protected void performDefaultAuthentication(final Company company,
                                                final String userId,
                                                final Date lastModified,
                                                final HttpServletResponse response,
                                                final HttpServletRequest request) throws SystemException, PortalException, DotSecurityException, DotDataException {

        final User user = APILocator.getUserAPI().loadUserById(userId);

        if (null != user) {

            // the login haven't change since we created the web token
            if (0 == user.getModificationDate().compareTo(lastModified)) {

                LoginFactory.doCookieLogin(PublicEncryptionFactory.encryptString(userId), request, response);
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
} // E:O:F:JsonWebTokenInterceptor.
