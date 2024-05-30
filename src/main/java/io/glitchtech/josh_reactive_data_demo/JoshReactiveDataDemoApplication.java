package io.glitchtech.josh_reactive_data_demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class JoshReactiveDataDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JoshReactiveDataDemoApplication.class, args);
    }

}

@Component
@RequiredArgsConstructor
@Log4j2
class SampleDataInitializer {
    // Field will be initialized by the lombok annotation using the fields
    private final ReservationRepository reservationRepository;

    // Gets called when the application is ready - loaded
    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        // Create a list of names
        Flux<String> names = Flux.just(
                "Muzi", "Rea", "Liyanda", "Bomme", "Themba", "Ntando", "Bongi", "Kaykay"
        );
        // Create a Flux of reservations from the list of names
        Flux<Reservation> reservations = names.map(name -> new Reservation(null, name));
        // reservationRepository.save(Reservation) returns Mono<Reservation>
        // But we need a list which is Flux<Reservation>, Mono<Reservation> is only one
        // So to flatten Flux<<Mono<Reservation>> to Flux<Reservation> we use flatMap
        Flux<Reservation> saved = reservations.flatMap(this.reservationRepository::save);

        // Clean the database before saving data
        this.reservationRepository
                // delete all data in the DB
                .deleteAll()
                // Create a Publisher  from saved Flux<Reservation>
                .thenMany(saved)
                // Read the records from above step
                .thenMany(this.reservationRepository.findAll())
                // log each
                .subscribe(log::info);

       /* // Cleaner method
        Flux<Reservation> reservationFlux = Flux.just(
                        "Muzi", "Rea", "Liyanda", "Bomme", "Themba", "Ntando", "Bongi", "Kaykay"
                ).map(name -> new Reservation(null, name))
                .flatMap(reservationRepository::save);
        // Start the stream
        reservationFlux.subscribe(log::info:while ();*/
    }
}

// Persist to Mongo Database
interface ReservationRepository extends ReactiveCrudRepository<Reservation, String> {
}

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
class Reservation {
    @Id
    private String id;
    private String name;
}
