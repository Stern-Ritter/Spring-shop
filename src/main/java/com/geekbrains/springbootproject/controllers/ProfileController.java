package com.geekbrains.springbootproject.controllers;

import com.geekbrains.springbootproject.entities.Profile;
import com.geekbrains.springbootproject.services.ProfileService;
import com.geekbrains.springbootproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping
    public String profilePage(Principal principal, Model model) {
        Profile profile = profileService.findByUsername(principal.getName()).orElse(null);
        if (profile == null) {
            profile = new Profile();
            profile.setId(0L);
            profile.setUser(userService.findByUserName(principal.getName()));
        }
        model.addAttribute("profile", profile);
        return "profile";
    }

    @PostMapping
    public String editProfile(@ModelAttribute("profile") Profile profile, BindingResult bindingResult,
                              HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        profileService.saveOrUpdate(profile);
        String referer = httpServletRequest.getHeader("referer");
        return "redirect:" + referer;
    }
}