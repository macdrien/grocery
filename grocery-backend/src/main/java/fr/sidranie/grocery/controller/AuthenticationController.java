package fr.sidranie.grocery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @PostMapping("authentication")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void onSuccessfulAuthentication() {
        // Empty body to only handle the endpoint and return the session cookie
    }
}
