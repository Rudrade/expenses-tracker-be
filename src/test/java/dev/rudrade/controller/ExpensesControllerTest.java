package dev.rudrade.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

import org.jboss.resteasy.reactive.RestResponse.StatusCode;
import org.junit.jupiter.api.Test;

import dev.rudrade.entity.Expense;
import dev.rudrade.response.ExpenseListResponse;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@TestHTTPEndpoint(ExpenseController.class)
class ExpensesControllerTest {
    
    // ################## findAll ######################### //

    @Test
    void testFindByAll() {

    }

    @Test
    void testFindByNecessity() {

    }

    @Test
    void testFindByCategory() {

    }

    @Test
    void testFindByAmount() {

    }

    @Test
    void testFindByDescription() {

    }

    @Test
    void testFindAllEmpty() {
        Expense expense = constructExpense();

        Expense expense1 = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then().extract().as(Expense.class);

        Expense expense2 = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then().extract().as(Expense.class);

        ExpenseListResponse resultExpenses = given()
            .when().get()
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(ExpenseListResponse.class);

        assertThat(resultExpenses.count())
            .isEqualTo(2);

        assertThat(resultExpenses.expenses())
            .containsExactlyInAnyOrder(expense1, expense2);
    }

    // ################## findById ######################## //

    @Test
    void testFindById() {
        Expense expense = new Expense();
        expense.setDateOfCreation(LocalDate.now());
        expense.setDescription("Teste despesa");
        expense.setAmount(26.7);
        expense.setCategory("Categoria teste");
        expense.setNecessity("Sem necessidade");

        Expense expense1 = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then().extract().as(Expense.class);

        Expense result = given()
            .pathParam("id", expense1.getId())
            .when().get("/{id}")
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(Expense.class);

        assertThat(result)
            .isNotNull();

        assertThat(result)
            .usingDefaultComparator()
            .isEqualTo(expense1);
    }

    @Test
    void testFindByIdEmpty() {
        String result = given()
            .pathParam("id", UUID.randomUUID())
            .when().get("/{id}")
            .then()
                .statusCode(StatusCode.NO_CONTENT)
                .extract().asString();

        assertThat(result).isNullOrEmpty();
    }

    @Test
    void testFindByIdInvalidUUID() {
        String result = given()
            .pathParam("id", "123")
            .when().get("/{id}")
            .then()
                .statusCode(StatusCode.BAD_REQUEST)
                .extract().asString();
        
        assertThat(result).isEqualTo("Invalid UUID string: 123");
    }

    // ################## create ########################## //
    
    @Test
    void testCreate() {
        Expense expense = constructExpense();

        Expense response = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(Expense.class);

        assertThat(response).isNotNull();

        assertThat(response)
            .usingRecursiveComparison()
            .ignoringFields("id");

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void testCreateWithoutDateOfCreation() {
        Expense expense = new Expense();
        expense.setDescription("Teste despesa");
        expense.setAmount(26.7);
        expense.setCategory("Categoria teste");
        expense.setNecessity("Sem necessidade");

        String response = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then()
                .statusCode(StatusCode.BAD_REQUEST)
                .extract().asString();

        assertThat(response).isEqualTo("Invalid property value of dateOfCreation");
    }

    @Test
    void testCreateWithoutDescription() {
        Expense expense = new Expense();
        expense.setDateOfCreation(LocalDate.now());
        expense.setAmount(26.7);
        expense.setCategory("Categoria teste");
        expense.setNecessity("Sem necessidade");

        String response = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then()
                .statusCode(StatusCode.BAD_REQUEST)
                .extract().asString();

        assertThat(response).isEqualTo("Invalid property value of description");
    }

    @Test
    void testCreateWithoutAmount() {
        Expense expense = new Expense();
        expense.setDateOfCreation(LocalDate.now());
        expense.setDescription("Teste despesa");
        expense.setCategory("Categoria teste");
        expense.setNecessity("Sem necessidade");

        String response = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then()
                .statusCode(StatusCode.BAD_REQUEST)
                .extract().asString();

        assertThat(response).isEqualTo("Invalid property value of amount");
    }

    @Test
    void testUpdate() {
        Expense expense = constructExpense();

        Expense response1 = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(Expense.class);

        Expense expense2 = new Expense();
        expense2.setId(response1.getId());
        expense2.setDateOfCreation(LocalDate.of(2020, 1, 15));
        expense2.setDescription("Alterado");
        expense2.setAmount(7845.68);
        expense2.setCategory("Cat");
        expense2.setNecessity("Com alguma");

        Expense response2 = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense2)
            .when().post()
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(Expense.class);

        assertThat(response2).isEqualTo(expense2);
    }

    // ################ delete ######################### //

    @Test
    void testDelete() {
        Expense expense = constructExpense();

        Expense response1 = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(Expense.class);

        given()
            .pathParam("id", response1.getId().toString())
            .when().delete("/{id}")
            .then().statusCode(StatusCode.NO_CONTENT);

        given()
            .pathParam("id", response1.getId().toString())
            .when().get("/{id}")
            .then().statusCode(StatusCode.NO_CONTENT);
    }

    private Expense constructExpense() {
        Expense expense = new Expense();
        expense.setDateOfCreation(LocalDate.now());
        expense.setDescription("Teste despesa");
        expense.setAmount(26.7);
        expense.setCategory("Categoria teste");
        expense.setNecessity("Sem necessidade");
        return expense;
    }
}
