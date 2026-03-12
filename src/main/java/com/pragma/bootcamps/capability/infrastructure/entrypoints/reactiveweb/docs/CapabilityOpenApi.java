package com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.docs;

import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.requests.CapabilityRequest;
import com.pragma.bootcamps.capability.infrastructure.entrypoints.reactiveweb.dtos.responses.BusinessResponse;
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
public class CapabilityOpenApi {

    private static final String TAG = "Capability";

    private static final String CREATED_CODE = String.valueOf(HttpStatus.CREATED.value());
    private static final String BAD_REQUEST_CODE = String.valueOf(HttpStatus.BAD_REQUEST.value());
    private static final String CONFLICT_CODE = String.valueOf(HttpStatus.CONFLICT.value());

    private static final String CREATED_DESC = "Capability created successfully";
    private static final String BAD_REQUEST_DESC = "Invalid request data";
    private static final String CONFLICT_DESC = "Capability name already exists";

    private static final String OPERATION_SAVE = "saveCapability";
    private static final String OPERATION_DESC = "Creates a new capability";


    private static final String OK_DESC = "Capabilities listed successfully";
    private static final String OK_CODE = String.valueOf(HttpStatus.OK.value());

    private static final String OPERATION_LIST = "listCapabilities";
    private static final String OPERATION_LIST_DESC = "Lists all capabilities with pagination and sorting";

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";
    private static final String PARAM_SORT_BY = "sortBy";
    private static final String PARAM_ORDER = "order";
    private static final String DEFAULT_PAGE_DESC = "Page number for pagination (default: 0)";
    private static final String DEFAULT_SIZE_DESC = "Page size for pagination (default: 10)";
    private static final String SORT_BY_DESC = "Field to sort by (name or technologyCount)";
    private static final String ORDER_DESC = "Sort order (asc or desc)";

    public void saveCapability(Builder builder) {
        builder
                .operationId(OPERATION_SAVE)
                .description(OPERATION_DESC)
                .tag(TAG)

                .requestBody(requestBodyBuilder()
                        .required(true)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(CapabilityRequest.class))))

                .response(responseBuilder()
                        .responseCode(CREATED_CODE)
                        .description(CREATED_DESC)
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
                        .responseCode(CONFLICT_CODE)
                        .description(CONFLICT_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))));
    }

    public void listCapabilities(Builder builder) {
        builder
                .operationId(OPERATION_LIST)
                .description(OPERATION_LIST_DESC)
                .tag(TAG)
                .parameter(parameterBuilder()
                        .name(PARAM_PAGE)
                        .description(DEFAULT_PAGE_DESC)
                        .required(false))
                .parameter(parameterBuilder()
                        .name(PARAM_SIZE)
                        .description(DEFAULT_SIZE_DESC)
                        .required(false))
                .parameter(parameterBuilder()
                        .name(PARAM_SORT_BY)
                        .description(SORT_BY_DESC)
                        .required(false))
                .parameter(parameterBuilder()
                        .name(PARAM_ORDER)
                        .description(ORDER_DESC)
                        .required(false))
                .response(responseBuilder()
                        .responseCode(OK_CODE)
                        .description(OK_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))));
    }

}
