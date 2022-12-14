package sg.edu.nus.iss.leaveapp.leave.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.service.UserService;



@Controller
@RequestMapping("adm")
public class AdminController {
	@Autowired
	private UserService userService;
	@GetMapping("/managestaff")
	public String manageStaff(Model model) {
        List<User> users = userService.getUserList();
        model.addAttribute("staffs", users);
		return "managestaff";
	}
    @GetMapping("/managestaffleave")
	public String manageStaffLeave() {
		return "managestaffleave";
	}

    @GetMapping("/managecalendar")
	public String manageCalendar() {
		return "managecalendar";
	}

    @GetMapping("/manageleavetypes")
	public String manageLeaveTypes() {
		return "manageleavetypes";
	}

    @GetMapping("/managehierarchy")
	public String getHierarchy() {
		return "managehierarchy";
	}

    @GetMapping("/updatestaff")
	public String updateStaff() {
		return "updatestaff";
	}
    
    @GetMapping("/addstaff")
	public String addstaff() {
		return "addstaff";
	}

    
}
