# Kerno's DevX Challenge

Howdy 👋  So... are you a legend?

We are in the lookout for beast-mode backend developers, mindful of architecture, data structures, performance and good engineering work — but whom always keep an eye on timely, customer-facing value delivery.

You are proficient in Java (or event better, Kotlin for backend), and have experience working with different programming paradigms (procedural, object-oriented, functional). You do understand the difference between a hammer and a screwdriver, and craft code that tells a story.  


**And don't be shy!**
We place great value in education and experience; but we also recognize that talent is built on passion and hours dedicated to deliberate practice and learning... so don't shy away from applying if you feel you have what it takes!



## The challenge:
You need to build a little real-time chat backend.

Here is the expected flow in a nutshell:
- A WSServer accepts websocket connections from clients
	- Clients can subscribe to channels (see below)
		- They should receive a sorted list of message history + real-time messages
	- Clients can send messages to a given channel
		- Messages need to be placed in persistent storage
		- Messages need to be distributed to the channel subscribers

You are free to structure the projects, choose frameworks and/or libraries as you see fit to deliver on this project. Also feel free to use any of the infrastructure pieces provided in the docker-compose file or add your own choices to it. It does have to work with a simple `docker-compose up` tho.

We will be evaluating data structures, synchronization, resiliency and performance strategies.


### Clone this project to get started!
Once you are happy with it, zip it and send it to dev-challenges@kerno.io for us to review.

The repository already contains a usable skeleton for the infra and the services required. Update the docker-compose file to match the initialization commands of your respective ws-server and messages-worker projects. Use Java, Kotlin or Scala at will. We are a Kotlin house, but it is not required that you use Kotlin for this challenge.
