package hanu.example.corespringsecurity.security.service;

import hanu.example.corespringsecurity.domain.Account;
import hanu.example.corespringsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Spring Security가 인증에 사용할 UserDetailsService 구현체
 * UserDetails(인증정보) 를 리턴해야한다.
 */
@RequiredArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Account> opAccount = userRepository.findByUsername(username);

        // 여기에서는 username, password를 이용해서 해당 유저의 존재유무를 판단한다. 페스워드 검사등을 하지 않는다.
        Account account = opAccount.orElseThrow(
                () -> new UsernameNotFoundException("UsernameNotFoundException"));

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(account.getRole()));

        // AccountContext -> User -> UserDetails 구현체
        AccountContext accountContext = new AccountContext(opAccount.get(), roles);

        return accountContext;  // --> AuthenticationProvider에서 해당 리턴값을 받아 추가검증을 진행한다.
    }
}
