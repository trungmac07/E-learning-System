package com.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.user.grpc.UserIdGrpcService;

import io.grpc.Server;
import io.grpc.ServerBuilder;

@Configuration
public class GrpcServerConfig {

    private final UserIdGrpcService userIdGrpcService;

    public GrpcServerConfig(UserIdGrpcService userIdGrpcService) {
        this.userIdGrpcService = userIdGrpcService;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public Server grpcServer() {
        return ServerBuilder
                .forPort(9091) // gRPC server port
                .addService(userIdGrpcService)
                .build();
    }
}
