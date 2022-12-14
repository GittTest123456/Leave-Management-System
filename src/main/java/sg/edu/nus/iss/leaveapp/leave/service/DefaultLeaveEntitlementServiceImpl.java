package sg.edu.nus.iss.leaveapp.leave.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.leaveapp.leave.model.DefaultLeaveEntitlement;
import sg.edu.nus.iss.leaveapp.leave.repository.DefaultLeaveEntitlementRepository;

@Service
public class DefaultLeaveEntitlementServiceImpl implements DefaultLeaveEntitlementService {

    @Autowired
    private DefaultLeaveEntitlementRepository defaultLeaveEntitlementRepository;

    @Override
    public  DefaultLeaveEntitlement findBydesignation(String designation){
        return defaultLeaveEntitlementRepository.findBydesignation(designation);
    }

    @Override
    public DefaultLeaveEntitlement saveDefaultLeaveEntitlement(DefaultLeaveEntitlement leaveEntitlement){
        return defaultLeaveEntitlementRepository.save(leaveEntitlement);
    }
    
}
