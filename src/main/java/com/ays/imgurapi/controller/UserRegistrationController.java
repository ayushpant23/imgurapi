package com.ays.imgurapi.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ays.imgurapi.dto.AlbumDto;
import com.ays.imgurapi.dto.LoginDto;
import com.ays.imgurapi.dto.UserDto;
import com.ays.imgurapi.entity.Role;
import com.ays.imgurapi.entity.User;
import com.ays.imgurapi.repository.RoleRepository;
import com.ays.imgurapi.repository.UserRepository;
import com.ays.imgurapi.service.ImageService;
import com.ays.imgurapi.service.ImgurService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/api")
@Slf4j
public class UserRegistrationController {
	
	@Autowired
	private ImageService imageService;
	
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImgurService imgurService;
    
    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto){
    	log.info("Login method has tried by user : "+loginDto.getUsernameOrEmail());
    	Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public  ResponseEntity <Object> registerUser(@RequestBody UserDto userDto) throws IOException{
        if(userRepository.existsByUsername(userDto.getUsername())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(userDto.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));
        userRepository.save(user);
        AlbumDto  albumDto = imgurService.createUserAlbum(userDto.getUsername());
        imageService.saveAlbum(albumDto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }
    
    @PostMapping("/retrivedetails")
    public  ResponseEntity <Object> retreive(@RequestBody UserDto userDto) throws IOException{
    	log.info("Excuted to retreive all the images ");
    	if(userRepository.existsByUsername(userDto.getUsername())){
    		AlbumDto albumDto = imageService.findall(userDto.getUsername());
    		return imgurService.retrieveUserImages(albumDto.getId());
    	}else {
    		return new ResponseEntity<>("Invalid Username !", HttpStatus.BAD_REQUEST);
    	}
        
    }
    
    @PostMapping(value = "/upload", consumes = "multipart/form-data")    
    public ResponseEntity <Object> uploadImage(@RequestParam(name = "image") MultipartFile image, @RequestParam  (name = "username") String username) throws IOException {
    	log.info("Uploaded an image for {}", username);
    	if(userRepository.existsByUsername(username)){
    		AlbumDto albumDto = imageService.findall(username);
    		imgurService.upload(image, albumDto.getId(), username);
    	}else {
    		 return new ResponseEntity<>("Invalid Username !", HttpStatus.BAD_REQUEST);
    	}
    	
        return ResponseEntity.ok().body("Image Uploaded successfully");
    }
}
