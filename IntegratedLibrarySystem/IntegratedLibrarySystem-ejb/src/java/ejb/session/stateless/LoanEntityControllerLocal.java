/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LoanEntity;
import java.util.List;
import util.exception.StillLoaningException;


/**
 *
 * @author limwe
 */

public interface LoanEntityControllerLocal {
    public LoanEntity persistNewLoanEntity(LoanEntity newLoan) throws StillLoaningException;

    public List<LoanEntity> retrieveAllLoans();
}
