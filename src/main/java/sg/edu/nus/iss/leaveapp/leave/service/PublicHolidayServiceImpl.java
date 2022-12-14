package sg.edu.nus.iss.leaveapp.leave.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.leaveapp.leave.model.PublicHoliday;
import sg.edu.nus.iss.leaveapp.leave.repository.PublicHolidayRepository;

@Service
public class PublicHolidayServiceImpl implements PublicHolidayService {
    @Autowired
    private PublicHolidayRepository publicHolidayRepo;
    @Override
    public PublicHoliday savePublicHoliday(PublicHoliday publicHoliday){
        return publicHolidayRepo.save(publicHoliday);

    }
    @Override
    public List<PublicHoliday> getPublicHolList(){
        return publicHolidayRepo.findAll();
    }
    
}
