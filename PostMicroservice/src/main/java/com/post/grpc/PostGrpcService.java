package com.post.grpc;

import com.post.model.Post;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import proto.AddReactionProto;
import proto.AddReactionResponseProto;
import proto.PostGrpcServiceGrpc;
import com.post.service.PostService;

@GrpcService
public class PostGrpcService extends PostGrpcServiceGrpc.PostGrpcServiceImplBase {

    private final PostService postService;

    @Autowired
    public PostGrpcService(PostService postService) {
        this.postService = postService;
    }

    @Override
    public void addReaction(AddReactionProto request, StreamObserver<AddReactionResponseProto> responseObserver) {

        Post post = postService.addReaction(request.getPostId(), request.getUserId(), request.getLike());
        AddReactionResponseProto addReactionResponseProto;

        if (post == null)
            addReactionResponseProto = AddReactionResponseProto.newBuilder().setStatus("Status 400").build();
        else
            addReactionResponseProto = AddReactionResponseProto.newBuilder().setStatus("Status 200").build();

        responseObserver.onNext(addReactionResponseProto);
        responseObserver.onCompleted();

    }

}
