package dev.rudrade.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseListFilter {

    private int offset;
    private int limit;
    
    public ExpenseListFilter(int offset, int limit) {
        setOffset(offset);
        setLimit(limit);
    }

    public void setLimit(int limit) {
        this.limit = limit <= 0 ? 20 : limit;
    }
}
