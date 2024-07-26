package com.gr.freshermanagement.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gr.freshermanagement.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Schema(description = "Generic response wrapper for successful operations")
public class ResponseGeneral<T> {
    @Schema(description = "HTTP status code")
    private int status;

    @Schema(description = "Response message")
    private String message;

    @Schema(description = "Response data")
    private T data;

    @Schema(description = "Response timestamp")
    private String timestamp;

    public static <T> ResponseGeneral<T> of(int status, String message, T data) {
        return of(status, message, data, DateUtils.getCurrentDateString());
    }

    public static <T> ResponseGeneral<T> ofCreated(String message, T data) {
        return of(HttpStatus.CREATED.value(), message, data, DateUtils.getCurrentDateString());
    }

    public static <T> ResponseGeneral<T> ofSuccess(String message) {
        return of(HttpStatus.OK.value(), message, null, DateUtils.getCurrentDateString());
    }
}
