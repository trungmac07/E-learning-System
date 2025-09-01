package com.user.grpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.entity.User;
import com.user.repository.UserRepository;

import io.grpc.stub.StreamObserver;
import shared.grpc.studentId.grpc.InternalIdResponse;
import shared.grpc.studentId.grpc.KeycloakIdRequest;
import shared.grpc.studentId.grpc.UserIdServiceGrpc;

@Service
public class UserIdGrpcService extends UserIdServiceGrpc.UserIdServiceImplBase {

    @Autowired
    private final UserRepository studentRepository;

    public UserIdGrpcService(UserRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void getInternalIdByKeycloakId(KeycloakIdRequest request,
                                          StreamObserver<InternalIdResponse> responseObserver) {
        String keycloakId = request.getKeycloakId();
        System.out.println("THIS IS THE KEYCLOAK ID:" + keycloakId);
        User student = studentRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InternalIdResponse response = InternalIdResponse.newBuilder()
                .setInternalId(student.getId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
