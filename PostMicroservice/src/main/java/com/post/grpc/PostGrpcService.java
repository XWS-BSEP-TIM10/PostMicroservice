package com.post.grpc;

import com.post.dto.NewPostRequestDTO;
import com.post.exception.UserIsBlockedException;
import com.post.model.Comment;
import com.post.model.Post;
import com.post.service.LoggerService;
import com.post.service.PostService;
import com.post.service.impl.LoggerServiceImpl;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import proto.AddPostProto;
import proto.AddPostResponseProto;
import proto.AddReactionProto;
import proto.AddReactionResponseProto;
import proto.CommentPostProto;
import proto.CommentPostResponseProto;
import proto.CommentProto;
import proto.PostGrpcServiceGrpc;
import proto.PostProto;
import proto.RemoveReactionProto;
import proto.RemoveReactionResponseProto;
import proto.UserPostsProto;
import proto.UserPostsResponseProto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@GrpcService
public class PostGrpcService extends PostGrpcServiceGrpc.PostGrpcServiceImplBase {

    private final PostService postService;
    private final LoggerService loggerService;
    private final SimpleDateFormat iso8601Formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final String STATUS_OK = "Status 200";

    @Autowired
    public PostGrpcService(PostService postService) {
        this.postService = postService;
        this.loggerService = new LoggerServiceImpl(this.getClass());
    }

    @Override
    public void addReaction(AddReactionProto request, StreamObserver<AddReactionResponseProto> responseObserver) {
        AddReactionResponseProto addReactionResponseProto;
        try {
            Post post = postService.addReaction(request.getPostId(), request.getUserId(), request.getLike());
            if (post == null) {
                loggerService.reactionAddingFailed(request.getUserId(), request.getPostId());
                addReactionResponseProto = AddReactionResponseProto.newBuilder().setStatus("Status 400").build();
            } else {
                loggerService.reactionAddedSuccessfully(request.getUserId(), request.getPostId());
                addReactionResponseProto = AddReactionResponseProto.newBuilder().setStatus(STATUS_OK).build();
            }
        } catch (UserIsBlockedException userIsBlockedException) {
            loggerService.reactionAddingFailed(request.getUserId(), request.getPostId());
            addReactionResponseProto = AddReactionResponseProto.newBuilder().setStatus("Status 400").build();
        }
        responseObserver.onNext(addReactionResponseProto);
        responseObserver.onCompleted();

    }

    @Override
    public void removeReaction(RemoveReactionProto request, StreamObserver<RemoveReactionResponseProto> responseObserver) {
        RemoveReactionResponseProto responseProto;
        try {
            Post post = postService.removeReaction(request.getPostId(), request.getUserId());
            if (post == null) {
                loggerService.reactionRemovingFailed(request.getUserId(), request.getPostId());
                responseProto = RemoveReactionResponseProto.newBuilder().setStatus("Status 404").build();
            } else {
                loggerService.reactionRemovedSuccessfully(request.getUserId(), request.getPostId());
                responseProto = RemoveReactionResponseProto.newBuilder().setStatus(STATUS_OK).build();
            }
        } catch (UserIsBlockedException userIsBlockedException) {
            loggerService.reactionRemovingFailed(request.getUserId(), request.getPostId());
            responseProto = RemoveReactionResponseProto.newBuilder().setStatus("Status 400").build();
        }
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();

    }

    @Override
    public void addPost(AddPostProto request, StreamObserver<AddPostResponseProto> responseObserver) {
        NewPostRequestDTO newPostRequestDTO = new NewPostRequestDTO(request.getOwnerId(), request.getText());
        Post newPost = postService.addPost(newPostRequestDTO, request.getImage().toByteArray());
        AddPostResponseProto addPostResponseProto = AddPostResponseProto.newBuilder()
                .setId(newPost.getId())
                .setStatus(STATUS_OK)
                .build();
        loggerService.postCreatedSuccessfully(request.getOwnerId());
        responseObserver.onNext(addPostResponseProto);
        responseObserver.onCompleted();
    }

