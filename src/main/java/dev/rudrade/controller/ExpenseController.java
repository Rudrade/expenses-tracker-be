package dev.rudrade.controller;

import java.util.List;

import dev.rudrade.entity.Expense;
import dev.rudrade.service.ExpenseService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/expense")
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseController {
    
    @Inject private ExpenseService expenseService;

    @GET
    public List<Expense> getAll() {
        return expenseService.findAll();
    }

}
