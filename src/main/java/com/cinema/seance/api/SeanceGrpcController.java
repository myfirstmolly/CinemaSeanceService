package com.cinema.seance.api;

import com.cinema.seance.*;
import com.cinema.seance.model.Seance;
import com.cinema.seance.service.SeancesService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SeanceGrpcController extends SeanceServiceGrpc.SeanceServiceImplBase {

    @Autowired
    private SeancesService seancesService;

    @Override
    public void all(AllSeancesRequest request, StreamObserver<AllSeancesResponse> responseObserver) {
        List<Seance> seances = seancesService.getAllSeances();
        List<SeanceResponse> convertedSeances = seances.stream().
                map(Seance::toSeanceResponse).
                collect(Collectors.toList());
        AllSeancesResponse response = AllSeancesResponse.newBuilder().
                addAllSeances(convertedSeances).
                build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void add(SeanceRequest request, StreamObserver<SeanceResponse> responseObserver) {
        Seance seance = seancesService.addSeanceGrpc(request);
        responseObserver.onNext(seance.toSeanceResponse());
        responseObserver.onCompleted();
    }

    @Override
    public void byId(SeanceByIdRequest request, StreamObserver<SeanceResponse> responseObserver) {
        Seance seance = seancesService.getSeanceById(UUID.fromString(request.getId()));
        responseObserver.onNext(seance.toSeanceResponse());
        responseObserver.onCompleted();
    }

    @Override
    public void delete(SeanceByIdRequest request, StreamObserver<DeleteSeanceResponse> responseObserver) {
        seancesService.deleteSeanceById(UUID.fromString(request.getId()));
        responseObserver.onNext(DeleteSeanceResponse.newBuilder().build());
        responseObserver.onCompleted();
    }
}
