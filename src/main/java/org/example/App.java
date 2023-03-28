package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.repository.CrudRepository;

@SpringBootApplication
@EnableJdbcRepositories(basePackageClasses = MySimpleEntityRepository.class)
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}

interface MySimpleEntityRepository extends CrudRepository<MySimpleEntity, Long> {}

class MySimpleEntity {

    @Id
    private Long id;

    private String name;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private SomethingEmbedded somethingEmbedded;
}

class SomethingEmbedded {

    private String type;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private AnotherEmbeddedProperty anotherEmbeddedProperty;
}

class AnotherEmbeddedProperty {

    private String status;
}