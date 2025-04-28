package com.gestionecole.controller;


import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Metamodel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class DatabaseSmokeTest {

    @Autowired
    private EntityManager entityManager;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testTablesExist() {
        jdbcTemplate.query(
                "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = 'PUBLIC'",
                (rs) -> {
                    System.out.println("==== DATABASE TABLES ====");
                    while (rs.next()) {
                        System.out.println(rs.getString("table_name"));
                    }
                    System.out.println("=========================");
                }
        );
    }

    @Test
    void testEntityMetamodel() {
        Metamodel metamodel = entityManager.getMetamodel();
        System.out.println("==== REGISTERED ENTITIES ====");
        metamodel.getEntities().forEach(e ->
                System.out.println(" - " + e.getName())
        );
        System.out.println("=============================");
    }
}

