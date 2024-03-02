package com.nagarro.af.bookingtablesystem.service.authentication;

import com.nagarro.af.bookingtablesystem.controller.authentication.request.AuthenticationRequest;
import com.nagarro.af.bookingtablesystem.controller.authentication.request.RegisterRequest;
import com.nagarro.af.bookingtablesystem.controller.authentication.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
