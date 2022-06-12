package com.post.service;

public interface LoggerService {
    void reactionAddedSuccessfully(String userId, String postId);
    void reactionAddingFailed(String userId, String postId);
    void reactionRemovedSuccessfully(String userId, String postId);
    void reactionRemovingFailed(String userId, String postId);
    void postCreatedSuccessfully(String userId);
    void commentAddedSuccessfully(String userId, String postId);
    void commentAddingFailed(String userId, String postId);
    void allPostGetSuccessfully(String userId);
    void feedGetSuccessfully(String userId);
}
