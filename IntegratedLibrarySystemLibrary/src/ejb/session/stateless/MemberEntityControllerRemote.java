package ejb.session.stateless;

import entity.MemberEntity;
import java.util.List;

import util.exception.MemberExistsException;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;

public interface MemberEntityControllerRemote {
    public MemberEntity persistNewMemberEntity(MemberEntity newMember) throws MemberExistsException;
    
    public List<MemberEntity> retrieveAllMembers();
    
    public MemberEntity retrieveMemberById(Long memberId) throws MemberNotFoundException;
    
    public MemberEntity retrieveMemberByIdentityNumber(String identityNumber) throws MemberNotFoundException;
    
    public void updateMember(MemberEntity memberToUpdate) throws MemberExistsException;

    public void deleteMember(Long memberId) throws MemberNotFoundException;
    
    public MemberEntity registerMember(MemberEntity memberEntity) throws MemberExistsException;
    
    public MemberEntity memberLogin(String username, String password) throws InvalidLoginException;
    
}