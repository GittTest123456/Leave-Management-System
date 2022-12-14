package sg.edu.nus.iss.leaveapp.leave.service;

import java.time.LocalDate;
import java.util.List;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.User;

public interface LeaveApplicationService {

    public boolean saveLeaveApplication(LeaveApplication leaveApplication);
    public boolean checkIfWorkingDay(LocalDate startDate);
    public boolean checkIfLeaveAppliedExceedBalance(double daysBetween, String leaveType,User user);
    public String generateErrorMessage(boolean startDatePublicHol,boolean endDatePublicHol,boolean leaveAppliedExceedBalance, boolean overlap);
    public long checkNumberOfDaysOfLeave(LocalDate startDate, LocalDate endDate);
    public List<LeaveApplication> getLeaveApplicationList();
    public boolean checkIfOverlapLeave(LocalDate startDate, LocalDate endDate,User user);
    public boolean checkIfHalfDayOverlap(LocalDate Date, String halfdayIndicator, User user);

}
