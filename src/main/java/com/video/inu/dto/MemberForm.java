package com.video.inu.dto;

import lombok.Data;

@Data
public class MemberForm {
    private Long id;
    private String email;
    private String pwd;
    private String name;
    private String phone;
    private String check;
}
