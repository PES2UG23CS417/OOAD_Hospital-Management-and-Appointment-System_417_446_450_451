package com.hospital.billing.service;

/**
 * STRATEGY PATTERN — Concrete Strategy #1 (Billing System).
 *
 * Design Pattern: Strategy (GoF Behavioural).
 *   - Applied when the patient has no insurance and is not a senior citizen.
 *   - Discount percentage = 0 %; net amount equals gross amount.
 *
 * Design Principle: Single Responsibility Principle (SRP).
 *   - This class has exactly one responsibility: representing the "no discount" rule.
 *
 * Design Principle: Open/Closed Principle (OCP).
 *   - This class does not need to change when new discount types are introduced.
 */
public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public double apply(double grossAmount) {
        return grossAmount;   // no discount applied
    }

    @Override
    public String getDiscountType()       { return "NONE"; }

    @Override
    public double getDiscountPercentage() { return 0.0; }
}
