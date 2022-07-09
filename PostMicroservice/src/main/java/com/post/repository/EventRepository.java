package com.post.repository;


import com.post.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {
    Event save(Event event);
    List<Event> findAll();
}
