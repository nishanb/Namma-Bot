const express = require('express');
const axios = require('axios');

const app = express();
app.use(express.json());


app.post('/api/v1/simulate-ride-events', async (req, res) => {
  const riderPhoneNumber = req.body.riderPhoneNumber;
  const callBackUrl = req.body.callBackUrl

  if (!riderPhoneNumber || !callBackUrl) {
    return res.status(400).json({
      message: "riderPhoneNumber and callBackUrl is expected"
    })
  }

  console.log(`${riderPhoneNumber} added to queue with callback url ${callBackUrl}`)
  res.sendStatus(202)

  const endpoint = '/api/v1/backend-events';

  const apiClient = axios.create({
    baseURL: callBackUrl,
    headers: {
      'Content-Type': 'application/json'
    }
  });

  const requestBodies = [
    {
      "event": "DRIVER_ARRIVED",
      "message": "Your ride has arrived",
      "riderPhoneNumber": riderPhoneNumber,
      "data": {
        "key1": "value1",
        "key2": "value2"
      }
    },
    {
      "event": "RIDE_STARTED",
      "message": "Your ride has started",
      "riderPhoneNumber": riderPhoneNumber,
      "data": {
        "key1": "value1",
        "key2": "value2"
      }
    },
    {
      "event": "RIDE_ENDED",
      "message": "Your ride has ended",
      "riderPhoneNumber": riderPhoneNumber,
      "data": {
        "key1": "value1",
        "key2": "value2"
      }
    }
  ];

  const rideArrivedDelay = req.body.rideArrivedDelay || 60000;
  const rideStartedDelay = req.body.rideStartedDelay || 20000;
  const rideEndedDelay = req.body.rideEndedDelay || 120000;

  const delays = [rideArrivedDelay, rideStartedDelay, rideEndedDelay];

  for (let i = 0; i < requestBodies.length; i++) {
    try {
      await callEndpointWithDelay(apiClient, endpoint, requestBodies[i], delays[i]);
      console.log(`Successfully called ${endpoint} with body ${JSON.stringify(requestBodies[i])}`);
    } catch (error) {
      console.error(`Failed to call ${endpoint} with body ${JSON.stringify(requestBodies[i])}: ${error}`);
    }
  }
});

async function callEndpointWithDelay(apiClient, endpoint, body, delay) {
  await new Promise(resolve => setTimeout(resolve, delay));
  try {
    const response = await apiClient.post(endpoint, body);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to call ${endpoint} with body ${JSON.stringify(body)}: ${error}`);
  }
}

app.listen(3000, () => {
  console.log('Server started on port 3000');
});