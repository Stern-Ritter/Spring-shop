package com.geekbrains.springbootproject.services;

import com.geekbrains.springbootproject.entities.Profile;
import com.geekbrains.springbootproject.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    public Optional<Profile> findById(Long id) {
        return profileRepository.findById(id);
    }

    public Optional<Profile> findByUsername(String userName) {
        return profileRepository.findByUserName(userName);
    }

    public Profile saveOrUpdate(Profile profile) {
        return profileRepository.save(profile);
    }
}