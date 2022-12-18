package sg.edu.nus.iss.leaveapp.leave.model;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double annualLeave;
    private Double medicalLeave;
    private Double compensationLeave;
    private Long overtimeHours;

    public LeaveBalance(User user){
        DefaultLeaveEntitlement defaultLeaveEntitlement = user.getDefaultLeaveEntitlement();
        this.annualLeave = defaultLeaveEntitlement.getAnnualLeave();
        this.medicalLeave = defaultLeaveEntitlement.getMedicalLeave();
        this.compensationLeave = 0.0;
        this.overtimeHours = (long) (0);
        this.user = user;

    }

    @OneToOne (cascade=CascadeType.REMOVE, orphanRemoval=true)
    private User user;

}
