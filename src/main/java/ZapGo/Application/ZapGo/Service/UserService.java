package ZapGo.Application.ZapGo.Service;

import ZapGo.Application.ZapGo.Model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    ResponseEntity<?> saveUser(User user);

    ResponseEntity<?> confirmEmail(String confirmationToken);

    Optional<User> getUserById(Long userId);

    List<User> getAllUser();

    public String deleteUserById(Long id);

    public String updateUserData(User user , Long id );

    User findUser(String email);
}
