package com.sentinel.api.resource;

import com.sentinel.api.dto.ApiResponse;
import com.sentinel.api.dto.CreateTargetRequest;
import com.sentinel.domain.model.Target;
import com.sentinel.domain.service.TargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/targets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TargetResource {
  private static final Logger logger = LoggerFactory.getLogger(TargetResource.class);

  @Inject
  TargetService targetService;

  @POST
  public Response createTarget(CreateTargetRequest request) {
    logger.info("Received target creation request from {}", request.getUserEmail());

    try {
      Target createdTarget = targetService.createAndQueueTarget(request);

      ApiResponse<String> response = ApiResponse.ok("target queued for monitoring", createdTarget.getId());

      return Response.accepted(response).build();
    } catch (IllegalArgumentException e) {
      logger.warn("Validation failed: {}", e.getMessage());
      return Response
          .status(Response.Status.BAD_REQUEST)
          .entity(ApiResponse.error("Validation error: " + e.getMessage()))
          .build();
    } catch (Exception e) {
      logger.error("Unexpected error creating target", e);
      return Response
          .status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(ApiResponse.error("Internal Server Error"))
          .build();
    }
  }

  @GET
  @Path("/health")
  public Response health() {
    return Response.ok(ApiResponse.ok("API Gateway is running", null)).build();
  }
}