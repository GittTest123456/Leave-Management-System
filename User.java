package sg.edu.nus.iss.leaveapp.leave.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;


@NoArgsConstructor
@Data
@Entity
@Table(name= "User")
public class User {
    @Id
    @Column(name= "StaffID")
    private String username;

    @NotBlank(message = "The Full Name can't be null")
    @Column(name= "StaffFullName",columnDefinition = "nvarchar(150) not null")
    private String fullName;

    private String password;

    private String designation;

    @NotBlank
    @Pattern(regexp = "(\\8|9)[0-9]{7}")
    private String mobilePhone;

    @NotBlank
    @Email(message = "Email should be valid")
    @Size(max = 200)
    @Pattern(regexp = ".+@.+\\..+", message = "Wrong email!")
    private String email;


    private String reportingStaffID;

    public User(String username, String password, String fullName, String mobilePhone, String email, String designation, String reportingStaffID){
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.designation = designation;
        this.reportingStaffID = reportingStaffID;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    //
    private List<Role> roles;

    @OneToOne(mappedBy="user")
    private LeaveBalance staffleave;

    @OneToMany(mappedBy="user")
    private List<LeaveApplication> leaveApplication;

    @ManyToOne
    private DefaultLeaveEntitlement defaultLeaveEntitlement;
    
}
