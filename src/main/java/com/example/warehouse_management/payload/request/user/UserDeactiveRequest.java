package com.example.warehouse_management.payload.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDeactiveRequest {
    boolean enabled;
}
