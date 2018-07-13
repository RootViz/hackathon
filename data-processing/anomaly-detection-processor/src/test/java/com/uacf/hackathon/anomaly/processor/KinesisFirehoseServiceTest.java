package com.uacf.hackathon.anomaly.processor;

import static org.junit.Assert.assertNotNull;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.google.inject.Inject;
import com.uacf.hackathon.anomaly.processor.dao.AnomalyDataRecord;
import com.uacf.hackathon.anomaly.processor.modules.ProcessorModule;
import com.uacf.hackathon.anomaly.processor.services.KinesisFirehoseService;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * A test class for  {@link KinesisFirehoseService}
 */
@RunWith(GuiceTestRunner.class)
@GuiceModules(ProcessorModule.class)
public class KinesisFirehoseServiceTest {

	@Inject
	private KinesisFirehoseService service;

	private static final AnomalyDataRecord DATA_RECORD = new AnomalyDataRecord(1, 2, 3, "1",
		"1", "MEDIUM", "boom", 2.0);

	@Test
	public void testSerialize() throws Exception {
		assertNotNull(SerializationUtils.serialize(DATA_RECORD));
		System.out.println(String.valueOf(DATA_RECORD));
	}

	@Test
	public void sendRecord() throws Exception {
		service.setStreamName("ElasticsearchIndexStreamS3");
		service.send(Arrays.asList(DATA_RECORD));
	}

	@Test
	    public void date() throws Exception{
		String date="2018-07-13 00:29:00.000";
		System.out.println(LocalDateTime.now());


	}
}
