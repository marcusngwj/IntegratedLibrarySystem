package ejb.session.stateless;

import entity.MemberEntity;
import java.util.List;
import util.exception.EntityManagerException;
import util.exception.MemberExistsException;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;

public interface MemberEntityControllerLocal {
    public MemberEntity persistNewMemberEntity(MemberEntity MemberEntity) throws MemberExistsException, EntityManagerException;
    
    public List<MemberEntity> retrieveAllMembers();
    
    public MemberEntity retrieveMemberById(Long memberId) throws MemberNotFoundException;
    
    public MemberEntity retrieveMemberByIdentityNumber(String identityNumber) throws MemberNotFoundException;
    
    public void updateMember(MemberEntity memberEntity);

    public void deleteMember(Long memberId) throws MemberNotFoundException;
    
    public MemberEntity registerMember(MemberEntity memberEntity) throws MemberExistsException;
    
    public MemberEntity memberLogin(String username, String password) throws InvalidLoginException;

}
