package bsu.famcs.nikonchik.lab2;

import bsu.famcs.nikonchik.lab2.backend.entities.actors.Admin;
import bsu.famcs.nikonchik.lab2.backend.factories.ActorFactory;
import bsu.famcs.nikonchik.lab2.backend.repositories.actorsrepositories.AdminsRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final AdminsRepository adminsRepository;

    public AdminInitializer(AdminsRepository adminsRepository) {
        this.adminsRepository = adminsRepository;
    }

    @Override
    public void run(String... args) {
        if (adminsRepository.count() == 0) {
            ActorFactory actorFactory = new ActorFactory();
            Admin admin = actorFactory.createAdmin("vittorio_niko");
            adminsRepository.save(admin);
        }
    }
}
