package hanu.example.corespringsecurity.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 인증이 성공했을때 처리하는 핸들러
 * 인증 성공시: 인증전 요청정보를 기반으로 추가적인 처리를 할 수 있다.
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * requestCache(세션에 저장): 인증에 성공한 이후에 이동할 페이지 등의 정보를 저장하고 있다.
     * 예: A 페이지 접근 -> 로그인페이지 이동 -> 로그인성공 -> requestCache에 저장된 A페이지 -> A 페이지로 이동
     */
    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /*
         * 인증 성공후 추가적인 작업을 여기서 처리한다.
         */
        setDefaultTargetUrl("/");

        SavedRequest savedRequest = requestCache.getRequest(request, response); // 인증 성공전 요청정보 (예: 요청 URL, 파라미터 , etc...)
        if(savedRequest != null){
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }else{
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}
