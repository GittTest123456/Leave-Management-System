package sg.edu.nus.iss.leaveapp.leave.service;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveBalance;

public interface LeaveBalanceService {
    LeaveBalance saveLeaveBalance(LeaveBalance leaveBalance);
    void reduceLeave(LeaveApplication leaveApplication);
    
}
