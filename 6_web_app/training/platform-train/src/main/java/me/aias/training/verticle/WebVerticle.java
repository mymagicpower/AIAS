/*
 * Copyright 2021 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package me.aias.training.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import static me.aias.training.verticle.DataVerticle.*;

public class WebVerticle extends AbstractVerticle {

	private final Logger LOGGER = LoggerFactory.getLogger(WebVerticle.class.getCanonicalName());
	private final int port = 8088;

	@Override
	public void start() {
		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());

		SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
		BridgeOptions options = new BridgeOptions();

		options.addInboundPermitted(new PermittedOptions().setAddress(ADDRESS_TRAINER_REQUEST));

		options.addOutboundPermitted(new PermittedOptions().setAddress(ADDRESS_TRAINER));

		// Event bus
		router.mountSubRouter("/api/eventbus", sockJSHandler.bridge(options));

		// Static content (UI)
		router.route("/*").handler(StaticHandler.create());
		router.route("/*").handler(rc -> {
			if (!rc.currentRoute().getPath().startsWith("/api")) {
				rc.reroute("/index.html");
			}
		});

		server.requestHandler(router).listen(port, http -> {
			if (http.succeeded()) {
				LOGGER.info("HTTP server started: http://localhost:{0}", String.valueOf(port));
			} else {
				LOGGER.info("HTTP server failed on port {0}", String.valueOf(port));
			}
		});
	}
}
