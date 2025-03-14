package dev.rudrade.service;

import java.util.List;
import java.util.UUID;

import dev.rudrade.entity.Expense;
import dev.rudrade.repository.ExpenseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ExpenseService {

    @Inject private ExpenseRepository expenseRepository;

    public List<Expense> findAll() {
        return expenseRepository.listAll();
    }

    public Expense findById(UUID id) {
        return expenseRepository.findById(id);
    }

    @Transactional
    public Expense create(Expense expense) {
        expenseRepository.persist(expense);
        return expense;
    }

    public void deleteById(UUID id) {
        expenseRepository.deleteById(id);
    }

}
