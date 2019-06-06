package com.adamlentz.pantryuser.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthControllerResponse {
    private boolean success;
    private String message;
}
