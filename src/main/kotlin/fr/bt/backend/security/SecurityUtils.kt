package fr.bt.backend.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

import java.util.Optional

/**
 * Get the login of the current user.
 *
 * @return the login of the current user.
 */
fun getCurrentUserLogin(): Optional<String> {
    val securityContext = SecurityContextHolder.getContext()
    return Optional.ofNullable(securityContext.authentication)
        .map { authentication ->
            when (val principal = authentication.principal) {
                is UserDetails -> principal.username
                is String -> principal
                else -> null
            }
        }
}

/**
 * Get the JWT of the current user.
 *
 * @return the JWT of the current user.
 */
fun getCurrentUserJWT(): Optional<String> {
    val securityContext = SecurityContextHolder.getContext()
    return Optional.ofNullable(securityContext.authentication)
        .filter { it.credentials is String }
        .map { it.credentials as String }
}

/**
 * Check if a user is authenticated.
 *
 * @return true if the user is authenticated, false otherwise.
 */
fun isAuthenticated(): Boolean {
    val securityContext = SecurityContextHolder.getContext()
    return Optional.ofNullable(securityContext.authentication)
        .map { authentication ->
            authentication.authorities.none { it.authority == ANONYMOUS }
        }
        .orElse(false)
}

/**
 * If the current user has a specific authority (security role).
 *
 * The name of this method comes from the `isUserInRole()` method in the Servlet API
 *
 * @param authority the authority to check.
 * @return true if the current user has the authority, false otherwise.
 */
fun isCurrentUserInRole(authority: String): Boolean {
    val securityContext = SecurityContextHolder.getContext()
    return Optional.ofNullable(securityContext.authentication)
        .map { authentication ->
            authentication.authorities.any { it.authority == authority }
        }
        .orElse(false)
}
