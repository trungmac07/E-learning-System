package com.student.grpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.entity.Student;
import com.student.repository.StudentRepository;

import io.grpc.stub.StreamObserver;
import shared.grpc.studentId.grpc.InternalIdResponse;
import shared.grpc.studentId.grpc.KeycloakIdRequest;
import shared.grpc.studentId.grpc.StudentIdServiceGrpc;

@Service
public class StudentIdGrpcService extends StudentIdServiceGrpc.StudentIdServiceImplBase {

    @Autowired
    private final StudentRepository studentRepository;

    public StudentIdGrpcService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void getInternalIdByKeycloakId(KeycloakIdRequest request,
                                          StreamObserver<InternalIdResponse> responseObserver) {
        String keycloakId = request.getKeycloakId();
        System.out.println("THIS IS THE KEYCLOAK ID:" + keycloakId);
        Student student = studentRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        InternalIdResponse response = InternalIdResponse.newBuilder()
                .setInternalId(student.getId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
