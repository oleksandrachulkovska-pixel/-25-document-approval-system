package com.coursework.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriorityCalculationServiceTest {

    private final PriorityCalculationService service =
            new PriorityCalculationService();

    // Тест 1. Перевірка правильності обчислення інтегрального показника
    @Test
    void shouldCalculateCorrectIntegralIndex() {
        double result = service.calculate(80, 60, 40);

        assertEquals(63, result, 0.001);
    }

    // Тест 2. Перевірка нульових значень усіх факторів
    @Test
    void shouldReturnZeroForZeroFactors() {
        assertEquals(0, service.calculate(0, 0, 0), 0.001);
    }

    // Тест 3. Перевірка максимальних значень усіх факторів
    @Test
    void shouldReturn100ForMaximumFactors() {
        assertEquals(100, service.calculate(100, 100, 100), 0.001);
    }

    // Тест 4. Перевірка впливу першого фактора
    @Test
    void shouldCalculateFirstFactorCorrectly() {
        assertEquals(40, service.calculate(100, 0, 0), 0.001);
    }

    // Тест 5. Перевірка впливу другого фактора
    @Test
    void shouldCalculateSecondFactorCorrectly() {
        assertEquals(35, service.calculate(0, 100, 0), 0.001);
    }

    // Тест 6. Перевірка впливу третього фактора
    @Test
    void shouldCalculateThirdFactorCorrectly() {
        assertEquals(25, service.calculate(0, 0, 100), 0.001);
    }

    // Тест 7. Перевірка відхилення від'ємного значення
    @Test
    void shouldRejectNegativeFactor() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculate(-1, 50, 50));
    }

    // Тест 8. Перевірка відхилення значення понад 100
    @Test
    void shouldRejectFactorGreaterThan100() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculate(101, 50, 50));
    }

    // Тест 9. Перевірка відхилення NaN
    @Test
    void shouldRejectNaN() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculate(Double.NaN, 50, 50));
    }

    // Тест 10. Перевірка відхилення нескінченності
    @Test
    void shouldRejectInfiniteValue() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculate(Double.POSITIVE_INFINITY, 50, 50));
    }
}