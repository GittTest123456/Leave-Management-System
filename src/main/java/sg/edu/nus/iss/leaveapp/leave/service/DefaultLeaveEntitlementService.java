package sg.edu.nus.iss.leaveapp.leave.service;

import sg.edu.nus.iss.leaveapp.leave.model.DefaultLeaveEntitlement;

public interface DefaultLeaveEntitlementService {

   DefaultLeaveEntitlement findBydesignation(String designation);

   DefaultLeaveEntitlement saveDefaultLeaveEntitlement(DefaultLeaveEntitlement leaveEntitlement);

    
}
