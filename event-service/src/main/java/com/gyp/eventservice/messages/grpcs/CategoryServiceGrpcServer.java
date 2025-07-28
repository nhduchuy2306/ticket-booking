package com.gyp.eventservice.messages.grpcs;

import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.eventservice.grpc.category.CategoryServiceGrpc.CategoryServiceImplBase;
import com.gyp.eventservice.grpc.category.GetCategoryRequest;
import com.gyp.eventservice.grpc.category.GetCategoryResponse;
import com.gyp.eventservice.repositories.CategoryRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class CategoryServiceGrpcServer extends CategoryServiceImplBase {
	private final CategoryRepository categoryRepository;

	@Override
	public void getCategory(GetCategoryRequest request, StreamObserver<GetCategoryResponse> responseObserver) {
		super.getCategory(request, responseObserver);

		var category = categoryRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));

		var response = GetCategoryResponse.newBuilder()
				.setId(category.getId())
				.setName(category.getName())
				.setDescription(category.getDescription())
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
