package dev.rudrade.response;

import java.util.List;

import dev.rudrade.entity.Expense;

public record ExpenseListResponse(long count, List<Expense> expenses) {}
