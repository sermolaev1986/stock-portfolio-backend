package io.stock.portfolio.backend.controller;

import io.stock.portfolio.backend.controller.model.TransactionDTO;
import io.stock.portfolio.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/transactions", produces = {"application/json"})
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{owner}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionDTO> getTransactions(@PathVariable String owner) {

        return transactionService.getTransactionsByOwner(owner);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void postTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {

        transactionService.saveTransactions(List.of(transactionDTO));
    }

}
