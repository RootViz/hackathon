package com.uacf.hackathon.anomaly.processor.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uacf.hackathon.anomaly.processor.AnomalyProcessor.SeveritySignal;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A POJO from input Anomaly Data from clientProvider stream
 *
 * <p>
 * <pre>
 *         {
 *     "heartRate": 124,
 *     "speed": 2,
 *     "age": 60,
 *     "userId": "user_1",
 *     "workoutId": "workout_2",
 *     "rateType": "MEDIUM",
 *     "eventTime": "2018-07-12 21:14:00.000",
 *     "ANOMALY_SCORE": 0.5451929178281109
 * }
 *     </pre>
 * </p>
 */
public class AnomalyDataRecord implements Serializable {

	private final Integer heartRate;
	private final Integer speed;
	private final Integer age;
	private final String workoutId;
	private final String userId;
	private final String rateType;
	private final String eventTime;
	private final Double anomalyScore;
	private SeveritySignal signal;
	private final LocalDateTime date;

	public AnomalyDataRecord(@JsonProperty("heartRate") final Integer heartRate,
		@JsonProperty("speed") final Integer speed,
		@JsonProperty("age") final Integer age,
		@JsonProperty("userId") final String userId,
		@JsonProperty("workoutId") final String workoutId,
		@JsonProperty("rateType") final String rateType,
		@JsonProperty("eventTime") final String eventTime,
		@JsonProperty("ANOMALY_SCORE") final Double anomalyScore) {
		this.heartRate = heartRate;
		this.speed = speed;
		this.age = age;
		this.workoutId = workoutId;
		this.userId = userId;
		this.rateType = rateType;
		this.eventTime = eventTime;
		this.anomalyScore = anomalyScore;
		this.signal = SeveritySignal.LOW;
		this.date = LocalDateTime.now();
	}


	public Double getAnomalyScore() {
		return anomalyScore;
	}

	public void setSignal(SeveritySignal signal) {
		this.signal = signal;
	}

	@Override
	public String toString() {
		return "AnomalyDataRecord{" +
			"heartRate=" + heartRate +
			", speed=" + speed +
			", age=" + age +
			", workoutId='" + workoutId + '\'' +
			", userId='" + userId + '\'' +
			", rateType='" + rateType + '\'' +
			", eventTime='" + eventTime + '\'' +
			", anomalyScore=" + anomalyScore +
			", signal=" + signal +
			'}';
	}

}
