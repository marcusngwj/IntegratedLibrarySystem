/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MemberEntity;
import entity.StaffEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ExistingMemberException;
import util.exception.InvalidLoginException;
import util.logger.Logger;

/**
 *
 * @author limwe
 */
@Stateless
@Local(MemberEntityControllerLocal.class)
@Remote(MemberEntityControllerRemote.class)
public class MemberEntityController implements MemberEntityControllerRemote, MemberEntityControllerLocal {

    @PersistenceContext(unitName = "IntegratedLibrarySystem-ejbPU")
    private EntityManager em;

    
    public MemberEntityController() {}
    
    @Override
    public List<MemberEntity> retrieveAllMembers() {
        Query query = em.createQuery("SELECT m FROM CustomerEntity m");
        return query.getResultList();
    }

    @Override
    public MemberEntity persistNewMemberEntity(MemberEntity memberEntity) {
        em.persist(memberEntity);
        em.flush();
        em.refresh(memberEntity);
        return memberEntity;
    }

    @Override
    public MemberEntity registerMember(MemberEntity newMember) throws ExistingMemberException{
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
            throw new ExistingMemberException("Member already exist!");
        }

    }

    @Override
    public MemberEntity memberLogin(String username, String password) throws InvalidLoginException{
        Logger.log(Level.INFO, "MemberEntityController", "memberLogin", username + " || " + password);
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
