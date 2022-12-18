package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.leaveapp.leave.model.Role;
import sg.edu.nus.iss.leaveapp.leave.model.User;

public interface RoleService {
    Role saveRole(Role role);

    List<Role> findRoleByRoleName(String roleName);

    
}
