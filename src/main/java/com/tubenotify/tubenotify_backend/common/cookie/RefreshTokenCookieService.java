package com.tubenotify.tubenotify_backend.common.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Service for managing the refresh token HTTP cookie
 */
@Component
public class RefreshTokenCookieService {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    @Value("${app.useSecureCookie}")
    private boolean useSecureCookie;

    @Value("${auth.token.refreshExpirationInMils}")
    private Long refreshTokenExpirationTime;

    /**
     * Adds a refresh token cookie to the HTTP response
     *
     * @param response the HTTP response to add the cookie to
     * @param refreshToken the refresh token to store in the cookie
     */
    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);

        cookie.setHttpOnly(true);
        cookie.setSecure(useSecureCookie);
        cookie.setPath("/");
        cookie.setMaxAge((int) (refreshTokenExpirationTime / 1000));

        response.addCookie(cookie);

    }

    /**
     * Extracts the refresh token from the HTTP request cookies
     *
     * @param request the incoming HTTP request containing the cookies
     * @return the refresh token value, or null if not found
     */
    public String getRefreshTokenFromCookies(HttpServletRequest request) {


        // If there are no cookies in the request, return null
        if (request.getCookies() == null) {
            return null;
        }

        // Iterate through the cookies in the request
        for (Cookie cookie : request.getCookies()) {

            // Check if this cookie is the refresh token cookie
            if (REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {

                // Return the refresh token value
                return cookie.getValue();
            }
        }

        return null;
    }
}
