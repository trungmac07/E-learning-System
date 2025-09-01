package com.enrollment.grpc;

import org.springframework.stereotype.Service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import shared.grpc.studentId.grpc.InternalIdResponse;
import shared.grpc.studentId.grpc.KeycloakIdRequest;
import shared.grpc.studentId.grpc.UserIdServiceGrpc;

@Service
public class UserIdClient {

    private final UserIdServiceGrpc.UserIdServiceBlockingStub stub;

    public UserIdClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("user-service", 9091)
                .usePlaintext()
                .build();

        stub = UserIdServiceGrpc.newBlockingStub(channel);
    }

    public Long getInternalId(String keycloakId) {
        KeycloakIdRequest request = KeycloakIdRequest.newBuilder()
                .setKeycloakId(keycloakId)
                .build();

        InternalIdResponse response = stub.getInternalIdByKeycloakId(request);
        return response.getInternalId();
    }
}
