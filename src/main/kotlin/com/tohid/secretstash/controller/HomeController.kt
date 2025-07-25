package com.tohid.secretstash.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
@Tag(name = "Home", description = "Home endpoints")
class HomeController {

    @GetMapping("/")
    @Operation(
        summary = "Redirect to Swagger UI",
        description = "Redirects the root URL to the Swagger UI documentation"
    )
    fun home(): RedirectView {
        return RedirectView("/swagger-ui.html")
    }
}
