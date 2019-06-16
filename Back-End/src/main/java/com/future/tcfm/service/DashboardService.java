package com.future.tcfm.service;

import com.future.tcfm.model.Dashboard;
import org.springframework.web.bind.annotation.PathVariable;

public interface DashboardService {
    Dashboard getData(String email);
}
