/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MemberEntity;
import java.util.List;

import util.exception.ExistingMemberException;
import util.exception.InvalidLoginException;

/**
 *
 * @author limwe
 */
public interface MemberEntityControllerRemote {
    public MemberEntity persistNewMemberEntity(MemberEntity MemberEntity);
    
    public List<MemberEntity> retrieveAllMembers();
   //Register
    public MemberEntity registerMember(MemberEntity memberEntity) throws ExistingMemberException;
    
    //Login
    public MemberEntity memberLogin(String username, String password) throws InvalidLoginException;
    
}
