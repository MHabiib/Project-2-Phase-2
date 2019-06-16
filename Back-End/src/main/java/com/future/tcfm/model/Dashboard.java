package com.future.tcfm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "group")
public class Dashboard {
    private double groupBalance;
    private int totalMembers;
    private double pendingPayment;
    private double yourContribution;

    public double getGroupBalance() {
        return groupBalance;
    }

    public void setGroupBalance(double groupBalance) {
        this.groupBalance = groupBalance;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public double getPendingPayment() {
        return pendingPayment;
    }

    public void setPendingPayment(double pendingPayment) {
        this.pendingPayment = pendingPayment;
    }

    public double getYourContribution() {
        return yourContribution;
    }

    public void setYourContribution(double yourContribution) {
        this.yourContribution = yourContribution;
    }
}
