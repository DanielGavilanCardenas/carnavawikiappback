package org.carnavawiky.back.controller;

import org.carnavawiky.back.config.SecurityConfig;
import org.carnavawiky.back.config.WebConfig;
import org.carnavawiky.back.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = HealthController.class, excludeFilters = {
        // Excluimos WebConfig para evitar errores con FileStorageProperties y recursos estáticos
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
})
@Import({SecurityConfig.class, JwtService.class})
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock necesario para SecurityConfig
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("Debe devolver 'service UP' con estado 200 OK")
    void testCheckHealth() throws Exception {
        mockMvc.perform(get("/api/public/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("service UP"));
    }
}