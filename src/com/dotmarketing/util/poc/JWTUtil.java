package com.dotmarketing.util.poc;

import com.dotmarketing.util.Config;
import com.dotmarketing.util.UtilMethods;
import com.liferay.portal.util.CookieKeys;
import com.liferay.util.CookieUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Date;

import static com.liferay.util.CookieUtil.COOKIES_HTTP_ONLY;
import static com.liferay.util.CookieUtil.COOKIES_SECURE_FLAG;

/**
 * @author Jonathan Gamba
 *         5/16/16
 */
public class JWTUtil {

    // We need a signing key, so we'll create one just for this example. Usually
    // the key would be read from your application configuration instead.
    private static Key signingKey = MacProvider.generateKey();//TODO/FIXME: What is the proper way to get/generate this key?

    public static String generateToken(String id, String subject, String issuer, long ttlMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if ( ttlMillis >= 0 ) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    //Sample method to validate and read the JWT
    public static void parseToken(String accessToken) {

        if ( !UtilMethods.isSet(accessToken) ) {
            throw new IllegalArgumentException("Security Token not found");
        }

        //This line will throw an exception if it is not a signed JWS (as expected)
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(accessToken);

        if ( jws != null ) {
            Claims body = jws.getBody();
            System.out.println("ID: " + body.getId());
            System.out.println("Subject: " + body.getSubject());
            System.out.println("Issuer: " + body.getIssuer());
            System.out.println("Expiration: " + body.getExpiration());
        }
    }

    public static void createJWTCookie(HttpServletRequest request, HttpServletResponse response, String accessToken) {

        // if the JSESSIONID cookie does not exists create it
        if ( request.getCookies() == null || !CookieUtil.containsCookie(request.getCookies(), CookieKeys.JWT_ACCESS_TOKEN) ) {

            //Set-Cookie: access_token=eyJhbGciOiJIUzI1NiIsI.eyJpc3MiOiJodHRwczotcGxlL.mFrs3Zo8eaSNcxiNfvRh9dqKP4F1cB; Secure; HttpOnly;

            Cookie cookie = new Cookie(CookieKeys.JWT_ACCESS_TOKEN, accessToken);
            if ( Config.getBooleanProperty(COOKIES_HTTP_ONLY, false) ) {
                // add secure and httpOnly flag to the cookie
                cookie.setHttpOnly(true);
            }

            if ( Config.getStringProperty(COOKIES_SECURE_FLAG, "https").equals("always")
                    || (Config.getStringProperty(COOKIES_SECURE_FLAG, "https").equals("https") && request.isSecure()) ) {
                cookie.setSecure(true);
            }

            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

}