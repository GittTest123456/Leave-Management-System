package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.List;

import sg.edu.nus.iss.leaveapp.leave.model.PublicHoliday;

public interface PublicHolidayService {
    PublicHoliday savePublicHoliday(PublicHoliday publicHoliday);

    List<PublicHoliday> getPublicHolList();
    
}
