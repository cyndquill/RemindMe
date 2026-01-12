package com.cyndquil.remindme

import org.junit.Test

import org.junit.Assert.*

import com.cyndquil.remindme.util.*

/**
 * Unit testing for DateTime objects
 */
class DateTimeTest {
    /**
     * Check incrementing / decremeneting and addition operations
     */
    fun <T : TimeUnit> checkIncrementDecrement(timeZero: T, timeOne: T) {
        assert(timeZero.increment() == timeOne)
        assert(timeOne.decrement() == timeZero)
        assert((timeZero + timeOne) == timeOne)
    }

    /**
     * Check underflow doesn't overflow on TimeUnit types
     */
    fun <T : TimeUnit> checkUnderflow(timeZero: T) {
        assert(timeZero.decrement() == timeZero)
    }

    /**
     * Check underflow and overflow behaves properly on LoopingTimeUnit types
     */
    fun <T: LoopingTimeUnit> checkOverflowAndUnderflow(timeZero: T, timeOne: T, timeMax: T) {
        assert(timeMax.increment().getValue() == timeZero.getValue())
        assert(timeMax.increment().getUnitUp() == timeZero.getUnitUp()?.increment())
        assert((timeMax + timeOne).getValue() == timeZero.getValue())
        assert((timeMax + timeOne).getUnitUp() == timeZero.getUnitUp()?.increment())
        assert(timeZero.decrement().getValue() == timeMax.getValue())
        assert(timeZero.decrement().getUnitUp() == timeMax.getUnitUp()?.decrement())
    }

    @Test
    fun addition_isCorrect() {
        checkIncrementDecrement(Year(0), Year(1))
        checkIncrementDecrement(Month(0), Month(1))
        checkIncrementDecrement(Day(0), Day(1))
        checkIncrementDecrement(Hour(0), Hour(1))
        checkIncrementDecrement(Minute(0), Minute(1))
        checkUnderflow(Year(0))
        checkOverflowAndUnderflow(Month(0), Month(1), Month(Month(0).getMax()))
        checkOverflowAndUnderflow(Day(0), Day(1), Day(Day(0).getMax()))
        checkOverflowAndUnderflow(Hour(0), Hour(1), Hour(Hour(0).getMax()))
        checkOverflowAndUnderflow(Minute(0), Minute(1), Minute(Minute(0).getMax()))
    }
}
