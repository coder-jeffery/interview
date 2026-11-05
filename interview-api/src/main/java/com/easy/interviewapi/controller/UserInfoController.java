package com.easy.interviewapi.controller;

import com.easy.interviewapi.model.UserInfo;
import com.easy.interviewapi.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "用户", description = "用户查询 API")
@RestController
@RequestMapping("/user")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Operation(summary = "健康/欢迎信息")
    @GetMapping("/info")
    public String info() {
        return "Welcome to SpringBoot!";
    }

    @Operation(summary = "查询全部用户", description = "返回 ID、姓名、邮箱、角色")
    @ApiResponse(
            responseCode = "200",
            description = "用户列表",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserInfo.class)))
    )
    @GetMapping("/list")
    public List<UserInfo> list() {
        return userInfoService.listAll();
    }

    @Operation(summary = "按 ID 查询用户")
    @ApiResponse(responseCode = "200", description = "找到用户", content = @Content(schema = @Schema(implementation = UserInfo.class)))
    @ApiResponse(responseCode = "404", description = "用户不存在")
    @GetMapping("/{id}")
    public ResponseEntity<UserInfo> getById(
            @Parameter(description = "用户 ID", example = "1") @PathVariable long id) {
        return userInfoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
