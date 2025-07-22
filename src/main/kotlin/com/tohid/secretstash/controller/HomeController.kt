package com.tohid.secretstash.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/")
@RestController
class HomeController {

    @GetMapping
    fun home(): String {
        return "Welcome to Secret Stash!"
    }
}