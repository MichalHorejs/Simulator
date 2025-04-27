package com.gina.simulator.init;

import com.gina.simulator.person.Person;
import com.gina.simulator.person.PersonRepository;
import com.gina.simulator.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDataInit implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${data.init.user:false}")
    private boolean loadData;

    @Override
    public void run(String... args){

        if (loadData) {
            if (!personRepository.existsByUsername("admin")){
                Person admin = new Person();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("pass"));
                admin.setRole(Role.ADMIN);

                personRepository.save(admin);
            }

            if (!personRepository.existsByUsername("user")){
                Person person = new Person();
                person.setUsername("user");
                person.setPassword(passwordEncoder.encode("pass"));
                person.setRole(Role.USER);

                personRepository.save(person);
            }

            log.info("Person data initialized");
        }

    }
}
