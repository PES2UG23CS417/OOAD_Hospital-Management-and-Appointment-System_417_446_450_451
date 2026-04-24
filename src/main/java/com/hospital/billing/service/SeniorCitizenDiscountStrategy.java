package com.hospital.billing.service;

/**
 * STRATEGY PATTERN — Concrete Strategy #3 (Billing System).
 *
 * Design Pattern: Strategy (GoF Behavioural).
 *   - Applied when the patient is 60 years old or older and has no insurance.
 *   - Discount percentage = 15 %.
 *
 * Design Principle: Single Responsibility Principle (SRP).
 *   - This class has exactly one responsibility: the senior-citizen discount algorithm.
 *   - Age eligibility logic (≥ 60) and the rate (15 %) are encapsulated here.
 *
 * Design Principle: Open/Closed Principle (OCP).
 *   - Changing the senior discount threshold or rate only touches this class.
 *
 * GRASP — Information Expert:
 *   - SeniorCitizenDiscountStrategy is the expert on the 15 % senior discount.
 */
public class SeniorCitizenDiscountStrategy implements DiscountStrategy {

    private static final double SENIOR_DISCOUNT_PCT   = 15.0;
    public  static final int    SENIOR_AGE_THRESHOLD  = 60;

    @Override
    public double apply(double grossAmount) {
        return grossAmount * (1 - SENIOR_DISCOUNT_PCT / 100.0);
    }

    @Override
    public String getDiscountType()       { return "SENIOR_CITIZEN"; }

    @Override
    public double getDiscountPercentage() { return SENIOR_DISCOUNT_PCT; }
}
