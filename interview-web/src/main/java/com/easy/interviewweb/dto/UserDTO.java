package com.easy.interviewweb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度2‑50位")
    private String username;
    private String password;
    private String nickname;
    private String email;
    @NotBlank(message = "手机号不能为空")
    private String phone;
    private String avatar;
    private int status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updateTime;

    public UserDTO copyForCache() {
        return UserDTO.builder()
                .id(id)
                .username(username)
                .nickname(nickname)
                .email(email)
                .phone(phone)
                .avatar(avatar)
                .status(status)
                .createTime(createTime)
                .updateTime(updateTime)
                .build();
    }
}
