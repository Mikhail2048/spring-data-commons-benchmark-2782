package org.example;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.mapping.PersistentPropertyPath;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.stereotype.Component;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Component
public class AppTest {

    private PersistentPropertyPath<RelationalPersistentProperty> name;
    private PersistentPropertyPath<RelationalPersistentProperty> typeEmbedded;
    private PersistentPropertyPath<RelationalPersistentProperty> statusEmbeddedTwice;

    @Setup
    public void setUp() {
        JdbcMappingContext mappingContext = new JdbcMappingContext(DefaultNamingStrategy.INSTANCE);
        mappingContext.setInitialEntitySet(Set.of(MySimpleEntity.class));
        name = mappingContext.getPersistentPropertyPath("name", MySimpleEntity.class);
        typeEmbedded = mappingContext.getPersistentPropertyPath("somethingEmbedded.type", MySimpleEntity.class);
        statusEmbeddedTwice = mappingContext.getPersistentPropertyPath("somethingEmbedded.anotherEmbeddedProperty.status", MySimpleEntity.class);
    }

    @Test
    public void onApplicationReady() throws IOException {
        Main.main(new String[]{});
    }

    @Benchmark
    public boolean testIsBasePathOfTwoNestingLevels() {
        return typeEmbedded.isBasePathOf(statusEmbeddedTwice);
    }

    @Benchmark
    public boolean testIsBasePathOfOneNestingLevel() {
        return name.isBasePathOf(typeEmbedded);
    }

    @Benchmark
    public Object testGetExtensionForBaseOfNested() {
        return statusEmbeddedTwice.getExtensionForBaseOf(typeEmbedded);
    }

    @Benchmark
    public Object testGetExtensionForBaseSingleLevelNesting() {
        return statusEmbeddedTwice.getExtensionForBaseOf(typeEmbedded);
    }
}
