package com.example.jwtauth.config

enum class RequestPermitURL(val url: String) {
    LOGIN("/member/login"),
    REGISTER("/member/register"),
    JWT("/member/jwt"),
}