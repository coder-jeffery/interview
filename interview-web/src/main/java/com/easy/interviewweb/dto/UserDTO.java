package com.easy.interviewweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    //主键，@Id 不能漏，包必须是 jakarta.persistence.Id
    private Long id;
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度2‑50位")
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private int status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
