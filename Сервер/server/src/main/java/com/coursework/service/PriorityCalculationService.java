package com.coursework.service;

public class PriorityCalculationService {

    // Розрахунок інтегрального показника
    public double calculate(double f1, double f2, double f3) {

        validateFactor(f1, "F1");
        validateFactor(f2, "F2");
        validateFactor(f3, "F3");

        double result = 0.40 * f1
                + 0.35 * f2
                + 0.25 * f3;

        return Math.round(result * 100.0) / 100.0;
    }

    // Перевірка значень факторів
    private void validateFactor(double value, String name) {

        if (!Double.isFinite(value) || value < 0 || value > 100) {
            throw new IllegalArgumentException(
                    name + " повинен бути в межах від 0 до 100");
        }
    }
}