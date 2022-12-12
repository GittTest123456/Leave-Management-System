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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

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
    private String fullname;

    private String password;

    private String designation;

    private String reportingStaffID;

    public User(String username, String password, String fullname, String designation, String reportingStaffID){
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.designation = designation;
        this.reportingStaffID = reportingStaffID;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    //
    private List<Role> roles;

    @OneToOne(mappedBy="user")
    private LeaveEntitlement staffleave;
    
}
