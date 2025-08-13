package routine.log.controller;

import routine.log.config.jwt.JwtBlacklist;
import routine.log.config.jwt.JwtTokenProvider;
import routine.log.dto.user.DeleteRequestDto;
import routine.log.dto.user.LoginRequestDto;
import routine.log.dto.user.SignupRequestDto;
import routine.log.dto.user.TokenResponseDto;
import routine.log.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtBlacklist jwtBlacklist;
    private final UserDetailService userService;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto req){
        userService.signup(req.getUsername(), req.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        String accessToken = jwtTokenProvider.createToken(auth.getName());
        return ResponseEntity.ok(new TokenResponseDto("Bearer " + accessToken));
    }

    // 로그아웃 -> 토큰 블랙리스트 등록
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authz) {
        if (authz != null && authz.startsWith("Bearer ")) {
            String token = authz.substring(7);
            if (jwtTokenProvider.validate(token)) {
                long exp = jwtTokenProvider.getExpirationEpochMillis(token);
                jwtBlacklist.add(token, exp);
            }
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<?> deleteMe(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody DeleteRequestDto req,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authz) {

        userService.deleteMe(principal.getUsername(), req.getPassword());


        if (authz != null && authz.startsWith("Bearer ")) {
            String token = authz.substring(7);
            if (jwtTokenProvider.validate(token)) {
                jwtBlacklist.add(token, jwtTokenProvider.getExpirationEpochMillis(token));
            }
        }
        return ResponseEntity.noContent().build();
    }




}
