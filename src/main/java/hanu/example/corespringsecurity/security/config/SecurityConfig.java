package hanu.example.corespringsecurity.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 테스트용 h2 인메모리 콘솔 접속을 위한 시큐리티 설정
        web.ignoring().antMatchers("/h2-console/**");

        web
                .ignoring()
                .antMatchers("/resources/**"); // #3
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 테스트용 h2 인메모리 콘솔 접속을 위한 시큐리티 설정
        httpSecurity
                .authorizeRequests()    // 권한요청 처리 설정 메서드
                .antMatchers("/h2-console/**").permitAll()  // 누구나 h2-console 접속 허용
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")
                .disable(); // GET메소드는 문제가 없는데 POST메소드만 안되서 CSRF 비활성화 시킴

        httpSecurity.formLogin();

        httpSecurity
                .authorizeRequests()
                .antMatchers("/", "/users").permitAll()
                .antMatchers("/test").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/messages").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 핵심 설정: 아래 설정으로 Security가 인증처리를 시도한다.
        auth.userDetailsService(userDetailsService);
    }

    //    주의: 참고용으로 삭제하지 말것
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        String password = passwordEncoder().encode("1111");
//
//        // 테스트용 유저 생성
//        auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
//        auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANAGER", "USER");
//        auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN", "USER", "MANAGER");
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //비밀번호 암호화 빈 등록
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
