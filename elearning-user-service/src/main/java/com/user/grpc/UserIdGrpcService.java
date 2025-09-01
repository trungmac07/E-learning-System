package com.user.grpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.entity.User;
import com.user.repository.UserRepository;

import io.grpc.stub.StreamObserver;
import shared.grpc.userId.grpc.InternalIdResponse;
import shared.grpc.userId.grpc.KeycloakIdRequest;
import shared.grpc.userId.grpc.UserIdServiceGrpc;

@Service
public class UserIdGrpcService extends UserIdServiceGrpc.UserIdServiceImplBase {

    @Autowired
    private final UserRepository userRepository;

    public UserIdGrpcService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getInternalIdByKeycloakId(KeycloakIdRequest request,
                                          StreamObserver<InternalIdResponse> responseObserver) {
        String keycloakId = request.getKeycloakId();
        System.out.println("THIS IS THE KEYCLOAK ID:" + keycloakId);
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InternalIdResponse response = InternalIdResponse.newBuilder()
                .setInternalId(user.getId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
