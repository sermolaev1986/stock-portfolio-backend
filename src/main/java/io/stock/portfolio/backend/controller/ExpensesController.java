package io.stock.portfolio.backend.controller;

import io.stock.portfolio.backend.controller.model.ExpenseDTO;
import io.stock.portfolio.backend.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/expenses")
@RequiredArgsConstructor
public class ExpensesController {

    private final ExpensesService expensesService;

    @GetMapping
    public List<ExpenseDTO> getExpenses(@RequestParam String month) {
        return expensesService.getExpenses(month);
    }
}
