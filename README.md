# Karna Bot

<!--  logo & tag goes here -->


> Smarter , Flexible, State-Aware Conversation Chat Bots for Your Quick Services


[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![build-status workflow](https://github.com/nishanb/Namma-Bot/actions/workflows/app-deploy.yml/badge.svg)
![Contributors workflow](https://img.shields.io/github/contributors/nishanb/Namma-Bot)
![GitHub issues](https://img.shields.io/github/issues/nishanb/Namma-Bot)

Karna is a Chatbot designed to facilitate ride booking on the Namma Yatri Ride Hailing Platform. The chatbot is
integrated with WhatsApp and provides multilingual support to users. Its primary goal is to deliver a seamless and
convenient booking experience, free of any complications. Additionally, Karna offers a range of other features to
enhance user experience.

## Features

> System Features

- **Multi-Platform Support**: The Karna Chat bot is designed to be integrated with multiple platforms, including WhatsApp, Telegram, Slack, and others. 


- **Parallel Conversation Support**: The bot is highly scalable and can handle a large number of conversations for multiple users parallel. 


- **Conversation Isolation** : The bot is designed to handle multiple conversations at the same time without interfering with other user conversations.


- **State-Aware Conversation**: The bot is designed to maintain the state of the conversation with the user. This means that the bot can remember information provided by the user earlier in the conversation and use it to provide more personalized responses. For example, if the user has already provided their location, the bot can use that information to suggest nearby ride options.


- **Integrated Template Engine**: The bot includes a template engine that allows the generation of messages and responses to user queries quickly and efficiently in multiple languages.


- **Horizontal Scalability**: The bot is designed to be horizontally scalable, meaning that it can handle a large number of users by distributing the workload across multiple servers.


- **Auto Conversation Closing**: The bot is designed to automatically close conversations with users after a specified period of inactivity.

> Bot Features

- **End-to-End Ride Booking** : Users can book a ride in Namma Yatri Platform from start to finish using the Karna Chat bot.


- **Ride Status Tracking** : The bot provides users with real-time updates on the status of their ride, such as driver
  ETA and more.


- **Ride Cancellation** : Users can cancel their booked ride using the bot.


- **Feedback Provision** : The bot allows users to provide feedback on their ride experience.


- **Multi-Language Support** : The bot supports multiple languages to cater to a wider user base, currently Kannada,
  Hindi and English are supported.


- **Starred Places Management** : Users can manage their frequently used or favorite locations and use them for booking
  rides quickly.


- **View Past Rides** : Users can view their ride history and check ride details.


- **Namma Yatri Open Data** : The bot provides access to Namma Yatri Open Data.


- **Additional Features** : The bot provides additional features such as support, FAQ section, and more.

## Tech Stack

- [Camunda](https://camunda.com/): Camunda is a popular open-source platform for workflow automation and business process management.


- [Spring Boot](https://spring.io/projects/spring-boot): Spring Boot is a popular framework for building REST APIs in Java.


- [Redis](https://redis.io/):  Redis is an in-memory data structure store that is often used as a database, cache, and message broker.


- [Mockoon](https://mockoon.com/): Mockoon is an open-source tool that helps developers to simulate APIs. Mockoon was used to simulate Namma Yatri APIs.


- [MongoDB](https://www.mongodb.com/): MongoDB is a popular NoSQL document database. We used MongoDB, to store our system's data and analytics details.

By using open-source technologies, we were able to build a scalable and robust ride-booking system that is both efficient and cost-effective.

## Bot In Action

Watch the video to get a better idea on how bot works

> Booking Ride on Namma Yatri Service

`Drop Video or video links here`


> Cancelling Ride on Namma Yatri Service

`Drop Video or video links here`

> Multi Lingual Support

`Drop Video or video links here`

> Managing Starred Places

`Drop Video or video links here`

## System Architecture

![](assets/design/karna-bot-hld-v1.png)

## Technical Documentation

TODO section

## Open Analytics Dashboard
The analytics dashboard provides insights into the usage of the Karna Chat bot, such as the number of users, user trends, and active conversations. This information can be used to improve the bot's performance and user experience.

[Click here to view Live Analytics DashBoard ](https://charts.mongodb.com/charts-test-hwppi/public/dashboards/5f6a3bd3-8ed8-44e9-8480-f9915f290cc7)

![Screenshot 2023-04-23 at 10 12 49 PM](https://user-images.githubusercontent.com/21797317/233852809-a395d560-e6a5-4d26-99dc-d31dd7859cf7.png)


## Prerequisites to use Bot

To use Namma Yatri Service chatbot, you need to have an active WhatsApp account and a smartphone with a reliable
internet connection. Ensure that your WhatsApp account is registered with the phone number you wish to use to interact
with the chatbot.

## Try It Out
We are excited about the Karna Chat bot and can't wait for you to try it out. While we are not ready to make it public yet, we are building a waitlist for early access. Joining our waitlist will give you an exclusive opportunity to try out the Karna Chat bot. To join our waitlist, please register here [Link Goes Here]

## Roadmap & Future Scope
- [X] Analytics Dashboard
- [ ] Build Adapter model to easily integrate with multiple Messaging Platforms
- [ ] DashBoard to manage Template and Conversation




## How to set up

`docker-compose --env-file ~/.env.prod build`

## License

TODO

## Contribution

TODO

## Issues

TODO

## Conclusion

The Namma Yatri Service chatbot is an innovative and user-friendly solution for booking rides on WhatsApp. It is
designed to provide a hassle-free and convenient booking experience for users who prefer to use chatbots to interact
with services. We hope you find it useful and welcome any feedback or suggestions you may have.




