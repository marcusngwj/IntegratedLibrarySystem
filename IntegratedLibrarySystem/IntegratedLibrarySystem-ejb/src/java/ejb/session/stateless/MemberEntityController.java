package ejb.session.stateless;

import entity.MemberEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MemberExistsException;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;
import util.helper.CryptographicHelper;
import util.logger.Logger;

@Stateless
@Local(MemberEntityControllerLocal.class)
@Remote(MemberEntityControllerRemote.class)
public class MemberEntityController implements MemberEntityControllerRemote, MemberEntityControllerLocal {

    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;

    public MemberEntityController() {}
    
    @Override
    public MemberEntity persistNewMemberEntity(MemberEntity newMember) throws MemberExistsException {
        List<MemberEntity> staffList = retrieveAllMembers();
        for (MemberEntity member : staffList) {
            if (!member.getMemberId().equals(newMember.getMemberId()) && member.getIdentityNumber().equals(newMember.getIdentityNumber())) {
                throw new MemberExistsException("The identity number already exists. Please try again.");
            }
        }
        
        em.persist(newMember);
        em.flush();
        em.refresh(newMember);
        return newMember;
    }
    
    @Override
    public List<MemberEntity> retrieveAllMembers() {
        Query query = em.createQuery("SELECT m FROM MemberEntity m");
        return query.getResultList();
    }
    
    @Override
    public MemberEntity retrieveMemberById(Long memberId) throws MemberNotFoundException {
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.memberId= :inMemberId");
        query.setParameter("inMemberId", memberId);
        
        try {
            return (MemberEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new MemberNotFoundException("Member Id " + memberId + " does not exist!");
        }
    }
    
    @Override
    public MemberEntity retrieveMemberByIdentityNumber(String identityNumber) throws MemberNotFoundException {
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.identityNumber = :inIdentityNumber");
        query.setParameter("inIdentityNumber", identityNumber);
        
        try {
            return (MemberEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new MemberNotFoundException("Member Identity Number " + identityNumber + " does not exist!");
        }
    }
    
    @Override
    public void updateMember(MemberEntity memberToUpdate) throws MemberExistsException {
        List<MemberEntity> staffList = retrieveAllMembers();
        for (MemberEntity member : staffList) {
            if (!member.getMemberId().equals(memberToUpdate.getMemberId()) && member.getIdentityNumber().equals(memberToUpdate.getIdentityNumber())) {
                throw new MemberExistsException("The identity number already exists. Please try again.");
            }
        }
        
        em.merge(memberToUpdate);
    }
    
    @Override
    public void deleteMember(Long memberId) throws MemberNotFoundException {
        MemberEntity memberToRemove = retrieveMemberById(memberId);
        em.remove(memberToRemove);
    }

    @Override
    public MemberEntity createNewMember(MemberEntity newMember) throws MemberExistsException{
    List<MemberEntity> allMembers = retrieveAllMembers();
        Boolean memberExist = false;

        for (MemberEntity existingMember : allMembers) {
            if (existingMember.getIdentityNumber().equals(newMember.getIdentityNumber())) {
                memberExist = true;
                break;
            }
        }
        if (!memberExist) {
             em.persist(newMember);
             em.flush();
             em.refresh(newMember);
            return newMember;
        } else {
            throw new MemberExistsException("Member already exist!");
        }

    }

    @Override
    public MemberEntity memberLogin(String identityNumber, String securityCode) throws InvalidLoginException{
        Logger.log(Logger.INFO, "MemberEntityController", "memberLogin", identityNumber);
        
        try {
            MemberEntity memberEntity = retrieveMemberByIdentityNumber(identityNumber);
            String securityCodeHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(securityCode + memberEntity.getSalt()));
            
            if(memberEntity.getSecurityCode().equals(securityCodeHash)) {
                return memberEntity;
            }
            else {
                throw new InvalidLoginException(InvalidLoginException.INVALID_MEMBER_CREDENTIALS);
            }
        }
        catch(MemberNotFoundException | NoResultException | NonUniqueResultException ex) {
            throw new InvalidLoginException(InvalidLoginException.INVALID_MEMBER_CREDENTIALS);
        }
    }
    
}
