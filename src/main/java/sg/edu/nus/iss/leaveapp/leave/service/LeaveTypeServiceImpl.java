package sg.edu.nus.iss.leaveapp.leave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveType;
import sg.edu.nus.iss.leaveapp.leave.repository.LeaveTypeRepository;

@Service
public class LeaveTypeServiceImpl implements LeaveTypeService{
    @Autowired
    private LeaveTypeRepository leaveTypeRepo;

    @Override
    public LeaveType saveLeaveType(LeaveType leaveType){
        return leaveTypeRepo.save(leaveType);
    }
    
}
