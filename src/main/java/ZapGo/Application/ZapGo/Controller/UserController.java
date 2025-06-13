package ZapGo.Application.ZapGo.Controller;

import ZapGo.Application.ZapGo.Model.User;
import ZapGo.Application.ZapGo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

//    @PostMapping("/save")
//    public ResponseEntity<User> saveUser(@RequestBody User user){
//
//        return new ResponseEntity<>(userService.saveUser(user) , HttpStatus.CREATED);
//    }

    @GetMapping
    public ResponseEntity<?> getAllUser(){

        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") Long id){

        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK) ;
    }


    @PutMapping("/{userId}")
    public  ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable("userId") Long id){

        return new ResponseEntity<>(userService.updateUserData(user,id), HttpStatus.OK);
    }

}
