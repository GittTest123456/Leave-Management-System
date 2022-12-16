package sg.edu.nus.iss.leaveapp.leave;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;

// import java.rmi.ServerException;
// import java.io.Console;
// import java.time.Duration;
// import java.time.LocalDateTime;
// import java.time.OffsetDateTime;
// import java.util.ArrayList;
// import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

import sg.edu.nus.iss.leaveapp.leave.model.DefaultLeaveEntitlement;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveApplication;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveBalance;
import sg.edu.nus.iss.leaveapp.leave.model.LeaveEventEnum;
import sg.edu.nus.iss.leaveapp.leave.model.PublicHoliday;
import sg.edu.nus.iss.leaveapp.leave.model.Role;
import sg.edu.nus.iss.leaveapp.leave.model.User;
import sg.edu.nus.iss.leaveapp.leave.service.DefaultLeaveEntitlementService;
import sg.edu.nus.iss.leaveapp.leave.service.LeaveApplicationService;
import sg.edu.nus.iss.leaveapp.leave.service.LeaveBalanceService;
import sg.edu.nus.iss.leaveapp.leave.service.PublicHolidayService;
import sg.edu.nus.iss.leaveapp.leave.service.RoleService;
import sg.edu.nus.iss.leaveapp.leave.service.UserService;

// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;


