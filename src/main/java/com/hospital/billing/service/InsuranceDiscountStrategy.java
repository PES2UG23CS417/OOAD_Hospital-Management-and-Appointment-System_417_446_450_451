package com.hospital.billing.service;

/**
 * STRATEGY PATTERN — Concrete Strategy #2 (Billing System).
 *
 * Design Pattern: Strategy (GoF Behavioural).
 *   - Applied when the patient holds valid insurance coverage.
 *   - Discount percentage = 30 %.
 *
 * Design Principle: Single Responsibility Principle (SRP).
 *   - This class has exactly one responsibility: the insurance discount algorithm.
 *   - Any change to insurance discount rules is isolated here; BillingService
 *     and all other strategies are unaffected.
 *
 * Design Principle: Open/Closed Principle (OCP).
 *   - BillingService is closed for modification; it selects this strategy via
 *     the {@link DiscountStrategy} interface without knowing the formula inside.
 *
 * GRASP — Information Expert:
 *   - InsuranceDiscountStrategy is the expert on the 30 % insurance discount rate.
 */
public class InsuranceDiscountStrategy implements DiscountStrategy {

    private static final double INSURANCE_DISCOUNT_PCT = 30.0;

    @Override
    public double apply(double grossAmount) {
        return grossAmount * (1 - INSURANCE_DISCOUNT_PCT / 100.0);
    }

    @Override
    public String getDiscountType()       { return "INSURANCE"; }

    @Override
    public double getDiscountPercentage() { return INSURANCE_DISCOUNT_PCT; }
}
