package sg.edu.nus.iss.leaveapp.leave.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import sg.edu.nus.iss.leaveapp.leave.exception.ResourceNotFoundException;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
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
	private UserService userService;

    @GetMapping("/leaveapplication")
	public String startLeaveApplication(Model model
    ,HttpSession sessionObj) {
        LeaveApplication leaveApplication = (LeaveApplication) sessionObj.getAttribute("leaveApplication");
        if (leaveApplication == null){
            model.addAttribute("leaveApplication",new LeaveApplication());
        }
        else{

            model.addAttribute("leaveApplication", leaveApplication);
         }
		return "leaveapplication";
	}

    @PostMapping("/leaveapplication")
    public String validateLeaveApplication(@AuthenticationPrincipal UserDetails userDetails,@Valid @ModelAttribute("leaveApplication") LeaveApplication leaveApplication, BindingResult bindingResult, Model model
    ,HttpSession sessionObj) throws JsonMappingException, JsonProcessingException{
        if (bindingResult.hasErrors()){
                return "leaveapplication";

        }
        String errorMessage = generateErrorMessageAtValidation(userDetails,leaveApplication,null,"full");
        sessionObj.setAttribute("leaveApplication", leaveApplication);
        model.addAttribute("errorMessage",errorMessage);
        model.addAttribute("leaveApplication",leaveApplication);
        return "leavereview";

    }

    public String generateErrorMessageAtValidation(@AuthenticationPrincipal UserDetails userDetails,LeaveApplication leaveApplication, Long ID, String mode) throws JsonMappingException, JsonProcessingException{
        boolean startDatePublicHol;
        boolean endDatePublicHol;
        String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
        boolean overlap;
        long daysBetween;
        boolean leaveAppliedExceedBalance;
        if (mode.equals("full")){
            overlap = leaveApplicationService.checkIfOverlapLeave(leaveApplication.getStartDate(), leaveApplication.getEndDate(),user, ID);
            startDatePublicHol = leaveApplicationService.checkIfWorkingDay(leaveApplication.getStartDate());
            endDatePublicHol = leaveApplicationService.checkIfWorkingDay(leaveApplication.getEndDate());
            daysBetween = leaveApplicationService.checkNumberOfDaysOfLeave(leaveApplication.getStartDate(), leaveApplication.getEndDate());
            leaveApplication.setNumberOfDays((double)daysBetween);
            leaveAppliedExceedBalance = leaveApplicationService.checkIfLeaveAppliedExceedBalance((double)daysBetween, leaveApplication.getLeaveType(),user);
        }
        else{
            leaveApplication.setNumberOfDays(0.5);
            overlap = leaveApplicationService.checkIfHalfDayOverlap(leaveApplication.getStartDate(), leaveApplication.getHalfdayIndicator(),user, ID);
            startDatePublicHol = leaveApplicationService.checkIfWorkingDay(leaveApplication.getStartDate());
            endDatePublicHol =  startDatePublicHol;
            leaveAppliedExceedBalance = leaveApplicationService.checkIfLeaveAppliedExceedBalance(0.5, leaveApplication.getLeaveType(),user);
        }
        String errorMessage = leaveApplicationService.generateErrorMessage(startDatePublicHol, endDatePublicHol,leaveAppliedExceedBalance,overlap);
        return errorMessage;
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
        leaveApplicationService.saveLeaveApplication(leaveApplication);
        model.addAttribute("leaveApplication",leaveApplication);
        model.addAttribute("update","no");
        sessionObj.removeAttribute("leaveApplication");
        return "leavesubmission";

    }

    @GetMapping("/deleteleave")
    public String deleteleave(@RequestParam("id") String id, Model model){
        Long ID = Long.parseLong(id);
        Optional <LeaveApplication> leaveApplication = leaveApplicationService.findLeaveApplicationById(ID);
        if (leaveApplication.isPresent()){
            if(leaveApplication.get().getHalfdayIndicator() == null){
                leaveApplicationService.DeleteLeaveApplication(leaveApplication.get());
                model.addAttribute("leaveApplication", leaveApplication.get());
                return "leavesubmission";
            }
            else{
                leaveApplicationService.DeleteLeaveApplication(leaveApplication.get());
                model.addAttribute("compensationLeaveApplication", leaveApplication.get());
                return "compensationleavesubmission";
            }
        }
        else{
            throw new ResourceNotFoundException(
                "Leave Details not found with this Leave Application ID " + id);
        }
    }

    @GetMapping("/updateleave")
    public String updateleave(@RequestParam("id") String id, Model model){
        Long ID = Long.parseLong(id);
        Optional <LeaveApplication> leaveApplication = leaveApplicationService.findLeaveApplicationById(ID);
        if (leaveApplication.isPresent()){
            if(leaveApplication.get().getHalfdayIndicator() == null){
                model.addAttribute("leaveApplication", leaveApplication.get());
                return "leaveapplication";
            }
            else{
                model.addAttribute("compensationLeaveApplication", leaveApplication.get());
                return "compensationleaveonly";
            }
        }
        else{
            throw new ResourceNotFoundException(
                "Leave Details not found with this Leave Application ID " + id);
        }
    }

    @PostMapping("/updateleaveapplication")
    public String validateUpdateLeaveApplication(@AuthenticationPrincipal UserDetails userDetails,@Valid @ModelAttribute("leaveApplication") LeaveApplication leaveApplication, BindingResult bindingResult, Model model
    ,@RequestParam("id") String id) throws JsonMappingException, JsonProcessingException{
        if (id == null){
            throw new ResourceNotFoundException(
                "Leave Details not found with this Leave Application ID " + id);
        }
        else{
            leaveApplication.setStatus(LeaveEventEnum.PENDING);
            Long ID = Long.parseLong(id);
            leaveApplication.setId(ID);
            if (bindingResult.hasErrors()){
                return "leaveapplication";
            }
            String errorMessage = generateErrorMessageAtValidation(userDetails,leaveApplication,ID,"full");
            model.addAttribute("errorMessage",errorMessage);
            model.addAttribute("leaveApplication",leaveApplication);
            return "leavereview";
        }
    }

    @PostMapping("/updateleavesubmission")
    public String submitUpdateLeaveApplication(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("leaveApplication") LeaveApplication leaveApplication, Model model,
    @RequestParam("id") String id){
        Long ID = Long.parseLong(id);
        String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
        Optional <LeaveApplication> oldLeaveApplication = leaveApplicationService.findLeaveApplicationById(ID);
        if (oldLeaveApplication.isPresent()){
            leaveApplication.setUser(user);
            leaveApplication.setDateOfApplication(LocalDate.now());
            leaveApplication.setDateOfStatus(LocalDate.now());
            leaveApplication.setStatus(LeaveEventEnum.PENDING);
            model.addAttribute("leaveApplication",leaveApplication);
            model.addAttribute("update","yes");
            leaveApplicationService.saveUpdateLeaveApplication(oldLeaveApplication.get(), leaveApplication);
            return "leavesubmission";
        }
        else{
            throw new ResourceNotFoundException(
                "Leave Details not found with this Leave Application ID " + id);
        }

    }

	@GetMapping("/viewleave")
	public String viewLeave(@AuthenticationPrincipal UserDetails userDetails,Model model) {
        String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
        List<LeaveApplication> leaveApplicationList =leaveApplicationService.findLeaveApplicationByUser(user);
        if(leaveApplicationList.isEmpty()){
            model.addAttribute("leaveApplicationList", null);
        }
        else{
            model.addAttribute("leaveApplicationList", leaveApplicationList);
        }
        model.addAttribute("fullName", user.getFullName());
		return "viewleave";
	}

    @GetMapping("/viewleavedetails")
    public String viewleavedetails(@AuthenticationPrincipal UserDetails userDetails,@RequestParam("id") String id, Model model){
        Long ID = Long.parseLong(id);
        String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
        Optional <LeaveApplication> leaveApplication = leaveApplicationService.findLeaveApplicationById(ID);
        model.addAttribute("fullName", user.getFullName());
        if (leaveApplication.isPresent()){
            model.addAttribute("leaveApplication", leaveApplication.get());
        }
        else{
            throw new ResourceNotFoundException(
                "Leave Details not found with this Leave Application ID " + id);
        }
        return "viewleavedetails";

    }


    @GetMapping("/compensationleaveonly")
	public String applyHalfDayCompensationLeave(@AuthenticationPrincipal UserDetails userDetails,Model model,HttpSession sessionObj) {
        LeaveApplication compensationLeaveApplication = (LeaveApplication) sessionObj.getAttribute("compensationLeaveApplication");
        if (compensationLeaveApplication == null){
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
    ,HttpSession sessionObj) throws JsonMappingException, JsonProcessingException{
        if (bindingResult.hasErrors()){
            return "compensationleaveonly";
        }
        leaveApplication.setEndDate(leaveApplication.getStartDate());
        String errorMessage = generateErrorMessageAtValidation(userDetails,leaveApplication,null,"half");
        sessionObj.setAttribute("compensationLeaveApplication", leaveApplication);
        model.addAttribute("errorMessage",errorMessage);
        model.addAttribute("compensationLeaveApplication",leaveApplication);
        return "compensationleavereview";

    }

    @PostMapping("/updatecompensationleaveonly")
    public String validateUpdatecompensationLeaveApplication(@AuthenticationPrincipal UserDetails userDetails,@Valid @ModelAttribute("compensationLeaveApplication") LeaveApplication leaveApplication, BindingResult bindingResult, Model model
    ,@RequestParam("id") String id) throws JsonMappingException, JsonProcessingException{
        if (id == null){
            throw new ResourceNotFoundException(
                "Leave Details not found with this Leave Application ID " + id);
        }
        else{
            leaveApplication.setStatus(LeaveEventEnum.PENDING);
            Long ID = Long.parseLong(id);
            leaveApplication.setId(ID);
            if (bindingResult.hasErrors()){
                return "compensationleaveonly";
            }
            String errorMessage = generateErrorMessageAtValidation(userDetails,leaveApplication,ID,"half");
            model.addAttribute("errorMessage",errorMessage);
            model.addAttribute("leaveApplication",leaveApplication);
            return "compensationleavereview";
        }
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
        leaveApplicationService.saveLeaveApplication(leaveApplication);
        model.addAttribute("compensationleaveApplication",leaveApplication);
        sessionObj.removeAttribute("compensationLeaveApplication");
        model.addAttribute("update","no");
        return "compensationleavesubmission";

    }
    @PostMapping("/updatecompensationleavesubmission")
    public String submitUpdateCompensationLeaveApplication(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("compensationLeaveApplication") LeaveApplication leaveApplication, Model model,
    @RequestParam("id") String id){
        Long ID = Long.parseLong(id);
        String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
        Optional <LeaveApplication> oldLeaveApplication = leaveApplicationService.findLeaveApplicationById(ID);
        if (oldLeaveApplication.isPresent()){
            leaveApplication.setUser(user);
            leaveApplication.setDateOfApplication(LocalDate.now());
            leaveApplication.setDateOfStatus(LocalDate.now());
            leaveApplication.setStatus(LeaveEventEnum.PENDING);
            leaveApplication.setLeaveType("compensation_leave");
            leaveApplication.setEndDate(leaveApplication.getStartDate());
            model.addAttribute("leaveApplication",leaveApplication);
            model.addAttribute("update","yes");
            leaveApplicationService.UpdateCompensationLeaveApplication(oldLeaveApplication.get(), leaveApplication);
            return "compensationleavesubmission";
        }
        else{
            throw new ResourceNotFoundException(
                "Leave Details not found with this Leave Application ID " + id);
        }

    }
        
    }
	
