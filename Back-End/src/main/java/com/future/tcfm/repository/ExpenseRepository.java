package com.future.tcfm.repository;

import com.future.tcfm.model.Expense;
import com.future.tcfm.service.ExpenseService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    Expense findByTitle(String title);
    List<Expense> findByGroupNameLikeOrderByCreatedDateDesc(String groupName);
    Expense findByIdExpense(String id);
    List<Expense> findTop10ByGroupNameLikeOrderByCreatedDateDesc(String groupName);
}
