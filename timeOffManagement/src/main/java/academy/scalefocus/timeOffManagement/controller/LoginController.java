package academy.scalefocus.timeOffManagement.controller;

import academy.scalefocus.timeOffManagement.dto.LoginRequestDTO;
import academy.scalefocus.timeOffManagement.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/login")
@Api(tags = "Login")
public class LoginController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    @ApiOperation("This is used to get the bearer token ")
    public String login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return authenticationService.login(loginRequestDTO);
    }
}