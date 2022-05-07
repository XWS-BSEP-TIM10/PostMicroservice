package com.post.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import proto.ConnectionsGrpcServiceGrpc;
import proto.ConnectionsProto;
import proto.ConnectionsResponseProto;

import java.util.List;

@Component
public class ConnectionsGrpcClient {

    @GrpcClient("connectionsService")
    private ConnectionsGrpcServiceGrpc.ConnectionsGrpcServiceBlockingStub stub;

    public List<String> getConnections(String uuid){
        ConnectionsProto message = ConnectionsProto.newBuilder().setUuid(uuid).build();
        final ConnectionsResponseProto response  = this.stub.getConnections(message);
        return response.getConnectionsList();
    }

}
