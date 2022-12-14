package com.example.monolithic_auth.filter;

import com.example.monolithic_auth.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 컨트롤러가 호출되기 전에 호출되어
 * 로그인 상태 확인
 */
@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {


    private final JwtProvider jwtProvider;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("로그인 확인 인터셉터 실행");

        Cookie cookie = WebUtils.getCookie(request, "token");
        String token = cookie.getValue();
        String role = jwtProvider.getSubject(token);
        if(cookie == null) {
            log.info("비로그인 확인됨");

            response.sendRedirect("/home");//초기 화면으로 이동
            return false;
        } else if (!role.equals("ROLE_USER") && !role.equals("ROLE_ADMIN")){
            log.info("User 권한이 없는 사용자");
            return false;
        }
        log.info("로그인된 사용자.");
        return true;
    }
}
