package com.mokhtarabadi.messagesworker.repositories;

import com.mokhtarabadi.messagesworker.models.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<MessageEntity, String> {

}
