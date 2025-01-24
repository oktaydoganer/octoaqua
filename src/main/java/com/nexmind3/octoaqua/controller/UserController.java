package com.nexmind3.octoaqua.controller;

import com.nexmind3.octoaqua.dto.LoginDto;
import com.nexmind3.octoaqua.dto.ResetPasswordDto;
import com.nexmind3.octoaqua.dto.UserDto;
import com.nexmind3.octoaqua.entity.User;
import com.nexmind3.octoaqua.exception.CustomException;
import com.nexmind3.octoaqua.service.UserManager;
import com.nexmind3.octoaqua.util.JwtUtil;
import com.nexmind3.octoaqua.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("user")
@RestController
@RequiredArgsConstructor
public class UserController {
   @Autowired
   private UserManager userManager;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/cameraUsers")
    public List<UserDto> getCameraUsers() throws Exception {
        return userManager.getUsersByRole("CAMERA_MANAGER");
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        return principal;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto){

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateJwtToken(authentication);

            return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) throws CustomException {
        try{
            try {
                userValidator.validateUser(userDto);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            User registeredUser =  userManager. register(userDto);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


    @PostMapping("/saveCustomerUser")
    public ResponseEntity<UserDto> saveCustomerUser(@RequestBody UserDto userDto) throws CustomException {
        UserDto customerUser = null ;
        try{
            customerUser =  userManager.saveCustomerUser(userDto);
            return new ResponseEntity<>(customerUser, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<UserDto>(customerUser, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/getUserIdByMail")
    public ResponseEntity<Long> getUserIdByMail(@RequestBody String email) {
        Long userId = null ;
        try{
            userId =  userManager.getUserIdByMail(email);
            return new ResponseEntity<>(userId, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/checkCustomerUser")
    public ResponseEntity<Boolean> checkCustomerUser(@RequestBody Long userId) throws CustomException {
        Boolean customerUserExists = false;
        try{
            customerUserExists = userManager.checkCustomerUser(userId);
            return new ResponseEntity<>(customerUserExists, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<Boolean>(customerUserExists, HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/all")
  //  @PreAuthorize("hasAuthority('READ_ALL_USERS')")
    public List<User> getAllUsers() throws Exception {
        return userManager.getAllUsers();
    }

    @GetMapping("/verifyUserAccount/{key}/{email}")
    //@PreAuthorize("#oauth2.hasScope('server')")
    public ResponseEntity<?> verifyUserAccount(@PathVariable("key") String key, @PathVariable("email") String email) {
        try {
            User user = userManager.verifyUserAccount(email, key);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/checkUserExists")
    public boolean checkUserExists(@RequestBody String email) throws Exception {
        try {
           return userManager.checkUserExistsByEmail(email);
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) throws Exception {
        try {
           if(userManager.resetPassword(resetPasswordDto)){
               return new ResponseEntity( "Your password changed succesfully. You can go to the app and login.",HttpStatus.OK);
           } else{
               return new ResponseEntity( "Password could not be changed. Please try again." , HttpStatus.BAD_REQUEST);
           }
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    @PostMapping("/resetPasswordRequest")
    public ResponseEntity resetPasswordRequest(@RequestBody UserDto userDto) throws Exception {
        try {
            if(userDto == null){
                throw new Exception("Please provide an email address.");
            }
            User user = userManager.resetPasswordRequest(userDto.getEmail());
            if(user == null){
                return new ResponseEntity("Password Change link had already sent to your email.", HttpStatus.OK);
            }
            return new ResponseEntity("Password change link had already been sent to your email address. ", HttpStatus.OK);
        } catch (Exception e){
            throw new Exception(e);
        }
    }

}
