package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.docs;

import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.requests.BootcampCapabilityRequest;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.responses.BusinessResponse;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

@UtilityClass
public class BootcampCapabilityOpenApi {

    private static final String TAG = "BootcampCapability";
    private static final String OK_CODE = String.valueOf(HttpStatus.OK.value());
    private static final String BAD_REQUEST_CODE = String.valueOf(HttpStatus.BAD_REQUEST.value());
    private static final String NOT_FOUND_CODE = String.valueOf(HttpStatus.NOT_FOUND.value());
    private static final String INTERNAL_ERROR_CODE = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());

    private static final String OK_DESC = "Association successful";
    private static final String BAD_REQUEST_DESC = "Invalid request";
    private static final String NOT_FOUND_DESC = "Not found";
    private static final String INTERNAL_ERROR_DESC = "Internal server error";

    private static final String OPERATION_ASSOCIATE = "associateCapabilities";
    private static final String OPERATION_DESC = "Associates a list of capabilities to an existing bootcamp.";

    private static final String OPERATION_GET_CAPS_BY_BOOTCAMP = "getCapabilitiesByBootcampId";
    private static final String OPERATION_GET_CAPS_DESC = "Get all capabilities associated with a bootcamp by its ID.";
    private static final String RESPONSE_CAP_LIST_DESC = "List of CapabilitySummaryDto";
    private static final String PARAM_BOOTCAMP_ID = "bootcampId";
    private static final String PARAM_BOOTCAMP_ID_DESC = "Bootcamp ID to search associated capabilities";


    public void associateCapabilities(Builder builder) {
        builder
                .operationId(OPERATION_ASSOCIATE)
                .description(OPERATION_DESC)
                .tag(TAG)
                .requestBody(requestBodyBuilder()
                        .required(true)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BootcampCapabilityRequest.class))))
                .response(responseBuilder()
                        .responseCode(OK_CODE)
                        .description(OK_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))))
                .response(responseBuilder()
                        .responseCode(BAD_REQUEST_CODE)
                        .description(BAD_REQUEST_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))))
                .response(responseBuilder()
                        .responseCode(NOT_FOUND_CODE)
                        .description(NOT_FOUND_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))))
                .response(responseBuilder()
                        .responseCode(INTERNAL_ERROR_CODE)
                        .description(INTERNAL_ERROR_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))));
    }


    public void getCapabilitiesByBootcampId(Builder builder) {
        builder
                .operationId(OPERATION_GET_CAPS_BY_BOOTCAMP)
                .description(OPERATION_GET_CAPS_DESC)
                .tag(TAG)
                .parameter(parameterBuilder()
                        .name(PARAM_BOOTCAMP_ID)
                        .description(PARAM_BOOTCAMP_ID_DESC)
                        .in(ParameterIn.PATH)
                        .required(true))
                .response(responseBuilder()
                        .responseCode(OK_CODE)
                        .description(RESPONSE_CAP_LIST_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))))
                .response(responseBuilder()
                        .responseCode(NOT_FOUND_CODE)
                        .description(NOT_FOUND_DESC))
                .response(responseBuilder()
                        .responseCode(INTERNAL_ERROR_CODE)
                        .description(INTERNAL_ERROR_DESC));
    }

}
