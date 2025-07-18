package com.authmodule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;
import java.util.concurrent.CompletableFuture;

@WebMvcTest(AuthorizationController.class)
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorizationService authorizationService;

    @Test
    void testValidAuthorizationRequest() throws Exception {
        Mockito.when(authorizationService.isAuthorized("123", "read", "document-456"))
                .thenReturn(CompletableFuture.completedFuture(true));

        mockMvc.perform(post("/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "userId": "123",
                      "action": "read",
                      "resource": "document-456"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorized").value(true));
    }

    @Test
    void testServiceTimeout() throws Exception {
        // Simulate timeout: CompletableFuture never completes
        CompletableFuture<Boolean> never = new CompletableFuture<>();
        Mockito.when(authorizationService.isAuthorized("999", "read", "doc-timeout"))
                .thenReturn(never);

        mockMvc.perform(post("/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "userId": "999",
                      "action": "read",
                      "resource": "doc-timeout"
                    }
                """))
                .andExpect(status().isRequestTimeout());
    }
}