/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package instrumentation;

import java.util.logging.Logger;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.ignore.IgnoredTypesBuilder;
import io.opentelemetry.javaagent.extension.ignore.IgnoredTypesConfigurer;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;

@AutoService(IgnoredTypesConfigurer.class)
public class SocketIgnoredTypesConfigurer implements IgnoredTypesConfigurer {

	private static Logger logger = Logger.getLogger(SocketIgnoredTypesConfigurer.class.getName());

	@Override
	public void configure(IgnoredTypesBuilder builder, ConfigProperties config) {
		logger.info(" Allowing class java.net.Socket...");
		builder.allowClass("java.net.Socket");
	}
}