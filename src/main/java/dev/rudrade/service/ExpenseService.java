package dev.rudrade.service;

import java.util.List;
import java.util.UUID;

import dev.rudrade.entity.Expense;
import dev.rudrade.filter.ExpenseListFilter;
import dev.rudrade.repository.ExpenseRepository;
import dev.rudrade.response.ExpenseListResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ExpenseService {

    @Inject private ExpenseRepository expenseRepository;

    public ExpenseListResponse findAll(ExpenseListFilter filter) {
        int offset = filter.getOffset() == 0 ? 0 : filter.getLimit() / filter.getOffset();

        PanacheQuery<Expense> expenses = expenseRepository.findAll(filter)
            .page(Page.of(offset, filter.getLimit()));

        return new ExpenseListResponse(
            expenses.count(),
            expenses.list());
    }

    public List<Expense> findAll() {
        return expenseRepository.listAll();
    }

    public Expense findById(String id) {
        return expenseRepository.findById(UUID.fromString(id));
    }

    @Transactional
    public Expense create(Expense expense) {
        if (expense.getId() == null) {
            expenseRepository.persist(expense);
        
        } else {
            Expense bdExpense = expenseRepository.findById(expense.getId());
            if (bdExpense != null) {
                bdExpense.copy(expense);
                bdExpense.persist();
            }
        }

        return expense;
    }

    @Transactional
    public void deleteById(String id) {
        expenseRepository.deleteById(UUID.fromString(id));
    }

    public List<Expense> findRecent() {
        return expenseRepository.findRecent();
    }

}
