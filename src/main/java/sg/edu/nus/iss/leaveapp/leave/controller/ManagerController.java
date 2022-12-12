package sg.edu.nus.iss.leaveapp.leave.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveEventEnum;
import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.repository.LeaveApplicationRepository;
import sg.edu.nus.iss.leaveapp.leave.repository.UserRepository;
import sg.edu.nus.iss.leaveapp.leave.service.UserDetailsServiceImpl;

import org.springframework.security.core.userdetails.UserDetails;

@Controller
@RequestMapping("mgr")

public class ManagerController {
	@GetMapping("/mgrviewleave")
	public String getLeavePage() {
		return "mgrviewleave";
	}
    
}
