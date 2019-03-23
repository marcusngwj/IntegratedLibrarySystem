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
import util.exception.EntityManagerException;
import util.exception.MemberExistsException;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;
import util.exception.RuntimeExceptionTranslator;
import util.logger.Logger;

@Stateless
@Local(MemberEntityControllerLocal.class)
@Remote(MemberEntityControllerRemote.class)
public class MemberEntityController implements MemberEntityControllerRemote, MemberEntityControllerLocal {

    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;

    public MemberEntityController() {}
    
    @Override
    public MemberEntity persistNewMemberEntity(MemberEntity memberEntity) throws MemberExistsException, EntityManagerException {
        try {
            em.persist(memberEntity);
            em.flush();
            em.refresh(memberEntity);
            return memberEntity;
        }
        catch (RuntimeException ex) {
            if (RuntimeExceptionTranslator.isDatabseIntegrityConstantViolation(ex)) {
                throw new MemberExistsException("The username has already been taken. Please try again.");
            }
            else {
                throw new EntityManagerException("An unknown error has occurred.");
            }
        }
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
    public void updateMember(MemberEntity memberEntity) {
        em.merge(memberEntity);
    }
    
    @Override
    public void deleteMember(Long memberId) throws MemberNotFoundException {
        MemberEntity memberToRemove = retrieveMemberById(memberId);
        em.remove(memberToRemove);
    }

    @Override
    public MemberEntity registerMember(MemberEntity newMember) throws MemberExistsException{
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
    public MemberEntity memberLogin(String username, String password) throws InvalidLoginException{
        Logger.log(Logger.INFO, "MemberEntityController", "memberLogin", username + " || " + password);
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.username = :inUsername AND m.password = :inPassword");
        query.setParameter("inUsername", username);
        query.setParameter("inPassword", password);
        
        try {
            return (MemberEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new InvalidLoginException(InvalidLoginException.INVALID_CREDENTIALS);
        }
    }
    
}
