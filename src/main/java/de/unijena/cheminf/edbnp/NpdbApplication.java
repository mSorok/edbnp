package de.unijena.cheminf.edbnp;

import de.unijena.cheminf.edbnp.readers.ReadConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



import de.unijena.cheminf.edbnp.storage.StorageProperties;
import de.unijena.cheminf.edbnp.storage.StorageService;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class NpdbApplication {

    public static void main(String[] args) {

        //AnnotationConfigApplicationContext readContext = new AnnotationConfigApplicationContext(ReadConfig.class);



        SpringApplication.run(NpdbApplication.class, args);
    }


    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
