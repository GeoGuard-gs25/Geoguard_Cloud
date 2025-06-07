package global.geoguard.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import global.geoguard.model.EnergyBill;
import global.geoguard.model.Notification;
import global.geoguard.model.User;
import global.geoguard.repository.EnergyBillRepository;
import global.geoguard.repository.NotificationRepository;
import global.geoguard.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Configuration
public class DataBaseSeeder {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private NotificationRepository notificationRepository;

        @Autowired
        private EnergyBillRepository energyBillRepository;

@PostConstruct
public void init() {
    boolean jaTemDados = userRepository.findByEmail("gui@teste.com").isPresent()
                      || userRepository.findByEmail("rafao@teste.com").isPresent();

    if (jaTemDados) {
        System.out.println("Dados já existem no banco. Seeder não será executado.");
        return;
    }


    User gui = User.builder().email("gui@teste.com").name("Guilherme")
                    .password(passwordEncoder.encode("123456")).build();

    User rafa = User.builder().email("rafao@teste.com").name("Rafael")
                    .password(passwordEncoder.encode("78910")).build();

    userRepository.saveAll(List.of(gui, rafa));


    notificationRepository.saveAll(List.of(
            Notification.builder()
                .title("Boas-vindas")
                .message("Bem-vindo à plataforma GeoGuard!")
                .lida(false)
                .build(),

            Notification.builder()
                .title("Atualização de Sistema")
                .message("Atualização concluída com sucesso.")
                .lida(false)
                .build(),

            Notification.builder()
                .title("Consumo Excedente")
                .message("Alerta de consumo elevado")
                .lida(false)
                .build()
    ));

    energyBillRepository.saveAll(List.of(
            EnergyBill.builder()
                .owner(gui)
                .consumoKwh(320.0)
                .valorKwh(0.75)
                .amount(240.00)
                .month("2025-01")
                .build(),

            EnergyBill.builder()
                .owner(gui)
                .consumoKwh(300.0)
                .valorKwh(0.78)
                .amount(234.00)
                .month("2025-02")
                .build(),

            EnergyBill.builder()
                .owner(rafa)
                .consumoKwh(280.0)
                .valorKwh(0.80)
                .amount(224.00)
                .month("2025-01")
                .build()
    ));

    System.out.println("Seeder executado com sucesso.");
}



}
