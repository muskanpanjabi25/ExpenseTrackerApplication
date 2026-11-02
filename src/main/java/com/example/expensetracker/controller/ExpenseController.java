package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ExpenseController {

    private static final List<String> CATEGORIES =
            List.of("Food", "Transport", "Rent", "Shopping", "Bills", "Health", "Entertainment", "Other");

    private final ExpenseRepository repo;

    public ExpenseController(ExpenseRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String month, Model model) {
        YearMonth ym = (month == null || month.isBlank()) ? YearMonth.now() : YearMonth.parse(month);
        List<Expense> expenses = repo.findByDateBetweenOrderByDateDesc(ym.atDay(1), ym.atEndOfMonth());

        BigDecimal total = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> byCategory = expenses.stream()
                .collect(Collectors.groupingBy(Expense::getCategory, TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)));

        model.addAttribute("expenses", expenses);
        model.addAttribute("total", total);
        model.addAttribute("byCategory", byCategory);
        model.addAttribute("month", ym.toString());
        return "index";
    }

    @GetMapping("/expenses/new")
    public String newForm(Model model) {
        Expense e = new Expense();
        e.setDate(LocalDate.now());
        model.addAttribute("expense", e);
        model.addAttribute("categories", CATEGORIES);
        return "form";
    }

    @GetMapping("/expenses/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Expense e = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id " + id));
        model.addAttribute("expense", e);
        model.addAttribute("categories", CATEGORIES);
        return "form";
    }

    @PostMapping("/expenses")
    public String save(@Valid @ModelAttribute("expense") Expense expense,
                       BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", CATEGORIES);
            return "form";
        }
        repo.save(expense);
        return "redirect:/";
    }

    @PostMapping("/expenses/{id}/delete")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/";
    }
}
