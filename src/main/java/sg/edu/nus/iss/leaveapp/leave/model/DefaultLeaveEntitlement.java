package sg.edu.nus.iss.leaveapp.leave.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table
public class DefaultLeaveEntitlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String designation;

    private Double annualLeave;
    private Double medicalLeave;
    private Double compensationLeave;

    public DefaultLeaveEntitlement (String designation){
        this.designation = designation;
        if (designation.equals("Manager")){
            this.annualLeave = 18.0;
            this.medicalLeave = 60.0;
            this.compensationLeave = 0.0;
        }
        if (designation.equals("Employee")){
            this.annualLeave = 14.0;
            this.medicalLeave = 60.0;
            this.compensationLeave = 0.0;
        }
    }

    @OneToMany(mappedBy="defaultLeaveEntitlement")
    private List<User> users;

}