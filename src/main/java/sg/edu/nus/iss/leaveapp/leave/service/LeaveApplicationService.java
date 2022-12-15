package sg.edu.nus.iss.leaveapp.leave.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.User;

public interface LeaveApplicationService {

    public boolean saveLeaveApplication(LeaveApplication leaveApplication);
    public boolean saveUpdateLeaveApplication(LeaveApplication oldLeaveApplication, LeaveApplication newLeaveApplication);
    public boolean checkIfWorkingDay(LocalDate startDate);
    public boolean checkIfLeaveAppliedExceedBalance(double daysBetween, String leaveType,User user);
    public String generateErrorMessage(boolean startDatePublicHol,boolean endDatePublicHol,boolean leaveAppliedExceedBalance, boolean overlap);
    public long checkNumberOfDaysOfLeave(LocalDate startDate, LocalDate endDate);
    public List<LeaveApplication> getLeaveApplicationList();
    public List<LeaveApplication> findLeaveApplicationByUser(User user);
    public boolean checkIfOverlapLeave(LocalDate startDate, LocalDate endDate,User user, Long ID);
    public boolean checkIfHalfDayOverlap(LocalDate Date, String halfdayIndicator, User user, Long ID);
    public Optional<LeaveApplication> findLeaveApplicationById(Long id);
    public void UpdateCompensationLeaveApplication (LeaveApplication oldLeaveApplication, LeaveApplication newLeaveApplication);
    public void DeleteLeaveApplication(LeaveApplication leaveApplication);

}
