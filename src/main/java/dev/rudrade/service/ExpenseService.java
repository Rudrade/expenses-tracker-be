package dev.rudrade.service;

import java.util.List;

import dev.rudrade.entity.Expense;
import dev.rudrade.repository.ExpenseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ExpenseService {

    @Inject private ExpenseRepository expenseRepository;

    public List<Expense> findAll() {
        return expenseRepository.listAll();
    }

}
