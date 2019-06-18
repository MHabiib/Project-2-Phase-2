package com.future.tcfm.repository;

import com.future.tcfm.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    Expense findByTitle(String title);
    List<Expense> findByGroupNameLike(String groupName);
    Expense findByIdExpense(String id);
}
