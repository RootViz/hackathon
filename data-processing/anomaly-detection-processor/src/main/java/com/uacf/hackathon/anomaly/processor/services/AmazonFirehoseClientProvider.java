package com.uacf.hackathon.anomaly.processor.services;

import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClientBuilder;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * A {@link Provider} for {@link AmazonKinesisFirehose} client.
 */
@Singleton
public class AmazonFirehoseClientProvider implements Provider<AmazonKinesisFirehose> {

	@Override
	public AmazonKinesisFirehose get() {
		return AmazonKinesisFirehoseClientBuilder.defaultClient();
	}
}
