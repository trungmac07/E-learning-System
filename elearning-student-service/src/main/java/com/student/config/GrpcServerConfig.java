package com.student.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.student.grpc.StudentIdGrpcService;

import io.grpc.Server;
import io.grpc.ServerBuilder;

@Configuration
public class GrpcServerConfig {

    private final StudentIdGrpcService studentIdGrpcService;

    public GrpcServerConfig(StudentIdGrpcService studentIdGrpcService) {
        this.studentIdGrpcService = studentIdGrpcService;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public Server grpcServer() {
        return ServerBuilder
                .forPort(9091) // gRPC server port
                .addService(studentIdGrpcService)
                .build();
    }
}
