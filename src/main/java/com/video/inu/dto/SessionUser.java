package com.video.inu.dto;

import com.video.inu.domain.Member;
import lombok.Data;

@Data
public class SessionUser {
    public Long id;
    public String email;
    public String name;
    public String phone;

    public SessionUser(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.phone = member.getPhone();
    }

    public SessionUser() {}

}
