package com.tohid.secretstash.filters;


import com.tohid.secretstash.utils.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder


class JwtAuthFilterTest {

    private val jwtUtils: JwtUtils = mock(JwtUtils::class.java)
    private val filter = TestableJwtAuthFilter(jwtUtils)

    private val request: HttpServletRequest = mock(HttpServletRequest::class.java)
    private val response: HttpServletResponse = mock(HttpServletResponse::class.java)
    private val filterChain: FilterChain = mock(FilterChain::class.java)

    @BeforeEach
    fun setup() {
        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun teardown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should authenticate user with valid token`() {
        val token = "valid.jwt.token"
        `when`(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer $token")
        `when`(jwtUtils.getUsernameFromToken(token)).thenReturn("tohid")

        filter.callDoFilterInternal(request, response, filterChain)

        val authentication = SecurityContextHolder.getContext().authentication
        assertThat(authentication).isNotNull
        assertThat(authentication.principal).isEqualTo("tohid")
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should skip auth when Authorization header is missing`() {
        `when`(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null)

        filter.callDoFilterInternal(request, response, filterChain)

        assertThat(SecurityContextHolder.getContext().authentication).isNull()
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should skip auth when token does not start with Bearer`() {
        `when`(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Basic abc123")

        filter.callDoFilterInternal(request, response, filterChain)

        assertThat(SecurityContextHolder.getContext().authentication).isNull()
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should skip auth when jwtUtils throws exception`() {
        val token = "invalid.jwt"
        `when`(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer $token")
        `when`(jwtUtils.getUsernameFromToken(token)).thenThrow(RuntimeException("Bad token"))

        filter.callDoFilterInternal(request, response, filterChain)

        assertThat(SecurityContextHolder.getContext().authentication).isNull()
        verify(filterChain).doFilter(request, response)
    }
}

class TestableJwtAuthFilter(jwtUtils: JwtUtils) : JwtAuthFilter(jwtUtils) {
    fun callDoFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        super.doFilterInternal(request, response, filterChain)
    }
}