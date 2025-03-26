package dev.rudrade.repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import dev.rudrade.entity.Expense;
import dev.rudrade.filter.ExpenseListFilter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseRepository implements PanacheRepository<Expense> {
    
    public Expense findById(UUID id) {
        return find("id", id).firstResult();
    }

    public void deleteById(UUID id) {
        delete("id", id);
    }

    public PanacheQuery<Expense> findAll(ExpenseListFilter filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("description", filter.getDescription());
        params.put("amount", filter.getAmount());
        params.put("category", filter.getCategory());
        params.put("necessity", filter.getNecessity());
        params.entrySet().removeIf(e -> e.getValue() == null);

        StringBuilder query = new StringBuilder();
        Iterator<String> iterKeys = params.keySet().iterator();
        while (iterKeys.hasNext()) {
            String key = iterKeys.next();
            query.append(key).append("=:").append(key);
            if (iterKeys.hasNext()) {
                query.append(" and ");
            }
        }

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

}
