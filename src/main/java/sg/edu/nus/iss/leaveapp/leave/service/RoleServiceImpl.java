package sg.edu.nus.iss.leaveapp.leave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.leaveapp.leave.model.Role;
import sg.edu.nus.iss.leaveapp.leave.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    RoleRepository roleRepo;

    @Override
    public Role saveRole(Role role){
        return roleRepo.save(role);
    }
    
}
