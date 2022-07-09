package com.post.service;

import com.post.model.Event;

import java.util.List;

public interface EventService {
    Event save(Event event);
    List<Event> findAll();
}
