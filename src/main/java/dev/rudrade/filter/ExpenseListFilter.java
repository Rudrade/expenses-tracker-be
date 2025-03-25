package dev.rudrade.filter;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseListFilter {

    private int offset;
    private int limit;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Double amount;
    private String category;
    private String necessity;
    private String order; // TODO: IMPL
    
    public ExpenseListFilter(int offset, int limit, LocalDate startDate, LocalDate endDate,
        String description, Double amount, String category, String necessity) {
        setOffset(offset);
        setLimit(limit);
        setStartDate(startDate);
        setEndDate(endDate);
        setDescription(description);
        setAmount(amount);
        setCategory(category);
        setNecessity(necessity);
    }

    public void setLimit(int limit) {
        this.limit = limit <= 0 ? 20 : limit;
    }
}
