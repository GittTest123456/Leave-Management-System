package sg.edu.nus.iss.leaveapp.leave.controller;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveBalance;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveEventEnum;
import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.service.LeaveApplicationService;
import sg.edu.nus.iss.leaveapp.leave.service.UserService;
import sg.edu.nus.iss.leaveapp.leave.validator.LeaveApplicationValidator;

import org.springframework.security.core.userdetails.UserDetails;

@Controller
@RequestMapping("emp")
public class EmpController {

    @Autowired
    private LeaveApplicationService leaveApplicationService;
    @Autowired
    private LeaveApplicationValidator leaveApplicationValidator;
    @InitBinder
    private void initLeaveApplicationBinder(WebDataBinder binder){
        binder.addValidators(leaveApplicationValidator);
    }
    @Autowired
	private LeaveApplicationService leaveAppService;
    @Autowired
	private UserService userService;

    @GetMapping("/leaveapplication")
	public String startLeaveApplication(Model model
    ,HttpSession sessionObj) {
        LeaveApplication leaveApplication = (LeaveApplication) sessionObj.getAttribute("leaveApplication");
        if (leaveApplication == null){
            //return "redirect:/emp";
            model.addAttribute("leaveApplication",new LeaveApplication());
        }
        else{

            model.addAttribute("leaveApplication", leaveApplication);
         }
		return "leaveapplication";
	}

    @PostMapping("/leaveapplication")
    public String validateLeaveApplication(@AuthenticationPrincipal UserDetails userDetails,@Valid @ModelAttribute("leaveApplication") LeaveApplication leaveApplication, BindingResult bindingResult, Model model
    ,HttpSession sessionObj){
        if (bindingResult.hasErrors()){
            return "leaveapplication";
        }
        boolean startDatePublicHol = leaveApplicationService.checkIfWorkingDay(leaveApplication.getStartDate());
        boolean endDatePublicHol = leaveApplicationService.checkIfWorkingDay(leaveApplication.getEndDate());
        long daysBetween = leaveApplicationService.checkNumberOfDaysOfLeave(leaveApplication.getStartDate(), leaveApplication.getEndDate());
        leaveApplication.setNumberOfDays((double)daysBetween);
        String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
        boolean leaveAppliedExceedBalance = leaveApplicationService.checkIfLeaveAppliedExceedBalance((double)daysBetween, leaveApplication.getLeaveType(),user);
        boolean overlap = leaveApplicationService.checkIfOverlapLeave(leaveApplication.getStartDate(), leaveApplication.getEndDate(),user);
        String errorMessage = leaveApplicationService.generateErrorMessage(startDatePublicHol, endDatePublicHol,leaveAppliedExceedBalance,overlap);
        sessionObj.setAttribute("leaveApplication", leaveApplication);
        model.addAttribute("errorMessage",errorMessage);
        model.addAttribute("leaveApplication",leaveApplication);
        return "leavereview";

    }

    @PostMapping("/leavesubmission")
    public String submitLeaveApplication(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("leaveApplication") LeaveApplication leaveApplication, Model model,
    HttpSession sessionObj){
        String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
        leaveApplication.setUser(user);
        leaveApplication.setStatus(LeaveEventEnum.PENDING);
        leaveApplication.setDateOfApplication(LocalDate.now());
        leaveApplication.setDateOfStatus(LocalDate.now());
        leaveAppService.saveLeaveApplication(leaveApplication);
        model.addAttribute("leaveApplication",leaveApplication);
        sessionObj.removeAttribute("leaveApplication");
        return "leavesubmission";

    }

	@GetMapping("/viewleave")
	public String viewLeave(@AuthenticationPrincipal UserDetails userDetails,Model model) {
        List<LeaveApplication> leaveApplicationList =leaveApplicationService.getLeaveApplicationList();
        model.addAttribute("leaveApplicationList", leaveApplicationList);
		return "viewleave";
	}

    @GetMapping("/compensationleaveonly")
	public String applyHalfDayCompensationLeave(@AuthenticationPrincipal UserDetails userDetails,Model model,HttpSession sessionObj) {
        LeaveApplication compensationLeaveApplication = (LeaveApplication) sessionObj.getAttribute("compensationLeaveApplication");
        if (compensationLeaveApplication == null){
            //return "redirect:/emp";
            LeaveApplication compensationLeaveApp = new LeaveApplication();
            compensationLeaveApp.setLeaveType("compensation_leave");
            model.addAttribute("compensationLeaveApplication",compensationLeaveApp);
        }
        else{

            model.addAttribute("compensationLeaveApplication", compensationLeaveApplication);
         }
		return "compensationleaveonly";
	}

      @PostMapping("/compensationleaveonly")
    public String validateCompensationLeaveApplication(@AuthenticationPrincipal UserDetails userDetails,@Valid @ModelAttribute("compensationLeaveApplication") LeaveApplication leaveApplication, BindingResult bindingResult, Model model
    ,HttpSession sessionObj){
        if (bindingResult.hasErrors()){
            return "compensationleaveonly";
        }
        leaveApplication.setEndDate(leaveApplication.getStartDate());
        boolean DatePublicHol = leaveApplicationService.checkIfWorkingDay(leaveApplication.getStartDate());
        leaveApplication.setNumberOfDays(0.5);
        String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
        boolean leaveAppliedExceedBalance = leaveApplicationService.checkIfLeaveAppliedExceedBalance(0.5, leaveApplication.getLeaveType(),user);
        boolean overlap = leaveApplicationService.checkIfHalfDayOverlap(leaveApplication.getStartDate(),leaveApplication.getHalfdayIndicator(),user);
        String errorMessage = leaveApplicationService.generateErrorMessage(DatePublicHol, DatePublicHol,leaveAppliedExceedBalance,overlap);
        sessionObj.setAttribute("compensationLeaveApplication", leaveApplication);
        model.addAttribute("errorMessage",errorMessage);
        model.addAttribute("compensationLeaveApplication",leaveApplication);
        return "compensationleavereview";

    }

    @PostMapping("/compensationleavesubmission")
    public String submitCompensationLeaveApplication(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("compensationLeaveApplication") LeaveApplication leaveApplication, Model model,
    HttpSession sessionObj){
        String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
        leaveApplication.setUser(user);
        leaveApplication.setStatus(LeaveEventEnum.PENDING);
        leaveApplication.setDateOfApplication(LocalDate.now());
        leaveApplication.setDateOfStatus(LocalDate.now());
        leaveApplication.setLeaveType("compensation_leave");
        leaveApplication.setEndDate(leaveApplication.getStartDate());
        leaveAppService.saveLeaveApplication(leaveApplication);
        model.addAttribute("compensationleaveApplication",leaveApplication);
        sessionObj.removeAttribute("compensationLeaveApplication");
        return "compensationleavesubmission";

    }
        
    }
	
