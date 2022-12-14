package com.hotel.booking.usecase.reservation

import com.hotel.booking.domain.reservation.entities.ConfirmedReservation
import com.hotel.booking.domain.reservation.services.ReservationFactory
import com.hotel.booking.domain.reservation.services.ReservationGateway
import java.time.LocalDate

class ReservationBookingUseCase(
    private val reservationGateway: ReservationGateway,
    private val reservationFactory: ReservationFactory
) {
    suspend fun execute(clientId: String, startDate: LocalDate, endDate: LocalDate): ConfirmedReservation {
        return reservationFactory.create(clientId = clientId, startDate = startDate, endDate = endDate)
            .let {
                reservationGateway.create(it)
            }
    }
}
