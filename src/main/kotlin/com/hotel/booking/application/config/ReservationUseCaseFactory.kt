package com.hotel.booking.application.config

import com.hotel.booking.domain.reservation.services.ReservationFactory
import com.hotel.booking.domain.reservation.services.ReservationGateway
import com.hotel.booking.domain.reservation.validation.ReservationValidator
import com.hotel.booking.domain.reservation.validation.impl.AdvanceReservationLimitValidator
import com.hotel.booking.domain.reservation.validation.impl.ReservationDaysLimitValidator
import com.hotel.booking.domain.reservation.validation.impl.StartAfterBeginningValidator
import com.hotel.booking.domain.reservation.validation.impl.StartDateValidator
import com.hotel.booking.usecase.reservation.AvailabilityValidator
import com.hotel.booking.usecase.reservation.ListAvailabilityUseCase
import com.hotel.booking.usecase.reservation.ReservationBookingUseCase
import com.hotel.booking.usecase.reservation.ReservationCancellationServiceImpl
import com.hotel.booking.usecase.reservation.ReservationCancellationUseCase
import com.hotel.booking.usecase.reservation.ReservationEditingUseCase
import io.micronaut.context.annotation.Factory
import jakarta.inject.Named
import jakarta.inject.Singleton


@Factory
class ReservationUseCaseFactory {

    @Singleton
    fun availabilityValidator(reservationGateway: ReservationGateway) = AvailabilityValidator(
        reservationGateway = reservationGateway
    )

    @Singleton
    @Named("reservationBookingValidatorLists")
    fun reservationBookingValidatorLists(
        reservationFactory: AvailabilityValidator
    ): List<ReservationValidator> = listOf(
        ReservationDaysLimitValidator,
        AdvanceReservationLimitValidator,
        StartAfterBeginningValidator,
        StartDateValidator,
        reservationFactory,
    )

    @Singleton
    fun reservationFactory(
        @Named("reservationBookingValidatorLists") validators: List<ReservationValidator>
    ) = ReservationFactory(
        validators = validators
    )

    @Singleton
    fun reservationBookingUseCase(
        reservationFactory: ReservationFactory,
        reservationGateway: ReservationGateway,
    ) = ReservationBookingUseCase(
        reservationGateway = reservationGateway,
        reservationFactory = reservationFactory,
    )

    @Singleton
    fun reservationCancellationServiceImpl(
        reservationGateway: ReservationGateway
    ) = ReservationCancellationServiceImpl(
        reservationGateway = reservationGateway,
    )

    @Singleton
    fun reservationEditingUseCase(
        reservationCancellationService: ReservationCancellationServiceImpl,
        reservationFactory: ReservationFactory,
        reservationGateway: ReservationGateway,
    ) = ReservationEditingUseCase(
        reservationGateway = reservationGateway,
        reservationFactory = reservationFactory,
        reservationCancellationService = reservationCancellationService
    )

    @Singleton
    fun reservationCancellationUseCase(
        reservationCancellationService: ReservationCancellationServiceImpl
    ) = ReservationCancellationUseCase(
        reservationCancellationService = reservationCancellationService,
    )

    @Singleton
    fun listAvailabilityUseCase(reservationGateway: ReservationGateway) = ListAvailabilityUseCase(
        reservationGateway = reservationGateway,
    )
}
