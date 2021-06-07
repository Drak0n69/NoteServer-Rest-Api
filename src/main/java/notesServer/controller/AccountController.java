package notesServer.controller;


import notesServer.dto.request.user.AddUserByLoginRequestDto;
import notesServer.dto.request.user.UserChangePasswordRequestDto;
import notesServer.dto.request.user.UserDeleteRequestDto;
import notesServer.dto.request.user.UserRegisterRequestDto;
import notesServer.dto.response.user.EmptyResponseDto;
import notesServer.dto.response.user.UserInfoResponseDto;
import notesServer.erros.ServerException;
import notesServer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Validated
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/accounts",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserInfoResponseDto registerUser(
            @RequestBody @Valid UserRegisterRequestDto registerDto,
            HttpServletResponse response
    ) throws ServerException {
        String cookie = UUID.randomUUID().toString();
        UserInfoResponseDto dto = accountService.registerUser(registerDto, cookie);
        response.addCookie(new Cookie("JAVASESSIONID", cookie));
        return dto;
    }

    @DeleteMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto deleteUser(
            @CookieValue("JAVASESSIONID") String cookie,
            @RequestBody @Valid UserDeleteRequestDto deleteDto
    ) throws ServerException {
        return accountService.deleteUser(deleteDto, cookie);
    }

    @PutMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserInfoResponseDto changePassword(
            @CookieValue("JAVASESSIONID") String cookie,
            @RequestBody @Valid UserChangePasswordRequestDto changeDto,
            HttpServletResponse response
    ) throws ServerException {
        UserInfoResponseDto dto = accountService.changePassword(changeDto, cookie);
        response.addCookie(new Cookie("JAVASESSIONID", cookie));
        return dto;
    }

    @PutMapping(path = "accounts/{userId}/super", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto updateToSuper(
            @CookieValue("JAVASESSIONID") String cookie,
            @PathVariable("userId") @Min(1) long userId
    ) throws ServerException {
        return accountService.updateToSuper(userId, cookie);
    }

    @GetMapping(path = "/account")
    public UserInfoResponseDto getCurrentUserInfo(
            @CookieValue("JAVASESSIONID") String cookie
    ) throws ServerException {
        return accountService.getUserInfo(cookie);
    }

    @GetMapping(path = "/accounts")
    public List<UserInfoResponseDto> getAllUsersInfo(
            @CookieValue("JAVASESSIONID") String cookie,
            @RequestParam(name = "sortByRating", required = false) String sortBy,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "from", defaultValue = "0") long from,
            @RequestParam(name = "count", defaultValue = "1844674407370955165") long count
    ) throws ServerException {
        return accountService.getAllUsersInfo(sortBy, type, from, count, cookie);
    }

    @PostMapping(path = "/followings", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto addUserToFollowingList(
            @CookieValue("JAVASESSIONID") String cookie,
            @RequestBody @Valid AddUserByLoginRequestDto loginDto
    ) throws ServerException {
        return accountService.addUserToFollowingList(loginDto, cookie);
    }

    @PostMapping(path = "/ignore", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto addUserToIgnoreList(
            @CookieValue("JAVASESSIONID") String cookie,
            @RequestBody @Valid AddUserByLoginRequestDto loginDto
    ) throws ServerException {
        return accountService.addUserToIgnoreList(loginDto, cookie);
    }

    @DeleteMapping(path = "/followings/{login}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto deleteUserFromFollowingList(
            @CookieValue("JAVASESSIONID") String cookie,
            @PathVariable ("login") String login
    ) throws ServerException {
        return accountService.deleteUserFromFollowingList(login, cookie);
    }

    @DeleteMapping(path = "/ignore/{login}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto deleteUserFromIgnoreList(
            @CookieValue("JAVASESSIONID") String cookie,
            @PathVariable ("login") String login
    ) throws ServerException {
        return accountService.deleteUserFromIgnoreList(login, cookie);
    }
}
