package com.civilization.configuration;

import com.civilization.model.Nation;
import com.civilization.model.User;
import com.civilization.repository.NationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class NationsSeedDatabase {

    @Autowired
    private NationRepository nationRepository;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAutoValue;

    @PostConstruct
    public void databaseSeed() {
        if ("create".equals(ddlAutoValue)) {
            nationRepository.save(new Nation("Zulu", ":Zulu:", true));
            nationRepository.save(new Nation("Sweden", ":Sweden:", true));
            nationRepository.save(new Nation("Spain", ":Spain:", true));
            nationRepository.save(new Nation("Siam", ":Siam:", true));
            nationRepository.save(new Nation("Shoshone", ":Shoshone:", true));
            nationRepository.save(new Nation("Rome", ":Rome:", true));
            nationRepository.save(new Nation("Portugal", ":Portugal:", true));
            nationRepository.save(new Nation("Persia", ":Persia:", true));
            nationRepository.save(new Nation("Ottoman", ":Ottoman:", true));
            nationRepository.save(new Nation("Netherlands", ":Netherlands:", true));
            nationRepository.save(new Nation("Morocco", ":Morocco:", true));
            nationRepository.save(new Nation("Maya", ":Maya:", true));
            nationRepository.save(new Nation("Korea", ":Korea:", true));
            nationRepository.save(new Nation("Indonesia", ":Indonesia:", true));
            nationRepository.save(new Nation("India", ":India:", true));
            nationRepository.save(new Nation("Inca", ":Inca:", true));
            nationRepository.save(new Nation("Japan", ":Japan:", true));
            nationRepository.save(new Nation("France", ":France:", true));
            nationRepository.save(new Nation("Ethiopia", ":Ethiopia:", true));
            nationRepository.save(new Nation("China", ":China:", true));
            nationRepository.save(new Nation("Celts", ":Celts:", true));
            nationRepository.save(new Nation("Byzantium", ":Byzantium:", true));
            nationRepository.save(new Nation("Mongol", ":Mongol:", true));
            nationRepository.save(new Nation("Iroquois", ":Iroquois:", true));
            nationRepository.save(new Nation("Assyria", ":Assyria:", true));
            nationRepository.save(new Nation("Austria", ":Austria:", true));
            nationRepository.save(new Nation("Polynesia", ":Polynesia:", true));
            nationRepository.save(new Nation("Babylon", ":Babylon:", true));
            nationRepository.save(new Nation("Songhai", ":Songhai:", true));
            nationRepository.save(new Nation("Carthage", ":Carthage:", true));
            nationRepository.save(new Nation("Egypt", ":Egypt:", true));
            nationRepository.save(new Nation("Huns", ":Huns:", true));
            nationRepository.save(new Nation("Greece", ":Greece:", true));
            nationRepository.save(new Nation("Poland", ":Poland:", true));
            nationRepository.save(new Nation("Brazil", ":Brazil:", true));
            nationRepository.save(new Nation("Denmark", ":Denmark:", true));
            nationRepository.save(new Nation("Aztec", ":Aztec:", true));
            nationRepository.save(new Nation("Arab", ":Arab:", true));
            nationRepository.save(new Nation("Venice", ":Venice:", true));
            nationRepository.save(new Nation("England", ":England:", true));
            nationRepository.save(new Nation("Germany", ":Germany:", true));
            nationRepository.save(new Nation("Russia", ":Russia:", true));
            nationRepository.save(new Nation("America", ":America:", true));

        }
    }
}
