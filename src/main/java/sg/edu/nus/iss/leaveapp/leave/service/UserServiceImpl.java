package sg.edu.nus.iss.leaveapp.leave.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepo;
    
    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public List<User> getUserList() {
        return userRepo.findAll();
    }

    @Override
    public User updateUser(User user, String username){
        return null;

    }

    @Override
    public Boolean deleteUserByUsername(String username){
        return false;
    }

    @Override
    public User getUserByUsername(String username){
        return userRepo.getUserByUsername(username);
    }


    
}
