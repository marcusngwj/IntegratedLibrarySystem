/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import util.exception.StaffNotFoundException;

public interface StaffEntityControllerRemote {
    public StaffEntity persistNewStaffEntity(StaffEntity staffEntity);
    
    public List<StaffEntity> retrieveAllStaffs();
    
    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException;
}
