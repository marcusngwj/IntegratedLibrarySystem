/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfservicekioskterminal.module;

import ejb.session.stateless.ReservationEntityControllerRemote;

/**
 *
 * @author limwe
 */
class KioskReservationManagementModule {

    ReservationEntityControllerRemote reservationEntityControllerRemote;
    
    public KioskReservationManagementModule() {
    }

    KioskReservationManagementModule(ReservationEntityControllerRemote reservationEntityControllerRemote) {
        this.reservationEntityControllerRemote = reservationEntityControllerRemote;
    }
    
}
