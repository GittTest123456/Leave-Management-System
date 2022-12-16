package sg.edu.nus.iss.leaveapp.leave.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import sg.edu.nus.iss.leaveapp.leave.model.PublicHoliday;
import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.service.PublicHolidayService;
import sg.edu.nus.iss.leaveapp.leave.service.UserService;

@Controller
@RequestMapping("adm")
public class AdminController {
	@Autowired
	private UserService userService;
	@Autowired
	private PublicHolidayService publicHolService;
	@Autowired
	RestTemplate restTemplate;

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
	public String viewCalendar(@AuthenticationPrincipal UserDetails userDetails, Model model) throws JsonMappingException, JsonProcessingException {
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		//Consuming API-Public Holiday data
		List<PublicHoliday> publicHolList = publicHolService.getPublicHolList();
		publicHolList = publicHolList.stream().sorted((p1,p2)-> p1.getDateOfPublicHol().compareTo(p2.getDateOfPublicHol())).collect(Collectors.toList());
		model.addAttribute("publicHol", publicHolList);
		return "managecalendar";
	}

	@PostMapping("/publichol/update")
	public String updatePublicHol(@AuthenticationPrincipal UserDetails userDetails,@ModelAttribute(value="hol")PublicHoliday publicHol, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		model.addAttribute("action","edit");
		model.addAttribute("publicHol", publicHol);
		model.addAttribute("error","");
		return "editcalendar";
	}

	@PostMapping("/updatepublichol")
	public String SubmitUpdatePublicHol(@AuthenticationPrincipal UserDetails userDetails,
	@Valid @ModelAttribute(value="publicHol")PublicHoliday publicHol, BindingResult bindingResult, Model model) throws Exception{
		String staffID = userDetails.getUsername();
		User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		if (bindingResult.hasErrors()){
			model.addAttribute("action","edit");
			model.addAttribute("error","");
			return "editcalendar";}
			boolean success = publicHolService.updatePublicHoliday(publicHol);
			if (success){
				model.addAttribute("publicHol", publicHol);
				model.addAttribute("action", "update");
				return "submitpublichol";
			}
			else{
				throw new Exception("InternalServerError");
			}
	
	}

	@PostMapping("/publichol/delete")
	public String DeletePublicHol(@AuthenticationPrincipal UserDetails userDetails,
	@ModelAttribute(value="hol")PublicHoliday publicHol, Model model) throws Exception{
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		boolean success = publicHolService.deletePublicHoliday(publicHol);
		if (success){
			model.addAttribute("publicHol", publicHol);
			model.addAttribute("action", "delete");
			return "submitpublichol";
		}
		else{
			throw new Exception("InternalServerError");
		}

	}

	@GetMapping("/addpublichol")
	public String addPublicHol(@AuthenticationPrincipal UserDetails userDetails, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		model.addAttribute("publicHol", new PublicHoliday());
		model.addAttribute("action","add");
		model.addAttribute("error","");
		return "editcalendar";
	}

	@PostMapping("/addpublichol")
	public String submitAddPublicHol(@AuthenticationPrincipal UserDetails userDetails,
	@Valid @ModelAttribute(value="publicHol")PublicHoliday publicHol, BindingResult bindingResult, Model model){
		String staffID = userDetails.getUsername();
        User user = userService.getUserByUsername(staffID);
		model.addAttribute("fullName",user.getFullName());
		if (bindingResult.hasErrors()){
			model.addAttribute("action","add");
			model.addAttribute("error","");
			return "editcalendar";
		}
		boolean success = publicHolService.saveNewPublicHoliday(publicHol);
		if (success){
			model.addAttribute("publicHol", publicHol);
			model.addAttribute("action", "add");
			return "submitpublichol";
		}
		else{
			model.addAttribute("error", "This date overlap with an existing public holiday in the records. Please select another date and try again.");
			model.addAttribute("action", "add");
			return "editcalendar";

		}
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
