package com.example.studentmanagementsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDto {
    private String oldPassword;
    private String newPassword;
    private String rePassword;
}
