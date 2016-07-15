package com.dotcms.web.websocket;

import javax.websocket.server.ServerEndpointConfig.Configurator;

import com.dotmarketing.business.APILocator;

/**
 * This {@link Configurator} class is in charge of the single instantiation of
 * the ServerSocket End points. It delegates to the
 * {@link WebSocketContainerAPI} the task to keep just one instance of the web
 * sockets. Consequently, we can get the end-point instance in any other place
 * of the application.
 *
 * @author jsanca
 * @version 3.7
 * @since Jul 12, 2016
 */
public class DotCmsWebSocketConfigurator extends Configurator {

	private WebSocketContainerAPI webSocketContainerAPI = APILocator.getWebSocketContainerAPI();

	@Override
	public <T> T getEndpointInstance(final Class<T> endpointClass) throws InstantiationException {
		return this.webSocketContainerAPI.getEndpointInstance(endpointClass);
	} // E:O:F:getEndpointInstance.

} // E:O:F:DotCmsWebSocketConfigurator.
