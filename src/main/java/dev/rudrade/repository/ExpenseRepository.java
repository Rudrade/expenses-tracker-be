package dev.rudrade.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dev.rudrade.entity.Expense;
import dev.rudrade.filter.ExpenseListFilter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseRepository implements PanacheRepository<Expense> {
    
    public Expense findById(UUID id) {
        return find("id", id).firstResult();
    }

    public void deleteById(UUID id) {
        delete("id", id);
    }

    public List<Expense> findRecent() {
        return find("order by dateOfCreation desc")
            .page(Page.ofSize(5))
            .list();
    }

    public PanacheQuery<Expense> findAll(ExpenseListFilter filter) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        appendParam(params, query, "description", filter.getDescription());
        appendParam(params, query, "category", filter.getCategory());
        appendParam(params, query, "necessity", filter.getNecessity());

        // Construct date range params
        if (filter.getStartDate() != null) {
            if (!query.isEmpty()) {
                query.append(" and ");
            }

            query.append(" dateOfCreation >= :startDate");
            params.put("startDate", filter.getStartDate());
        }
        if (filter.getEndDate() != null) {
            if (!query.isEmpty()) {
                query.append(" and ");
            }

            query.append(" dateOfCreation <= :endDate");
            params.put("endDate", filter.getEndDate());
        }

        try {
        return find(query.toString(), params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void appendParam(Map<String, Object> params, StringBuilder query, String key, String value) {
        if (value == null) return;

        if (query.length() > 0) {
            query.append(" and ");
        }

        query.append("lower(").append(key).append(") LIKE :").append(key);

        params.put(key, "%" + value.toLowerCase() + "%");
    }

    
}
