package com.example.warehouse_management.services.impl;

import com.example.warehouse_management.exception.ExistedException;
import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.type.ERole;
import com.example.warehouse_management.models.user.RefreshToken;
import com.example.warehouse_management.models.user.Role;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.payload.request.auth.LoginRequest;
import com.example.warehouse_management.payload.request.auth.RegisterUserRequest;
import com.example.warehouse_management.payload.request.auth.ResetPasswordRequest;
import com.example.warehouse_management.payload.request.user.UserUpdateRequest;
import com.example.warehouse_management.payload.response.JwtResponse;
import com.example.warehouse_management.payload.response.UserResponse;
import com.example.warehouse_management.repository.RoleRepository;
import com.example.warehouse_management.repository.UserRepository;
import com.example.warehouse_management.security.jwt.JwtUtils;
import com.example.warehouse_management.security.services.RefreshTokenService;
import com.example.warehouse_management.security.services.UserDetailsImpl;
import com.example.warehouse_management.services.UserServices;
import com.example.warehouse_management.services.domain.ObjectsUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServicesImpl implements UserServices {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JavaMailSender mailSender;
    @Autowired
    RefreshTokenService refreshTokenService;

    private ModelMapper modelMapper=new ModelMapper();

    public JwtResponse authenticationUser(@RequestBody LoginRequest login){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return new JwtResponse(jwt,userDetails.getId(),userDetails.getCode(),
                userDetails.getFullName(),userDetails.getEmail(),roles,refreshToken.getToken());
    }

    public UserResponse registerUser(RegisterUserRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ExistedException("Error: Email is already in use!");
        }
        String code =generateUserCode();
        // Create new user's account
        User user = new User(code, registerRequest.getEmail(), encoder.encode(registerRequest.getPassword()),
                registerRequest.getFullName(),registerRequest.getSex(), true);

        Set<Role> roles = new HashSet<>();
        if (registerRequest.getRole() == null) {
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new NotFoundGlobalException("Error: Role is not found."));
            roles.add(userRole);
        } else {
                switch (registerRequest.getRole()) {
                    case "admin":
                    case "Admin":
                    case "ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }

        }
        user.setRoles(roles);
        user.setCreateDate(new Date());
        User saveUser = userRepository.save(user);
        UserResponse userResponse=modelMapper.map(saveUser,UserResponse.class);
        return userResponse;

    }
    public boolean resetPassword(ResetPasswordRequest request)  {
        User user= userRepository.findUserByEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        userRepository.save(user);
        return true;
    }
    public boolean forgetPassword(String email){
        User user =userRepository.findUserByEmail(email);
        if (user==null){
            throw new NotFoundGlobalException("Không tìm thấy thông tin nhân viên");
        }
        try {
            sendVerificationEmail(user);
            return true;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }
    private void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "dothituongviplic@gmail.com";
        String senderName = "KHO QUẢN LÝ HÀNG HÓA A";
        String subject = "Cập nhât lại mật khẩu";
        String content = "Gửi %s,<br>"
                + "Để cập nhật lại mật khẩu của bạn vui lòng click vào link bên dưới:<br>"
                + "<h3><a href=%s>http://localhost:3000/doi-mat-khau</a></h3>"
                + "Cảm ơn,<br>"
                + "KHO QUẢN LÝ HÀNG HÓA A.";
        String jwt = jwtUtils.generateJwtToken(user);
        String url="http://localhost:3000/doi-mat-khau?token=%s";
        String urlFormat=String.format(url,jwt);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        String contentFormat = String.format(content,user.getFullName(),urlFormat);

        helper.setText(contentFormat, true);
        mailSender.send(message);
    }
    public UserResponse updateUser(String code,UserUpdateRequest userUpdateRequest){
        User user=userRepository.findUserByCode(code);
        if(user ==null){
            throw new NotFoundGlobalException("Không tìm thấy thông tin nhân viên");
        }

        Set<Role> roles = new HashSet<>();
        if (userUpdateRequest.getRole() == null) {
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new NotFoundGlobalException("Error: Role is not found."));
            roles.add(userRole);
        } else {

                switch (userUpdateRequest.getRole()) {
                    case "admin":
                    case "Admin":
                    case "ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
        }
        if(!ObjectsUtils.equalSet(user.getRoles(),roles)){
            user.setRoles(roles);
        }
        if(!ObjectsUtils.equal(user.getSex(),userUpdateRequest.getSex())){
            user.setSex(userUpdateRequest.getSex());
        }
        if(!ObjectsUtils.equal(user.getFullName(),userUpdateRequest.getFullName())){
            user.setFullName(userUpdateRequest.getFullName());
        }
        UserResponse userResponse=modelMapper.map(userRepository.save(user),UserResponse.class);
        return userResponse;
    }

    @Override
    public List<UserResponse> getAll() {
        List<User> users=userRepository.findAll();
        List<UserResponse> userResponse = users.stream()
                .sorted(Comparator.comparing(User::getCode))
                .map(user -> modelMapper.map(user,UserResponse.class))
                .collect(Collectors.toList());
        return userResponse;
    }

    @Override
    public UserResponse findUserByCode(String code) {
        UserResponse userResponse=modelMapper.map(findByCode(code),UserResponse.class);
        return userResponse;
    }

    @Override
    public User findByCode(String code) {
        User user=userRepository.findUserByCode(code);
        if(ObjectUtils.isEmpty(user))
            throw new NotFoundGlobalException("Không tìm thấy user"+ code);
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if(ObjectUtils.isEmpty(user))
            throw new NotFoundGlobalException("Không tìm thấy user"+ email);
        return user;
    }

    @Override
    public String deactivateByUserCode(String userCode) {
        User user = findByCode(userCode);
        user.setEnabled(false);
        userRepository.save(user);
        return "Đã vô hiệu hoá user "+ user.getFullName();
    }

    @Override
    public List<UserResponse> search(String keyword) {
        List<User> user = userRepository.search(keyword);
        List<UserResponse> userResponses = user.stream().map(e->modelMapper.map(e,UserResponse.class))
                .collect(Collectors.toList());
        return userResponses;
    }

    private String generateUserCode(){
        Random rnd = new Random();
        User user =userRepository.findTopByOrderByIdDesc();
        if (user ==null)
            return "NV0001";

        long id= user.getId()+1;
        String userCode=String.format("NV000%d",id);
        return userCode;

    }


}
