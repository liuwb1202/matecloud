package vip.mate.oauth.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.mate.core.common.api.Result;
import vip.mate.core.common.constant.Oauth2Constant;
import vip.mate.core.common.util.TokenUtil;
import vip.mate.oauth.service.CaptchaService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class AuthController {

    @Qualifier("consumerTokenServices")
    private final ConsumerTokenServices consumerTokenServices;

    private final CaptchaService captchaService;

    @GetMapping("/auth/userInfo")
    public Result<?> getUserInfo(HttpServletRequest request) {
        Claims claims = TokenUtil.getClaims(getToken(request));
        String userName = (String)claims.get("userName");
        String avatar = (String) claims.get("avatar");
        Map<String, Object> data = new HashMap<>();
        data.put("userName", userName);
        data.put("avatar", avatar);
        return Result.data(data);
    }

    @GetMapping("/auth/code")
    public Result<?> authCode() {
        return captchaService.getCode();
    }

    @PostMapping("/auth/logout")
    public Result<?> logout(HttpServletRequest request) {
//        String headerToken = request.getHeader(Oauth2Constant.HEADER_TOKEN);
        consumerTokenServices.revokeToken(getToken(request));
        return Result.success("操作成功");
    }

    private String getToken(HttpServletRequest request) {
        String headerToken = request.getHeader(Oauth2Constant.HEADER_TOKEN);
        return TokenUtil.getToken(headerToken);
    }
}
