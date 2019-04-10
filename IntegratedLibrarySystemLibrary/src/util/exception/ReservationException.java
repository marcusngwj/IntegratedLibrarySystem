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
public class ReservationException extends Exception {

    public static final String MULTIPLE_RESERVATION = "Member has already reserved this book before.";
    public static final String NO_LOAN_RESERVATION = "Member is not allowed to reserve book that are currently available in the library";

    public ReservationException() {
    }

    public ReservationException(String message) {
        super(message);
    }
}
