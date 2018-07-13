package com.uacf.hackathon.anomaly.processor.modules;


import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.google.inject.AbstractModule;
import com.uacf.hackathon.anomaly.processor.services.AmazonFirehoseClientProvider;
import com.uacf.hackathon.anomaly.processor.services.KinesisFirehoseService;

/**
 * An {@link AbstractModule} that binds the Service layer and Provide layer together to JVM during AWS Lambda
 * bootstrap.
 */
public class ProcessorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(KinesisFirehoseService.class);
		bind(AmazonKinesisFirehose.class).toProvider(AmazonFirehoseClientProvider.class);
	}
}
