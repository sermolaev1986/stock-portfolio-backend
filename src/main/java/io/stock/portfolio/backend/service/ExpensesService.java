package io.stock.portfolio.backend.service;

import io.stock.portfolio.backend.client.GoogleSheetsApiClient;
import io.stock.portfolio.backend.controller.model.ExpenseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpensesService {

    private final GoogleSheetsApiClient googleSheetsApiClient;

    public List<ExpenseDTO> getExpenses(String month) {
        String range = month + "!B33:U34";
        final List<List<Object>> data = googleSheetsApiClient.getDataForRange(range);
        var values = data.get(0);
        var labels = data.get(1);

        var result = new ArrayList<ExpenseDTO>();
        for (int i = 0; i < values.size(); i++) {
            result.add(new ExpenseDTO()
                    .setValue(new BigDecimal((String) values.get(i)))
                    .setCategory((String) labels.get(i)));
        }
        return result;
    }
}
