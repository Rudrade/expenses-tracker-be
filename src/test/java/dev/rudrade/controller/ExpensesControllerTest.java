package dev.rudrade.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Comparator;
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

        LocalDate endDate = expense1.getDateOfCreation().plusDays(5);

        ExpenseListResponse response = given()
            .queryParam("description", expense1.getDescription())
            .queryParam("amount", expense1.getAmount())
            .queryParam("category", expense1.getCategory())
            .queryParam("necessity", expense1.getNecessity())
            .queryParam("startDate", expense1.getDateOfCreation().toString())
            .queryParam("endDate", endDate.toString())
            .when().get()
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(ExpenseListResponse.class);

        assertThat(response.count()).isGreaterThanOrEqualTo(2);

        assertThat(response.expenses())
            .usingElementComparator(Comparator.comparing(Expense::getId))
            .containsOnlyOnce(expense1, expense2);
    }

    @Test
    void testFindByStartDate() {
        Expense expense = constructExpense();

        Expense expense1 = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then().extract().as(Expense.class);

        expense.setDateOfCreation(LocalDate.of(2000, 1, 1));
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post();

        ExpenseListResponse response = given()
            .queryParam("startDate", expense1.getDateOfCreation().toString())
            .when().get()
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(ExpenseListResponse.class);

        assertThat(response.expenses())
            .allSatisfy(e -> e.getDateOfCreation().isAfter(expense1.getDateOfCreation()));
        assertThat(response.expenses())
            .containsOnlyOnce(expense1);
    }

    @Test
    void testFindByEndDate() {
        Expense expense = constructExpense();

        Expense expense1 = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then().extract().as(Expense.class);

        expense.setDateOfCreation(LocalDate.now().plusYears(1));
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post();

        ExpenseListResponse response = given()
            .queryParam("endDate", expense1.getDateOfCreation().toString())
            .when().get()
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(ExpenseListResponse.class);

        assertThat(response.expenses())
            .allSatisfy(e -> e.getDateOfCreation().isBefore(expense1.getDateOfCreation()));
        assertThat(response.expenses())
            .containsOnlyOnce(expense1);
    }

    @Test
    void testFindByPartialNecessity() {
        Expense expense = constructExpense();

        Expense expense1 = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then().extract().as(Expense.class);

        expense.setNecessity("2 description");
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post();

        ExpenseListResponse response = given()
            .queryParam("necessity", "Sem")
            .when().get()
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(ExpenseListResponse.class);

        assertThat(response.expenses())
            .usingElementComparator(Comparator.comparing(Expense::getNecessity))
            .containsOnly(expense1);
    }


    @Test
    void testFindByNecessity() {
        Expense expense = constructExpense();

        Expense expense1 = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then().extract().as(Expense.class);

        expense.setNecessity("2 description");
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post();

        ExpenseListResponse response = given()
            .queryParam("necessity", expense1.getNecessity())
            .when().get()
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(ExpenseListResponse.class);

        assertThat(response.expenses())
            .usingElementComparator(Comparator.comparing(Expense::getNecessity))
            .containsOnly(expense1);
    }

    @Test
    void testFindByCategory() {
        Expense expense = constructExpense();

        Expense expense1 = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then().extract().as(Expense.class);

        expense.setCategory("2 description");
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post();

        ExpenseListResponse response = given()
            .queryParam("category", expense1.getCategory())
            .when().get()
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(ExpenseListResponse.class);

        assertThat(response.expenses())
            .usingElementComparator(Comparator.comparing(Expense::getCategory))
            .containsOnly(expense1);
    }

    @Test
    void testFindByDescription() {
        Expense expense = constructExpense();

        Expense expense1 = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post()
            .then().extract().as(Expense.class);

        expense.setDescription("2 description");
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(expense)
            .when().post();

        ExpenseListResponse response = given()
            .queryParam("description", expense1.getDescription())
            .when().get()
            .then()
                .statusCode(StatusCode.OK)
                .body(notNullValue())
                .extract().as(ExpenseListResponse.class);

        assertThat(response.expenses())
            .usingElementComparator(Comparator.comparing(Expense::getDescription))
            .containsOnly(expense1);
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
            .isGreaterThanOrEqualTo(2);

        assertThat(resultExpenses.expenses())
            .containsOnlyOnce(expense1, expense2);
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
