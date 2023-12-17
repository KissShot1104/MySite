package com.mysite.medium.user.service;

import com.mysite.medium.DataNotFoundException;
import com.mysite.medium.user.dto.SiteUserDto;
import com.mysite.medium.user.dto.SiteUserDtoMapper;
import com.mysite.medium.user.dto.UserCreateDto;
import com.mysite.medium.user.entity.SiteUser;
import com.mysite.medium.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SiteUserDtoMapper siteUserDtoMapper;

    @Transactional
    public void createUser(final UserCreateDto userCreateDto) {

        final SiteUser user = SiteUser.builder()
                .username(userCreateDto.getUsername())
                .email(userCreateDto.getEmail())
                .password(passwordEncoder.encode(userCreateDto.getPassword1()))
                .build();

        userRepository.save(user);
    }

    public SiteUserDto getUser(final String username) {
        final Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);

        if (siteUser.isEmpty()) {
            throw new DataNotFoundException("siteuser not found");
        }

        final SiteUserDto siteUserDto = siteUserDtoMapper.siteUserToSiteUserDto(siteUser.get());

        return siteUserDto;
    }


}