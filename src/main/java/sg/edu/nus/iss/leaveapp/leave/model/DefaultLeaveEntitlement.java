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
    private String jobGrade;

    private Double annualLeave;
    private Double medicalLeave;

    public DefaultLeaveEntitlement (String jobGrade){
        this.jobGrade = jobGrade;
        if (jobGrade.equals("ISS02")){
            this.annualLeave = 18.0;
            this.medicalLeave = 60.0;
        }
        if (jobGrade.equals("ISS01")){
            this.annualLeave = 14.0;
            this.medicalLeave = 60.0;
        }
        if (jobGrade.equals("ISS03")){
            this.annualLeave = 0.0;
            this.medicalLeave = 0.0;
        }
    }

    @OneToMany(mappedBy="defaultLeaveEntitlement")
    private List<User> users;

}