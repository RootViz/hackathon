package com.uacf.hackathon.anomaly.lambda.framework;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Abtract class that does bootstrapping for any lambda projects to load the {@link Guice} {@link Module} to the AWS
 * lambda runtime enviroment JVM to have the service object pool available for dependency injection at runtime.
 */
public class AbstractLambda {

	/**
	 * slf4j logger
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(AbstractLambda.class);

	/**
	 * The function's Guice injector.
	 */
	private Injector injector;

	/**
	 * The list of service modules in the application.
	 */
	private List<Module> serviceModules = new ArrayList<>();

	public AbstractLambda(Module... modules) {
		serviceModules.addAll(Arrays.asList(modules));
		injector = Guice.createInjector(serviceModules);
		LOGGER.info("total loaded modules " + serviceModules.size());
		injector.injectMembers(this);
	}

	/**
	 * Gets an instance of a service.
	 *
	 * @param serviceClass The class of service to get.
	 * @param <T> The type of service to return.
	 * @return The service instance.
	 */
	public <T> T getService(Class<T> serviceClass) {
		return injector.getInstance(serviceClass);
	}
}
