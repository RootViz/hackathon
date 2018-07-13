import json
from boto import kinesis
import random
import time

kinesis = kinesis.connect_to_region("us-east-1")
streamName="RunAppEventInputStream"
# generate normal metrics with probability .99
def getNormalMetrics():
    data = {}
    #identifiers
    data['userId']="user_"+str(random.randint(0,3))
    data['workoutId']="workout_"+str(random.randint(0,3))
    #age and heartrate are dependent
    data['age']=random.randint(18,80)
    data['heartRate'] = random.randint(60, 100)
    #cadence and height are dependent
    #data['height']=random.randint(74,240)
    #data['cadence'] = random.randint(15, 89)
    #other metrics
    data['speed'] = random.randint(1, 2)
    #data['elevation'] = random.randint(28, 70)
    #type
    data['rateType'] = "NORMAL"
    return data
# generate medium metrics  with probability .01 (very few)
def getMediumMetrics():
    data = {}
    #identifiers
    data['userId']="user_"+str(random.randint(0,3))
    data['workoutId']="workout_"+str(random.randint(0,3))
    #age and heartrate are dependent
    data['age']=random.randint(18,80)
    data['heartRate'] = random.randint(101, 150)
    #cadence and height are dependent
    #data['height']=random.randint(74,240)
    #data['cadence'] = random.randint(89,120)
    #other metrics
    data['speed'] = random.randint(2, 3)
    #data['elevation'] = random.randint(71, 120)
    #type
    data['rateType'] = "MEDIUM"
    return data

# generate high metrics with probability .01 (very few)
#this is mostly anomalous data.
def getHighMetrics():
    data = {}
    #identifiers
    data['userId']="user_"+str(random.randint(0,3))
    data['workoutId']="workout_"+str(random.randint(0,3))
    #age and heartrate are dependent
    data['age']=random.randint(18,80)
    data['heartRate'] = random.randint(150, 170)
    #cadence and height are dependent
    #data['height']=random.randint(74,240)
    #data['cadence'] = random.randint(121,180)
    #other metrics
    data['speed'] = random.randint(10, 300)
    #data['elevation'] = random.randint(120, 170)
    #type
    data['rateType'] = "HIGH"
    return data

while True:
    rnd = random.random()
    time.sleep(2)
    if (rnd < 10):
        rawData=getHighMetrics()
        partitionKey=rawData['userId']+"_metric_"+rawData['workoutId']
        #print partitionKey
        data = json.dumps(rawData)
        print data
        kinesis.put_record(streamName, data, partitionKey)
    else:
        prob=[10,10,20] #prob of 20 is 10%
        randomNumber=random.randint(0,2)
        if (10==prob[randomNumber]):
            rawData=getNormalMetrics()
            partitionKey=rawData['userId']+"_metric_"+rawData['workoutId']
            data = json.dumps(rawData)
            print data
            kinesis.put_record(streamName, data, partitionKey)
        else:
            rawData=getMediumMetrics()
            partitionKey=rawData['userId']+"_metric_"+rawData['workoutId']
            data = json.dumps(getMediumMetrics())
            print data
            kinesis.put_record(streamName, data, partitionKey)
