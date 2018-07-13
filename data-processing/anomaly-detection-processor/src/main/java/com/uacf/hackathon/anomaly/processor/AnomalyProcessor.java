package com.uacf.hackathon.anomaly.processor;

import static com.google.common.base.Preconditions.checkArgument;
import static com.uacf.hackathon.anomaly.processor.AnomalyProcessor.SeveritySignal.LOW;
import static com.uacf.hackathon.anomaly.processor.AnomalyProcessor.SeveritySignal.MEDIUM;
import static com.uacf.hackathon.anomaly.processor.AnomalyProcessor.SeveritySignal.SEVERE;
import static com.uacf.hackathon.anomaly.processor.AnomalyProcessor.SeveritySignal.SUPER_LOW;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.google.inject.Inject;
import com.uacf.hackathon.anomaly.lambda.framework.AbstractLambda;
import com.uacf.hackathon.anomaly.processor.dao.AnomalyDataRecord;
import com.uacf.hackathon.anomaly.processor.modules.ProcessorModule;
import com.uacf.hackathon.anomaly.processor.services.KinesisFirehoseService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class processes the result of anomaly detection produced by random forest algorithm and assigns labels based on
 * the anomaly score range as "LOW", "HIGH", "SEVERE"
 */
public class AnomalyProcessor extends AbstractLambda {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Inject
	protected KinesisFirehoseService kinesisFirehoseService;

	public AnomalyProcessor() {
		super(new ProcessorModule());
	}

	public enum SeveritySignal {

		SUPER_LOW("VOID"), LOW("LOW_LEVEL"), MEDIUM("MID_LEVEL"), SEVERE("SEVERE");

		protected String level;

		SeveritySignal(String action) {
			this.level = action;
		}
	}

	private static RangeMap<Double, SeveritySignal> SEVERITY_SIGNAL = TreeRangeMap.create();

	static {
		SEVERITY_SIGNAL.put(Range.lessThan(0.1), SUPER_LOW);
		SEVERITY_SIGNAL.put(Range.openClosed(0.1, 0.9), LOW);
		SEVERITY_SIGNAL.put(Range.closedOpen(1.0, 2.0), MEDIUM);
		SEVERITY_SIGNAL.put(Range.greaterThan(2.0), SEVERE);
	}


	private void processAnomalyStream(final KinesisEvent anomalyInputEvent, final Context lambdaContext) {

		final LambdaLogger lambdaContextLogger = lambdaContext.getLogger();
		checkArgument(kinesisFirehoseService.checkStreamStatus());

		// tagging logic
		List<AnomalyDataRecord> anomalyDataRecords = anomalyInputEvent.getRecords().stream()
			.map(AnomalyProcessor::parseInput)
			.map(AnomalyProcessor::tagSeverity)
			.collect(Collectors.toList());

		// send to clientProvider
		kinesisFirehoseService.send(anomalyDataRecords);
		lambdaContextLogger.log("total records sent" + anomalyDataRecords.size());

	}

	private static AnomalyDataRecord parseInput(final KinesisEventRecord record) {

		final byte[] data = record.getKinesis().getData().array();
		AnomalyDataRecord out = null;
		try {
			out = MAPPER.readValue(data, AnomalyDataRecord.class);
		} catch (IOException e) {
			LOGGER.error("Exception while parsing InputStream record " + Arrays.toString(data), e);
		}
		return out;
	}

	/**
	 * This function tags the anomaly record based on {@link AnomalyProcessor#SEVERITY_SIGNAL}
	 *
	 * @param record the
	 * @see AnomalyDataRecord
	 */
	private static AnomalyDataRecord tagSeverity(final AnomalyDataRecord record) {
		SeveritySignal tag = SEVERITY_SIGNAL.get(record.getAnomalyScore());
		record.setSignal(tag);
		return record;
	}
}
