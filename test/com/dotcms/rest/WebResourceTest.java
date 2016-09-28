package com.dotcms.rest;

import com.dotcms.TestBase;
import com.dotcms.repackage.javax.ws.rs.NotAuthorizedException;
import com.dotcms.repackage.javax.ws.rs.client.Client;
import com.dotcms.repackage.javax.ws.rs.client.WebTarget;
import com.dotcms.repackage.org.codehaus.cargo.util.Base64;
import com.dotcms.repackage.org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import com.dotcms.rest.config.RestServiceUtil;
import com.dotcms.rest.exception.SecurityException;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.ApiProvider;
import com.dotmarketing.business.LayoutAPI;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.servlets.test.ServletTestRunner;
import com.liferay.portal.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WebResourceTest extends TestBase {

    private Client client;
    private WebTarget webTarget;
    private HttpServletRequest request;
    private String serverName;
    private Integer serverPort;

    @BeforeEach
    public void init() {
        client = RestClientBuilder.newClient();
        request = ServletTestRunner.localRequest.get();
        serverName = request.getServerName();
        serverPort = request.getServerPort();
        webTarget = client.target("http://" + serverName + ":" + serverPort + "/api/role");
        RestServiceUtil.addResource(DummyResource.class);
    }

    @Test
    public void testAuthenticateNoUser() {
        assertThrows(NotAuthorizedException.class, () -> webTarget.path("/loadchildren/").request().get(String.class));
    }

    @Test
    public void testAuthenticateInvalidUserInURL() {
        assertThrows(NotAuthorizedException.class, () -> webTarget.path("/loadchildren/user/wrong@user.com/password/123456").request().get(String.class));
    }

    @Test
    public void testAuthenticateValidUserInURL() {
        String response = webTarget.path("/loadchildren/user/admin@dotcms.com/password/admin").request().get(String.class);
        assertNotNull(response);
    }

    @Test
    public void testAuthenticateInvalidUserBasicAuth() {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("wrong@user.com", "123456");
        client.register(feature);
        webTarget = client.target("http://" + serverName + ":" + serverPort + "/api/role");
        assertThrows(NotAuthorizedException.class, () -> webTarget.path("/loadchildren/").request().get(String.class));
    }

    @Test
    public void testAuthenticateValidUserBasicAuth() {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin@dotcms.com", "admin");
        client.register(feature);
        webTarget = client.target("http://" + serverName + ":" + serverPort + "/api/role");
        String response = webTarget.path("/loadchildren/").request().get(String.class);
        assertNotNull(response);
    }

    @Test
    public void testAuthenticateInvalidUserHeaderAuth() {
        assertThrows(NotAuthorizedException.class, () -> webTarget.path("/loadchildren/").request().header("DOTAUTH", Base64.encode("wrong@user.com:123456")).get(String.class));
    }

    @Test
    public void testAuthenticateValidUserHeaderAuth() {
        String response = webTarget.path("/loadchildren/").request().header("DOTAUTH", Base64.encode("admin@dotcms.com:admin")).get(String.class);
        assertNotNull(response);
    }

    @Test
    public void testUserWithoutPermissionOnPortlet() throws DotDataException {
        final String requiredPortlet = "veryCoolPortlet";
        LayoutAPI mockLayoutAPI = mock(LayoutAPI.class);
        User user = APILocator.getUserAPI().getUsersByName("Admin User", 0, 0, APILocator.getUserAPI().getSystemUser(), false).get(0);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("DOTAUTH")).thenReturn(Base64.encode("admin@dotcms.com:admin"));
        when(mockLayoutAPI.doesUserHaveAccessToPortlet(requiredPortlet, user)).thenReturn(false);

        ApiProvider mockProvider = mock(ApiProvider.class);
        when(mockProvider.layoutAPI()).thenReturn(mockLayoutAPI);
        when(mockProvider.userAPI()).thenReturn(APILocator.getUserAPI());

        WebResource webResource = new WebResource(mockProvider);
        assertThrows(SecurityException.class, () -> webResource.init(null, true, request, true, requiredPortlet));
    }

    @Test
    public void testUserWithPermissionOnPortlet() throws DotDataException {
        final String requiredPortlet = "veryCoolPortlet";
        LayoutAPI mockLayoutAPI = mock(LayoutAPI.class);
        User user = APILocator.getUserAPI().getUsersByName("Admin User", 0, 0, APILocator.getUserAPI().getSystemUser(), false).get(0);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("DOTAUTH")).thenReturn(Base64.encode("admin@dotcms.com:admin"));
        when(mockLayoutAPI.doesUserHaveAccessToPortlet(requiredPortlet, user)).thenReturn(true);

        ApiProvider mockProvider = mock(ApiProvider.class);
        when(mockProvider.layoutAPI()).thenReturn(mockLayoutAPI);
        when(mockProvider.userAPI()).thenReturn(APILocator.getUserAPI());

        WebResource webResource = new WebResource(mockProvider);
        InitDataObject data = webResource.init(null, true, request, true, requiredPortlet);
        assertNotNull(data);
    }


}
