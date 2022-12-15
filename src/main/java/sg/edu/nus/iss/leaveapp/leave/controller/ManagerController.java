package sg.edu.nus.iss.leaveapp.leave.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import sg.edu.nus.iss.leaveapp.leave.exception.ResourceNotFoundException;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveEventEnum;
import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.service.LeaveApplicationService;
import sg.edu.nus.iss.leaveapp.leave.service.UserService;

@Controller
@RequestMapping("mgr")

public class ManagerController {

	@Autowired
    private LeaveApplicationService leaveApplicationService;
	@Autowired
	private UserService userService;
	
	@GetMapping("/mgrviewleave")
	public String getAllSubordinateLeavePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		List<LeaveApplication> allSubordinateLeaveApplication = getAllSubordinateLeave(userDetails);
		if (allSubordinateLeaveApplication.isEmpty()){
			model.addAttribute("leaveApplicationList", null);
		}
		else{
			model.addAttribute("leaveApplicationList", allSubordinateLeaveApplication);
		}
		model.addAttribute("view", "allleave");
		return "mgrviewleave";
	}

	@GetMapping("/mgrviewleaveforapproval")
	public String getAllSubordinateLeaveforApprovalPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		List<LeaveApplication> allSubordinateLeaveApplication = getAllSubordinateLeave(userDetails);
		List<LeaveApplication> allSubordinatePendingLeaveApplication = new ArrayList<LeaveApplication>();
		for (LeaveApplication leaveApplication: allSubordinateLeaveApplication){
				if(leaveApplication.getStatus() == LeaveEventEnum.PENDING){
					allSubordinatePendingLeaveApplication.add(leaveApplication);
				}
			}
		if (allSubordinatePendingLeaveApplication.isEmpty()){
			model.addAttribute("leaveApplicationList", null);
		}
		else{
			model.addAttribute("leaveApplicationList", allSubordinatePendingLeaveApplication);
		}
		model.addAttribute("view", "leaveforapproval");
		return "mgrviewleave";
	}

	public List<LeaveApplication> getAllSubordinateLeave(@AuthenticationPrincipal UserDetails userDetails){
		String ManagerID = userDetails.getUsername();
		List<User> subordinateList = userService.findSubordinateByManagerID(ManagerID);
		List<LeaveApplication> leaveApplications = new ArrayList<LeaveApplication>();
		for (User subordinate: subordinateList){
			leaveApplications.addAll(leaveApplicationService.findLeaveApplicationByUser(subordinate));
		}
		return leaveApplications;
	}

	@GetMapping("/decideleaveoutcome")
    public String viewleavedetails(@RequestParam("id") String id, Model model){
        Long ID = Long.parseLong(id);
        Optional <LeaveApplication> leaveApplication = leaveApplicationService.findLeaveApplicationById(ID);
        if (leaveApplication.isPresent()){
            model.addAttribute("leaveApplication", leaveApplication.get());
        }
        else{
            throw new ResourceNotFoundException(
                "Leave Details not found with this Leave Application ID " + id);
        }
        return "decideleaveoutcome";

    }

    
}
