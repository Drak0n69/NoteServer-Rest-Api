package notesServer.controller;


import notesServer.dto.request.user.UserLoginRequestDto;
import notesServer.dto.response.user.EmptyResponseDto;
import notesServer.erros.ServerException;
import notesServer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private AccountService accountService;
    
    @Autowired
    public SessionController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto loginUser(
            @RequestBody @Valid UserLoginRequestDto loginDto,
            HttpServletResponse response
    ) throws ServerException {
        String cookie = UUID.randomUUID().toString();
        response.addCookie(new Cookie("JAVASESSIONID", cookie));
        return accountService.login(loginDto, cookie);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto logoutUser(
            @CookieValue("JAVASESSIONID") String cookie
    ) throws ServerException {
        return accountService.logout(cookie);
    }
}