    @Override
    public void commentPost(CommentPostProto request, StreamObserver<CommentPostResponseProto> responseObserver) {
        CommentPostResponseProto commentPostResponseProto;
        try {
            Post post = postService.addComment(request.getPostId(), request.getUserId(), request.getComment());


            if (post == null) {
                loggerService.commentAddingFailed(request.getUserId(), request.getPostId());
                commentPostResponseProto = CommentPostResponseProto.newBuilder().setComment(request.getComment()).setStatus("Status 400").build();
            } else {
                loggerService.commentAddedSuccessfully(request.getUserId(), request.getPostId());
                commentPostResponseProto = CommentPostResponseProto.newBuilder().setComment(request.getComment()).setStatus(STATUS_OK).build();
            }
        } catch (UserIsBlockedException userIsBlockedException) {
            loggerService.commentAddingFailed(request.getUserId(), request.getPostId());
            commentPostResponseProto = CommentPostResponseProto.newBuilder().setStatus("Status 400").build();
        }
        responseObserver.onNext(commentPostResponseProto);
        responseObserver.onCompleted();

    }

    @Override
    public void getUserPosts(UserPostsProto request, StreamObserver<UserPostsResponseProto> responseObserver) {
        List<Post> posts = postService.getPostsFromUser(request.getUserId());
        List<PostProto> postsProto = new ArrayList<>();
        for (Post post : posts) {
            List<CommentProto> commentsProto = new ArrayList<>();
            for (Comment c : post.getComments()) {
                commentsProto.add(CommentProto.newBuilder().setUserId(c.getUserId()).setText(c.getText()).build());
            }
            PostProto postProto = PostProto.newBuilder()
                    .setPostId(post.getId())
                    .setText(post.getText())
                    .setOwnerId(post.getOwnerId())
                    .setCreationDate(iso8601Formatter.format(post.getCreationDate()))
                    .setImage(Base64.getEncoder().encodeToString(post.getImage().getData()))
                    .addAllLikes(post.getLikes())
                    .addAllComments(commentsProto)
                    .addAllDislikes(post.getDislikes())
                    .build();
            postsProto.add(postProto);
        }
        UserPostsResponseProto userPostsResponseProto = UserPostsResponseProto.newBuilder()
                .addAllPosts(postsProto)
                .build();
        loggerService.allPostGetSuccessfully(request.getUserId());
        responseObserver.onNext(userPostsResponseProto);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserFeed(UserPostsProto request, StreamObserver<UserPostsResponseProto> responseObserver) {
        List<Post> posts = postService.getFeed(request.getUserId());
        List<PostProto> postsProto = new ArrayList<>();
        for (Post post : posts) {
            List<CommentProto> commentsProto = new ArrayList<>();
            for (Comment c : post.getComments()) {
                commentsProto.add(CommentProto.newBuilder().setUserId(c.getUserId()).setText(c.getText()).build());
            }
            PostProto postProto = PostProto.newBuilder()
                    .setPostId(post.getId())
                    .setText(post.getText())
                    .setOwnerId(post.getOwnerId())
                    .setCreationDate(iso8601Formatter.format(post.getCreationDate()))
                    .setImage(Base64.getEncoder().encodeToString(post.getImage().getData()))
                    .addAllLikes(post.getLikes())
                    .addAllComments(commentsProto)
                    .addAllDislikes(post.getDislikes())
                    .build();
            postsProto.add(postProto);
        }
        UserPostsResponseProto userPostsResponseProto = UserPostsResponseProto.newBuilder()
                .addAllPosts(postsProto)
                .build();
        loggerService.feedGetSuccessfully(request.getUserId());
        responseObserver.onNext(userPostsResponseProto);
        responseObserver.onCompleted();
    }
}
