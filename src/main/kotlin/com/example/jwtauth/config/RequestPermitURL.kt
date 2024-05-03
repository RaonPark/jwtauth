package com.example.jwtauth.config

enum class RequestPermitURL(val url: String) {
    LOGIN("/api/login"),
    REGISTER("/api/register"),
}