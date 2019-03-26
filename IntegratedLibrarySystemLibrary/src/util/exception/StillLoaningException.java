/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author limwe
 */
public class StillLoaningException extends Exception{
    public static final String BOOK_STILL_LOANING = "Book is still being lent.";
    public StillLoaningException() {}
    
    public StillLoaningException(String message) {
        super(message);
    }
}
