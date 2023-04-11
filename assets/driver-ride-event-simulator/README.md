## Ride Event Simulator
This is a Node.js application that simulates ride events for a ride-sharing service. It exposes an API endpoint that accepts rider phone numbers and callback URLs, and then calls a separate API endpoint with simulated ride events for that rider.

### Installation
To install the application, follow these steps:

- Clone the repository to your local machine.
- Install the dependencies by running npm install in the project directory.
- Start the application by running npm start.
Usage
- The application exposes a single API endpoint at `/api/v1/simulate-ride-events`. This endpoint accepts a JSON payload with the following structure:

### Request Body

`
{ "riderPhoneNumber": "123-456-7890",
"callBackUrl": "https://example.com/api/v1/events",
"rideArrivedDelay": 60000,
"rideStartedDelay": 20000,
"rideEndedDelay": 120000
}`

- The riderPhoneNumber and callBackUrl fields are required. The rideArrivedDelay, rideStartedDelay, and rideEndedDelay fields are optional and specify the delay (in milliseconds) between each simulated ride event.

- When the API endpoint is called, the application will respond with a 202 Accepted status code and immediately begin sending simulated ride events to the specified callback URL. The application will send three events in total, with the specified delays between each event.

### Sample curl 
`curl --location --request POST 'http://<server>/api/v1/simulate-ride-events' \
--header 'Content-Type: application/json' \
--data-raw '{
"riderPhoneNumber": "917892693018",
"callBackUrl" : "https://fa94-2406-7400-51-cb3c-d558-fd68-8036-47c5.ngrok-free.app",
"rideStartedDelay": 1000,
"rideArrivedDelay": 1000,
"rideEndedDelay": 1000
}'`

### Docker
This application can also be run inside a Docker container. To build a Docker image of the application, run docker build -t ride-event-simulator . in the project directory. To run the application inside a Docker container, run docker run -p 3000:3000 ride-event-simulator.

### License
This application is licensed under the MIT license. See the LICENSE file for more information.





