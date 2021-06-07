package notesServer.controller;

import com.google.gson.Gson;
import notesServer.dto.request.user.UserChangePasswordRequestDto;
import notesServer.dto.request.user.UserDeleteRequestDto;
import notesServer.dto.request.user.UserRegisterRequestDto;
import notesServer.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class TestAccountController {

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mvc;

    private final Gson GSON = new Gson();

    @ParameterizedTest
    @ValueSource(strings = {"User", "User123", "Юзер", "Юзер123"})
    void registerUserWithLogin(String login) throws Exception {
        UserRegisterRequestDto registerDto = new UserRegisterRequestDto("Иван", "Иванов",
                "Иванович", login, "password123");
        String json = GSON.toJson(registerDto);
        mvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("JAVASESSIONID"));
        verify(accountService, times(1)).registerUser(eq(registerDto), anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"password_>123", "PasswoЙrd123", "Fbkhbж♂С123", "ПарольпарольБ"})
    void registerUserWithPassword(String pass) throws Exception {
        UserRegisterRequestDto registerDto = new UserRegisterRequestDto("Иван", "Иванов",
                "Иванович", "login", pass);
        String json = GSON.toJson(registerDto);
        mvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("JAVASESSIONID"));
        verify(accountService, times(1)).registerUser(eq(registerDto), anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"__User", "thisLoginGreaterMaxLengthLoginnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
            "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn", "╜ФJ3123123123"})
    void failRegisterUserWithWrongLogin(String login) throws Exception {
        UserRegisterRequestDto registerDto = new UserRegisterRequestDto("Иван", "Иванов",
                "Иванович", login, "password123");
        String json = GSON.toJson(registerDto);
        mvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"short"})
    void failRegisterUserWithWrongPassword(String pass) throws Exception {
        UserRegisterRequestDto registerDto = new UserRegisterRequestDto("Иван", "Иванов",
                "Иванович", "login", pass);
        String json = GSON.toJson(registerDto);
        mvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void deleteUser() throws Exception {
        UserDeleteRequestDto deleteDto = new UserDeleteRequestDto("password123");
        String json = GSON.toJson(deleteDto);
        mvc.perform(delete("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().isOk());
        verify(accountService, times(1)).deleteUser(deleteDto, "val");
    }

    @Test
    void changeUserPassword() throws Exception {
        UserChangePasswordRequestDto changeDto = new UserChangePasswordRequestDto("Иван", "Иванов",
                "Иванович", "oldPassword", "newPassword");
        String json = GSON.toJson(changeDto);
        mvc.perform(put("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().isOk());
        verify(accountService, times(1)).changePassword(changeDto, "val");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"short", "0"})
    void failChangeUserPasswordWrongNewPass(String pass) throws Exception {
        UserChangePasswordRequestDto changeDto = new UserChangePasswordRequestDto("Иван", "Иванов",
                "Иванович", "oldPassword", pass);
        String json = GSON.toJson(changeDto);
        mvc.perform(put("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"short", "0"})
    void failChangeUserPasswordWrongOldPass(String pass) throws Exception {
        UserChangePasswordRequestDto changeDto = new UserChangePasswordRequestDto("Иван", "Иванов",
                "Иванович", pass, "newPassword");
        String json = GSON.toJson(changeDto);
        mvc.perform(put("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void updateUserToSuper() throws Exception {
        mvc.perform(put("/api/accounts/1/super")
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().isOk());
        verify(accountService, times(1)).updateToSuper(1L, "val");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void failUpdateUserToSuper(int id) throws Exception {
        mvc.perform(put("/api/accounts/" + id + "/super")
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void getCurrentUserInfo() throws Exception {
        mvc.perform(get("/api/account")
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().isOk());
        verify(accountService, times(1)).getUserInfo("val");
    }

    @Test
    void failGetCurrentUserInfo() throws Exception {
        mvc.perform(get("/api/account")
                .cookie(new Cookie("wrong_cookie", "val")))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void getAllUsersInfo() throws Exception {
        mvc.perform(get("/api/accounts?sortByRating=desc&type=deleted&from=5&count=20")
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().isOk());
        verify(accountService, times(1)).getAllUsersInfo(
                eq("desc"),
                eq("deleted"),
                eq(5L),
                eq(20L),
                eq("val"));
    }

    @Test
    void getAllUserInfoNoParams() throws Exception {
        mvc.perform(get("/api/accounts")
                .cookie(new Cookie("JAVASESSIONID", "val")))
                .andExpect(status().isOk());
        verify(accountService, times(1)).getAllUsersInfo(
                eq(null),
                eq(null),
                eq(0L),
                eq(1844674407370955165L),
                eq("val"));
    }
}
