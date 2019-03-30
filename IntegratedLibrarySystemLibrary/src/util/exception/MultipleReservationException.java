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
public class MultipleReservationException extends Exception {

    public static final String MULTIPLE_RESERVCATION = "Member has already reserved this book before.";

    public MultipleReservationException() {
    }

    public MultipleReservationException(String message) {
        super(message);
    }
}
