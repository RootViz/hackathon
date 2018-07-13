/**
 * Welcome to the SQL editor
 * =========================
 *
 * The SQL code you write here will continuously transform your streaming data
 * when your application is running.
 *
 * Get started by clicking "Add SQL from templates" or pull up the
 * documentation and start writing your own custom queries.
 * demo event:
   {"heartRate": 64, "elevation": 52, "workoutId": 0, "age": 72, "userId": 1,
   "rateType": "NORMAL", "height": 177, "speed": 2, "cadence": 71}
 */
--Creates a temporary stream.
CREATE OR REPLACE STREAM "HEART_RATE_TEMP_STREAM" (
	        "heartRate"        INTEGER,
          "speed"            INTEGER,
          "age"              INTEGER,
          "userId"           varchar(20),
          "workoutId"        varchar(20),
	        "rateType"         varchar(20),
	        "ANOMALY_SCORE"    DOUBLE);

--Creates another stream for application output.
CREATE OR REPLACE STREAM "HEART_RATE_DESTINATION_SQL_STREAM" (
          "heartRate"        INTEGER,
          "speed"            INTEGER,
          "age"              INTEGER,
          "userId"           varchar(20),
          "workoutId"        varchar(20),
	        "rateType"         varchar(20),
          "eventTime"        timestamp,
	        "ANOMALY_SCORE"    DOUBLE);

-- Compute an anomaly score for each record in the input stream based on userId and workoutId
-- for each metric and dependent variables
-- using Random Cut Forest

CREATE OR REPLACE PUMP "HEART_RATE_STREAM_PUMP" AS
   INSERT INTO "HEART_RATE_TEMP_STREAM"
      SELECT STREAM "heartRate","speed","age","userId","workoutId","rateType", ANOMALY_SCORE
      FROM TABLE(RANDOM_CUT_FOREST(
              CURSOR(SELECT STREAM * FROM "SOURCE_SQL_STREAM_001"),400,400,20000,15));

-- Sort records by descending anomaly score, insert into output stream
CREATE OR REPLACE PUMP "HEART_RATE_OUTPUT_PUMP" AS
   INSERT INTO "HEART_RATE_DESTINATION_SQL_STREAM"
      SELECT STREAM "heartRate","speed","age","userId","workoutId","rateType",STEP("HEART_RATE_TEMP_STREAM".ROWTIME BY INTERVAL '60' SECOND) AS "eventTime", ANOMALY_SCORE FROM "HEART_RATE_TEMP_STREAM"
      ORDER BY FLOOR("HEART_RATE_TEMP_STREAM".ROWTIME TO SECOND), ANOMALY_SCORE DESC;
