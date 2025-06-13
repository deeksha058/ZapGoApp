package ZapGo.Application.ZapGo.Controller;

import ZapGo.Application.ZapGo.Model.User;
import ZapGo.Application.ZapGo.Repository.UserRepository;
import ZapGo.Application.ZapGo.Service.UserService;
import ZapGo.Application.ZapGo.config.AuthRequest;
import ZapGo.Application.ZapGo.config.AuthResponse;
import ZapGo.Application.ZapGo.jwt.JwtTokenUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Api(value = "CRUD Rest APIs for Post resources")
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authManager;
	@Autowired
	JwtTokenUtil jwtUtil;
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@GetMapping
    public ResponseEntity<?> getAllUser(){

        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") Long id){

        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK) ;
    }


    @PutMapping("/{userId}")
    public  ResponseEntity<?> updateUser(@RequestBody User user, @Valid @PathVariable("userId") Long id){
        return new ResponseEntity<>(userService.updateUserData(user,id), HttpStatus.OK);
    }

	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
		try {

//			Authentication authentication = authManager.authenticate(
//					new UsernamePasswordAuthenticationToken(
//							request.getEmail(), request.getPassword())
//			);

			Optional<User> user = userRepository.findByEmail(request.getEmail());
			if (user.isEmpty()) {
				return new ResponseEntity<>("Please signup first", HttpStatus.NOT_ACCEPTABLE);
			}

			if (!user.get().isEnabled()) {
				return new ResponseEntity<>("PleaseVerify you email Id", HttpStatus.UNAUTHORIZED);
			}
			String accessToken = jwtUtil.generateAccessToken(user.orElse(null));
			AuthResponse response = new AuthResponse(user.get().getEmail(), accessToken);
			
			return ResponseEntity.ok().body(response);
			
		} catch (BadCredentialsException ex) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody @Valid AuthRequest request) {

            User user1 = new User();
			user1.setEmail(request.getEmail());
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String password = passwordEncoder.encode(request.getPassword());
			user1.setPassword(password);

		    return userService.saveUser(user1);
	}

	@RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> confirmUserAccount(@RequestParam(required=false,name="token")String confirmationToken) {
		return userService.confirmEmail(confirmationToken);
	}
}