@SpringBootApplication
@EnableJpaRepositories
public class LeaveWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaveWebApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

	@Bean
	public CommandLineRunner commandLineRun(UserService userService, RoleService roleService, LeaveBalanceService leaveBalanceService, 
	DefaultLeaveEntitlementService defaultLeaveEntitlementService,PublicHolidayService publicHolService, LeaveApplicationService leaveAppService){
		return args -> {
			System.out.println("---- Create some roles");
			Role admin = roleService.saveRole(new Role("Admin"));
			Role employee = roleService.saveRole(new Role("Employee"));
			Role manager = roleService.saveRole(new Role("Manager"));

			System.out.println("---- Create some users");
			User john = new User("2531", "John123", "John Tan Meng Keng", "92887201", "John@gmail.com", "Administrator", null);
			User tom = new User("1835", "Tom123", "Tom Lee Shin", "92887888", "Tom@gmail.com","Employee", "2811");
			User sally = new User("2811", "Sally123", "Sally Ang Jean Tee", "92888000", "Sally@gmail.com", "Manager",null);
			User jerry = new User("5833", "Jerry123", "Jerry Heng An Tan", "92895888", "Jerry@gmail.com", "Employee","2811");

			john.setRoles(Arrays.asList(admin));
			tom.setRoles(Arrays.asList(employee));
			sally.setRoles(Arrays.asList(manager, employee));
			jerry.setRoles(Arrays.asList(employee));

			DefaultLeaveEntitlement manage = new DefaultLeaveEntitlement("Manager");
			DefaultLeaveEntitlement employ = new DefaultLeaveEntitlement("Employee");
			DefaultLeaveEntitlement admins = new DefaultLeaveEntitlement("Administrator");
			defaultLeaveEntitlementService.saveDefaultLeaveEntitlement(manage);
			defaultLeaveEntitlementService.saveDefaultLeaveEntitlement(employ);
			defaultLeaveEntitlementService.saveDefaultLeaveEntitlement(admins);

			john.setDefaultLeaveEntitlement(admins);
			tom.setDefaultLeaveEntitlement(employ);
			sally.setDefaultLeaveEntitlement(manage);
			jerry.setDefaultLeaveEntitlement(employ);

			userService.saveUser(john);
			userService.saveUser(tom);
			userService.saveUser(sally);
			userService.saveUser(jerry);
//since leave entitlement contains the foreign key staff_ID, it can only be set when staff is saved in the database/repository with staff ID.
			LeaveBalance johnBalanceLeave = new LeaveBalance(john);
			LeaveBalance tomBalanceLeave = new LeaveBalance(tom);
			LeaveBalance sallyBalanceLeave = new LeaveBalance(sally);
			LeaveBalance jerryBalanceLeave = new LeaveBalance(jerry);
			sallyBalanceLeave.setCompensationLeave(2.5);
			jerryBalanceLeave.setCompensationLeave(2.5);
			tomBalanceLeave.setCompensationLeave(2.5);

			leaveBalanceService.saveLeaveBalance(johnBalanceLeave);
			leaveBalanceService.saveLeaveBalance(tomBalanceLeave);
			leaveBalanceService.saveLeaveBalance(sallyBalanceLeave);
			leaveBalanceService.saveLeaveBalance(jerryBalanceLeave);

			//LeaveType annualLeave = new LeaveType("annual_leave","1");
			//LeaveType medicalLeave = new LeaveType("medical_leave","1");
			//LeaveType compensationLeave = new LeaveType("compensation_leave","0.5");
			//leaveTypeService.saveLeaveType(annualLeave);
			//leaveTypeService.saveLeaveType(medicalLeave);
			//leaveTypeService.saveLeaveType(compensationLeave);

			//DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd/MM/yy");
			//Map<String, String> dateofPublicHol = new HashMap<>(){{
				//put("01/01/23", "New Year’s Day");
				//put("02/01/23", "New Year’s Day In Lieu");
				//put("22/01/23", "Chinese New Year");
				//put("23/01/23", "Chinese New Year");
				//put("24/01/23", "Chinese New Year In Lieu");
				//put("07/04/23", "Good Friday");
				//put("22/04/23", "Hari Raya Puasa");
				//put("01/05/23", "Labour Day");
				//put("03/06/23", "Vesak Day");
				//put("29/06/23", "Hari Raya Haji");
				//put("09/08/23", "National Day");
				//put("12/11/23", "Deepavali");
				//put("13/11/23", "Deepavali In Lieu");
				//put("25/12/23", "Christmas");


			//}};
			//List<PublicHoliday> listOfPublicHol = new ArrayList<PublicHoliday>();
			//for (String date: dateofPublicHol.keySet()){
				//LocalDate localDate = LocalDate.parse(date,df1);
				//String description = dateofPublicHol.get(date);
				//listOfPublicHol.add(new PublicHoliday(localDate, description));
			//}
			//for (PublicHoliday publichol: listOfPublicHol ){
				//publicHolService.savePublicHoliday(publichol);
			//}
			LeaveApplication leaveApplication = new LeaveApplication((long)2811, "annual_leave", LocalDate.now(), LocalDate.now(), "rest", "great", (long)90000000);
			leaveApplication.setNumberOfDays(0.5);
			leaveApplication.setHalfdayIndicator("AM");
			leaveApplication.setDateOfApplication(LocalDate.now());
			leaveApplication.setDateOfStatus(LocalDate.now());
			leaveApplication.setStatus(LeaveEventEnum.PENDING);
			leaveApplication.setUser(jerry);
			leaveAppService.saveLeaveApplication(leaveApplication);
			LeaveApplication leaveApplication2 = new LeaveApplication((long)2811, "compensation_leave", LocalDate.now(), LocalDate.now(), "rest", "great", (long)90000000);
			leaveApplication2.setNumberOfDays(0.5);
			leaveApplication2.setHalfdayIndicator("AM");
			leaveApplication2.setDateOfApplication(LocalDate.now());
			leaveApplication2.setDateOfStatus(LocalDate.now());
			leaveApplication2.setStatus(LeaveEventEnum.PENDING);
			leaveApplication2.setUser(tom);
			leaveAppService.saveLeaveApplication(leaveApplication2);
			//LeaveApplication leaveApplication2 = new LeaveApplication((long)2811, "compensation_leave", LocalDate.now(), LocalDate.now(), "rest", "great", (long)90000000);
			//leaveApplication.setNumberOfDays(0.5);
			//leaveApplication.setHalfdayIndicator("AM");
			//leaveApplication.setDateOfApplication(LocalDate.now());
			//leaveApplication.setDateOfStatus(LocalDate.now());
			//leaveApplication.setStatus(LeaveEventEnum.APPROVED);
			//leaveApplication.setUser(run);
			//leaveAppService.saveLeaveApplication(leaveApplication2);

		
			
		
		};
		
	}

}
