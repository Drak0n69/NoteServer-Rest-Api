package notesServer.controller;

import com.google.gson.Gson;
import notesServer.dto.request.user.UserLoginRequestDto;
import notesServer.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
public class TestSessionController {

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mvc;

    private final Gson GSON = new Gson();

    @Test
    void loginUser() throws Exception {
        UserLoginRequestDto loginDto = new UserLoginRequestDto("login", "password123");
        String json = GSON.toJson(loginDto);
        mvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("JAVASESSIONID"));
        verify(accountService, times(1)).login(eq(loginDto), anyString());
    }

    @Test
    void failLoginUser() throws Exception {
        UserLoginRequestDto loginDto = new UserLoginRequestDto(null, null);
        String json = GSON.toJson(loginDto);
        mvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    void logoutUser() throws Exception {
        mvc.perform(delete("/api/session")
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().isOk());
        verify(accountService, times(1)).logout(eq("val"));
    }

    @Test
    void failLogoutUser() throws Exception {
        mvc.perform(delete("/api/session")
                .cookie(new Cookie("wrong_cookie", "val")))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }
}
