package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.leaveapp.leave.model.User;

public interface UserService {

    User saveUser(User user);
    
    List<User> getUserList();

    User updateUser (User user, String username);

    Boolean deleteUserByUsername(String username);

    User getUserByUsername(String username);

    List<User> findSubordinateByManagerID(String managerID);
    
}
