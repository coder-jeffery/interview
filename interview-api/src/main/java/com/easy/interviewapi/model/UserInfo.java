package com.easy.interviewapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息")
public class UserInfo {

    @Schema(description = "用户 ID", example = "1")
    private Long id;

    @Schema(description = "姓名", example = "Alice")
    private String name;

    @Schema(description = "邮箱", example = "alice@example.com")
    private String email;

    @Schema(description = "角色", example = "Admin")
    private String role;
}
