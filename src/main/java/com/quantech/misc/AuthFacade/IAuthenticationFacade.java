package com.quantech.misc.AuthFacade;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade
{
    Authentication getAuthentication();
}
