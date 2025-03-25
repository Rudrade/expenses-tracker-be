package dev.rudrade.controller;

import java.time.LocalDate;

import org.jboss.resteasy.reactive.RestResponse;

import dev.rudrade.entity.Expense;
import dev.rudrade.filter.ExpenseListFilter;
import dev.rudrade.response.ExpenseListResponse;
import dev.rudrade.service.ExpenseService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/expense")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExpenseController {
    
    @Inject private ExpenseService expenseService;

    @GET
    public ExpenseListResponse findAll(@QueryParam("offset") int offset,  @QueryParam("limit") int limit,
        @QueryParam("startDate") LocalDate startDate, @QueryParam("endDate") LocalDate endDate, @QueryParam("description") String description,
        @QueryParam("amount") Double amount, @QueryParam("category") String category, @QueryParam("necessity") String necessity) {

        return expenseService.findAll(new ExpenseListFilter(offset, limit, startDate, endDate, description, amount, category, necessity));
    }

    @GET
    @Path("/{id}")
    public RestResponse<Expense> findById(@PathParam("id") String id) {
        Expense expense = expenseService.findById(id);
        if (expense == null) {
            return RestResponse.noContent();
        }

        return RestResponse.ok(expense);
    }

    @POST
    public Expense create(Expense expense) {
        return expenseService.create(expense);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        expenseService.deleteById(id);
    }

}
