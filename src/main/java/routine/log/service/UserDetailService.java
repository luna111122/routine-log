package routine.log.service;

import lombok.extern.slf4j.Slf4j;
import routine.log.domain.User;
import routine.log.exception.NotFoundException;
import routine.log.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(username));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public void signup(String username, String rawPassword){
        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        User u = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .build();


        log.info("회원가입 완료 userId: {}", u.getId());

        userRepository.save(u);
    }

    public void deleteMe(String username, String rawPassword){
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Long id = u.getId();

        if (!passwordEncoder.matches(rawPassword, u.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        userRepository.delete(u);

        log.info("회원탈퇴 완료 userId: {}", id);
    }


}
