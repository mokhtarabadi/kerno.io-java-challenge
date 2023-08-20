# Protocol Scheme Document

This document outlines the protocol scheme used in the provided JavaScript code for a WebSocket client. The client communicates with a WebSocket server using a specific protocol to perform various actions such as authentication, channel subscription, message sending, and more. The following sections describe the protocol scheme in detail.

## Connection Establishment

1. The client establishes a WebSocket connection with the server by connecting to the specified endpoint (`ws://localhost:8080`).

## Authentication

1. Upon successful connection, the client sends an authentication message to the server.
    - Type: `"AUTH"`
    - Data:
        - `"username"`: The username of the client (`"testuser"`)
        - `"password"`: The password associated with the username (`"admin"`)

## Message Types

The client and server communicate using different message types. The following sections describe the message types and their corresponding data.

### AUTH

- Sent by: Client
- Purpose: Request authentication with the server.
- Data:
    - `"username"`: The username of the client.
    - `"password"`: The password associated with the username.

### CHANNELS

- Sent by: Client
- Purpose: Request available channels from the server for the authenticated user.
- Data:
    - `"user"`: The username of the client.

### SUBSCRIBE

- Sent by: Client
- Purpose: Subscribe to a specific channel.
- Data:
    - `"user"`: The username of the client.
    - `"channel"`: The channel to subscribe to.

### MESSAGE

- Sent by: Client
- Purpose: Send a message to a subscribed channel.
- Data:
    - `"channel"`: The channel to which the message is being sent.
    - `"text"`: The content of the message.
    - `"sender"`: The username of the client sending the message.
    - `"acknowledge_id"`: A unique identifier for acknowledging the message (optional).

### ACKNOWLEDGE

- Sent by: Server
- Purpose: Confirm the acknowledgment of a message.
- Data:
    - `"id"`: The identifier of the acknowledged message.

### SNAPSHOT

- Sent by: Server
- Purpose: Provide a snapshot of messages in a channel.
- Data: An array of message objects with the following properties for each message:
    - `"text"`: The content of the message.
    - `"sender"`: The username of the message sender.
    - `"channel"`: The channel of the message.
    - `"timestamp"`: The timestamp of the message.

### BROADCAST

- Sent by: Server
- Purpose: Broadcast a message to subscribed clients.
- Data:
    - `"text"`: The content of the message.
    - `"sender"`: The username of the message sender.
    - `"channel"`: The channel of the message.
    - `"timestamp"`: The timestamp of the message.

### UNSUBSCRIBE

- Sent by: Client
- Purpose: Unsubscribe from a channel.
- Data:
    - `"channel"`: The channel to unsubscribe from.
    - `"user"`: The username of the client.

## Message Flow

1. After successful authentication, the client sends a `"CHANNELS"` request to the server to retrieve available channels for the authenticated user.
1. Once the server responds with the channels, the client selects the first channel from the received channels and sends a `"SUBSCRIBE"` request to subscribe to that channel.
1. Upon successful subscription, the client sends a `"MESSAGE"` request to the subscribed channel, containing the message content, sender, and an optional acknowledgement identifier.
1. When the server acknowledges the sent message, it sends an `"ACKNOWLEDGE"` message with the corresponding message identifier.
1. The server may also send a `"SNAPSHOT"` message to provide a snapshot of existing messages in the subscribed channel.
1. If the server broadcasts a message to the subscribed clients, it sends a `"BROADCAST"` message containing the message details.
1. To unsubscribe from a channel, the client sends an `"UNSUBSCRIBE"` request to the server.
1. When the server confirms the successful unsubscribe, it sends an `"UNSUBSCRIBE"` message back to the client.
1. The client can handle other message types using a default case.

## Connection Closure

1. If the WebSocket connection is closed, the client receives a `"close"` event and logs a corresponding message.

Note: The protocol scheme document assumes that the server follows a compatible protocol and handles the message types accordingly.