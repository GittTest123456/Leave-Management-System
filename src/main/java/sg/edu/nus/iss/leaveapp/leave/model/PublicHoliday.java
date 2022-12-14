package sg.edu.nus.iss.leaveapp.leave.model;
import java.time.LocalDate;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PublicHoliday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateOfPublicHol;
    private String nameOfPublicHol;

    public PublicHoliday(LocalDate dateOfPublicHol, String nameOfPublicHol){
        this.dateOfPublicHol = dateOfPublicHol;
        this.nameOfPublicHol = nameOfPublicHol;
    }

}
