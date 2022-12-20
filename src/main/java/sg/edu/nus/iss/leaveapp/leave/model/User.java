package sg.edu.nus.iss.leaveapp.leave.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.NoArgsConstructor;
import lombok.Data;


@NoArgsConstructor
@Data
@Entity
@Table(name= "User")
public class User {
    @Id
    @NotBlank(message = "The Staff ID cannot be blank")
    @Column(name= "StaffID")
    private String username;

    @NotBlank(message = "The Full Name cannot be blank")
    @Column(name= "StaffFullName",columnDefinition = "nvarchar(150) not null")
    private String fullName;

    @NotBlank(message = "The Password cannot be blank")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message="The password must be valid with at least one uppercase English letter, one lowercase English letter, at least one digit, at least one special character and minimum eight in length.")
    private String password;

    @NotBlank(message = "The Designation cannot be blank")
    private String designation;

    @NotBlank (message = "The Mobile Phone Number cannot be blank")
    @Pattern(regexp = "(\\8|9)[0-9]{7}", message= "The Mobile Phone should be valid")
    private String mobilePhone;

    @NotBlank (message = "The Email cannot be blank")
    @Email(message = "Email should be valid")
    @Size(max = 200)
    @Pattern(regexp = ".+@.+\\..+", message =  "Email should be valid")
    private String email;

    @NotBlank(message = "The Reporting Staff ID cannot be blank.")
    private String reportingStaffID;

    @NotBlank(message = "The Job Grade cannot be blank.")
    private String jobGrade;

    public User(String username, String password, String fullName, String mobilePhone, String email, String designation,
     String reportingStaffID,String jobGrade){
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.designation = designation;
        this.reportingStaffID = reportingStaffID;
        this.jobGrade = jobGrade;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    //
    private List<Role> roles;

    @OneToOne(mappedBy="user", cascade=CascadeType.REMOVE, orphanRemoval=true)
    private LeaveBalance staffleave;

    @OneToMany(mappedBy="user",cascade=CascadeType.REMOVE, orphanRemoval=true)
    private List<LeaveApplication> leaveApplication;

    @ManyToOne
    private DefaultLeaveEntitlement defaultLeaveEntitlement;
    
}
