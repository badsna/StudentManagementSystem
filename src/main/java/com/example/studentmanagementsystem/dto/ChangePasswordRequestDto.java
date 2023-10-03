package com.example.studentmanagementsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChangePasswordRequestDto implements Serializable {
    private String oldPassword;
    private String newPassword;
    private String rePassword;
}
