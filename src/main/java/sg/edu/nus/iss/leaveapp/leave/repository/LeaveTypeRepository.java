package sg.edu.nus.iss.leaveapp.leave.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveType;

@EnableJpaRepositories
@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, String> {
    
}