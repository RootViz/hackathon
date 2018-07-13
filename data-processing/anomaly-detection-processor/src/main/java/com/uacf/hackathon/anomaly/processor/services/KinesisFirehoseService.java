package com.uacf.hackathon.anomaly.processor.services;


import com.amazonaws.services.kinesis.model.ResourceNotFoundException;
import com.amazonaws.services.kinesisfirehose.model.DescribeDeliveryStreamRequest;
import com.amazonaws.services.kinesisfirehose.model.DescribeDeliveryStreamResult;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.PutRecordResult;
import com.amazonaws.services.kinesisfirehose.model.Record;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.uacf.hackathon.anomaly.processor.dao.AnomalyDataRecord;
import java.nio.ByteBuffer;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Service class that provides functionality to send and receive data from AWS firehose stream.
 *
 * Will contain methods like:
 *
 * <pre>
 *      1. single put record
 *      2. bulk put
 *      3. single put using KPL
 *      4. bulk put using KPL
 *  </pre>
 */
@Singleton
public class KinesisFirehoseService {

	private String streamName = System.getenv("STREAM_NAME");
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Logger LOGGER = LoggerFactory.getLogger(KinesisFirehoseService.class);
	@Inject
	AmazonFirehoseClientProvider clientProvider;

	/**
	 * Method checks the status of the clientProvider Stream and returns boolean status.
	 *
	 * @return boolean status true or false.
	 */
	public boolean checkStreamStatus() {
		DescribeDeliveryStreamRequest describeHoseRequest = new DescribeDeliveryStreamRequest()
			.withDeliveryStreamName(streamName);
		DescribeDeliveryStreamResult describeHoseResult;
		String status = "UNDEFINED";

		try {
			describeHoseResult = clientProvider.get().describeDeliveryStream(describeHoseRequest);
			status = describeHoseResult.getDeliveryStreamDescription().getDeliveryStreamStatus();
			LOGGER.info("Stream " + streamName + " has a status of "
				+ status);

		} catch (ResourceNotFoundException ex) {
			LOGGER.error("Stream " + streamName + " does not exist.", ex);
		}

		return !status.isEmpty() && status.equals("ACTIVE");

	}


	/**
	 * Sets List of {@link AnomalyDataRecord} to clientProvider stream.
	 *
	 * @param recordList of AnomalyDataRecord to be sent to clientProvider stream
	 */
	public void send(final List<AnomalyDataRecord> recordList) {
		recordList.forEach(this::putRecord);
	}

	/**
	 * Sends record to clientProvider stream.
	 *
	 * @param `record` anomaly record to be sent to elasticsearch to index.
	 */
	@SuppressWarnings("JavaDoc")
	private void putRecord(AnomalyDataRecord record) {

		String jsonData;

		try {
			jsonData = MAPPER.writeValueAsString(record).replaceAll("\\s+", "") + "\n";
			Record deliveryStreamRecord = new Record().withData(ByteBuffer.wrap(jsonData.getBytes()));

			PutRecordRequest putRecordRequest = new PutRecordRequest()
				.withDeliveryStreamName(streamName)
				.withRecord(deliveryStreamRecord);

			PutRecordResult result = clientProvider.get().putRecord(putRecordRequest);
			System.out.println(result.getSdkHttpMetadata().getHttpStatusCode());
		} catch (Exception ignored) {

		}

	}

	public void setStreamName(final String streamName) {
		this.streamName = streamName;
	}
}
