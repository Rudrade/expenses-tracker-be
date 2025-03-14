package dev.rudrade.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.isA;

import org.junit.jupiter.api.Test;

import dev.rudrade.entity.Expense;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@TestHTTPEndpoint(ExpenseController.class)
public class ExpensesControllerTest {
    
    @Test
    public void testCreate() {
        Expense expense = new Expense();

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then()
                .statusCode(201)
                .body(isA(Expense.class));
    }


}
