package com.post.service.impl;

import com.post.service.LoggerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerServiceImpl implements LoggerService {
   
    private final Logger logger;

    public LoggerServiceImpl(Class<?> parentClass) {this.logger = LogManager.getLogger(parentClass); }

    @Override
    public void reactionAddedSuccessfully(String userId, String postId) {
        logger.info("Post: {} reaction successfully added. User id: {}", postId, userId);
    }

    @Override
    public void reactionAddingFailed(String userId, String postId) {
        logger.warn("Post: {} reaction adding failed. User id: {}", postId, userId);
    }

    @Override
    public void reactionRemovedSuccessfully(String userId, String postId) {
        logger.info("Post: {} reaction successfully removed. User id: {}", postId, userId);
    }

    @Override
    public void reactionRemovingFailed(String userId, String postId) {
        logger.warn("Post: {} reaction removing failed. User id: {}", postId, userId);
    }

    @Override
    public void postCreatedSuccessfully(String userId) {
        logger.info("Post created successfully. User id: {}", userId);
    }

    @Override
    public void commentAddedSuccessfully(String userId, String postId) {
        logger.info("Post: {} comment successfully added. User id: {}", postId, userId);
    }

    @Override
    public void commentAddingFailed(String userId, String postId) {
        logger.warn("Post: {} comment adding failed. User id: {}", postId, userId);
    }

    @Override
    public void allPostGetSuccessfully(String userId) {
        logger.info("User: {} posts get successfully.", userId);
    }

    @Override
    public void feedGetSuccessfully(String userId) {
        logger.info("User: {} feed get successfully.", userId);
    }
}
