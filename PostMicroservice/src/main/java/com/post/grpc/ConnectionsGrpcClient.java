package com.post.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import proto.ConnectionStatusProto;
import proto.ConnectionStatusResponseProto;
import proto.ConnectionsGrpcServiceGrpc;
import proto.ConnectionsProto;
import proto.ConnectionsResponseProto;

import java.util.List;

@Component
public class ConnectionsGrpcClient {

    @GrpcClient("connectionsService")
    private ConnectionsGrpcServiceGrpc.ConnectionsGrpcServiceBlockingStub stub;

    public List<String> getConnections(String uuid) {
        ConnectionsProto message = ConnectionsProto.newBuilder().setUuid(uuid).build();
        final ConnectionsResponseProto response = this.stub.getConnections(message);
        return response.getConnectionsList();
    }

    public String getConnectionStatus(String initiatorId, String receiverId) {
        ConnectionStatusProto message = ConnectionStatusProto.newBuilder().setInitiatorId(initiatorId).setReceiverId(receiverId).build();
        final ConnectionStatusResponseProto response = this.stub.getConnectionStatus(message);
        return response.getConnectionStatus();
    }

}
