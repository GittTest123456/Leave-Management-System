package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.leaveapp.leave.model.User;

public interface UserService {

    Boolean saveUser(User user);
    
    List<User> getUserList();

    Boolean updateUser(User user) ;

    Boolean deleteUser(User user);

    User getUserByUsername(String username);

    List<User> findSubordinateByManagerID(String managerID);

    Boolean updateHierarchy(User staff);

    Boolean updateAccess(User staff);
    
}
