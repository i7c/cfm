package org.rliz.cfm.recorder.common.security

import org.rliz.cfm.recorder.user.boundary.UserBoundary
import org.rliz.cfm.recorder.user.data.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CfmAuthProvider : AuthenticationProvider {

    @Autowired
    private lateinit var userBoundary: UserBoundary

    private val encoder = BCryptPasswordEncoder()

    @Transactional
    override fun authenticate(authentication: Authentication?): Authentication {
        if (authentication == null) throw BadCredentialsException("Internal error")
        val name = authentication.name
        val password = authentication.credentials.toString()

        if (name.isEmpty()) throw BadCredentialsException("Provide a username")
        if (password.isEmpty()) throw BadCredentialsException("Provide a password")

        return regularUserLogin(name, password)
    }

    private fun regularUserLogin(name: String, password: String): Authentication {
        userBoundary.findUserByName(name)?.let {
            if (encoder.matches(password, it.password)) {
                return UsernamePasswordAuthenticationToken(it, "removed", determineRoles(it))
            }
        }
        throw BadCredentialsException("Bad credentials")
    }

    override fun supports(authentication: Class<*>?): Boolean =
        authentication!!.isAssignableFrom(UsernamePasswordAuthenticationToken::class.java)
}

fun determineRoles(u: User) = (if (u.systemUser) listOf(SimpleGrantedAuthority("ROLE_ADMIN")) else emptyList())
    .plus(SimpleGrantedAuthority("ROLE_USER"))
