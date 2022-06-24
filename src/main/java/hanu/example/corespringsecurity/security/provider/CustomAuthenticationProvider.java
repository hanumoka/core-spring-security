package hanu.example.corespringsecurity.security.provider;

import hanu.example.corespringsecurity.security.service.AccountContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * AuthenticationProvider : 특정유형의 인증을 수행한다.
 */
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //파라미터 authentication 는 AuthenticationManager로 부터 전달 받는다.
        //authentication 에는 사용자가 입력한 인증정보(username, password)가 들어있다.

        //인증을 위한 구현 로직이 들어간다.
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        AccountContext accountContext = (AccountContext)userDetailsService.loadUserByUsername(username);

        if(!passwordEncoder.matches(password, accountContext.getAccount().getPassword())){
            //패스워드 검증
            throw new BadCredentialsException("BadCredentialsException");
        }

        // 패스워드 말고도 필요한 검증을 이곳에서 처리하면 된다.

        //검증이 성공한 검증정보를 authenticationManager에게 다시 리턴한다.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(accountContext.getAccount(),
                        null, accountContext.getAuthorities());

        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // token 타입에 따라서 언제 Provider를 사용할지 조건을 지정하는 메소드
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
