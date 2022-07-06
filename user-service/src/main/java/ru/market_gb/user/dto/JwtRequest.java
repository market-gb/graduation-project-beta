package ru.market_gb.user.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
