package com.gr.freshermanagement.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gr.freshermanagement.dto.ResponseGeneral;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ExceptionHandlerUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void handleException(HttpServletResponse response, Exception ex) {
        response.setContentType("application/json");

        ResponseGeneral<String> responseBody = ResponseGeneral.of(response.getStatus(),
                ex.getMessage(), null);

        try {
            String jsonResponse = objectMapper.writeValueAsString(responseBody);
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}