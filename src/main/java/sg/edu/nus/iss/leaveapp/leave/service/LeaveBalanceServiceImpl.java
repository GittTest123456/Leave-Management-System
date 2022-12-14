package sg.edu.nus.iss.leaveapp.leave.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveBalance;
import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.repository.LeaveBalanceRepository;

@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {
    @Autowired
    private LeaveBalanceRepository leaveBalanceRepo;

    @Override
    public LeaveBalance saveLeaveBalance(LeaveBalance leaveBalance){
        return leaveBalanceRepo.save(leaveBalance);

    }

    @Override
    public void reduceLeave(LeaveApplication leaveApplication){
        User user =leaveApplication.getUser();
        LeaveBalance leaveBalance = leaveBalanceRepo.findByUser(user);
        String leaveType = leaveApplication.getLeaveType();
        if(leaveType.equals("annual_leave")){
            Double currentAnnualLeave = leaveBalance.getAnnualLeave();
            leaveBalance.setAnnualLeave(currentAnnualLeave - leaveApplication.getNumberOfDays());
        }
        else if(leaveType.equals("medical_leave")){
            Double currentMedicalLeave = leaveBalance.getMedicalLeave();
            leaveBalance.setMedicalLeave(currentMedicalLeave - leaveApplication.getNumberOfDays());
        }
        else{
            Double currentCompensationLeave = leaveBalance.getCompensationLeave();
            leaveBalance.setCompensationLeave(currentCompensationLeave - leaveApplication.getNumberOfDays());
        }
        leaveBalanceRepo.save(leaveBalance);
        
    }
    
}