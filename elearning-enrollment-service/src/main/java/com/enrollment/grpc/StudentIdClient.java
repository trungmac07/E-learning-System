package com.enrollment.grpc;

import org.springframework.stereotype.Service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import shared.grpc.studentId.grpc.InternalIdResponse;
import shared.grpc.studentId.grpc.KeycloakIdRequest;
import shared.grpc.studentId.grpc.StudentIdServiceGrpc;

@Service
public class StudentIdClient {

    private final StudentIdServiceGrpc.StudentIdServiceBlockingStub stub;

    public StudentIdClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("student-service", 9091)
                .usePlaintext()
                .build();

        stub = StudentIdServiceGrpc.newBlockingStub(channel);
    }

    public Long getInternalId(String keycloakId) {
        KeycloakIdRequest request = KeycloakIdRequest.newBuilder()
                .setKeycloakId(keycloakId)
                .build();

        InternalIdResponse response = stub.getInternalIdByKeycloakId(request);
        return response.getInternalId();
    }
}
