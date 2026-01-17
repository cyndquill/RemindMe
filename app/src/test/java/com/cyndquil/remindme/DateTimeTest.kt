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
     * Check underflow remains 0 on TimeUnit types
     */
    fun <T : TimeUnit> checkUnderflow(timeZero: T) {
        assert(timeZero.decrement() == timeZero)
    }

    /**
     * Check underflow and overflow behaves properly on LoopingTimeUnit types
     */
    fun <T: LoopingTimeUnit> checkOverflowAndUnderflow(timeZero: T, timeOne: T, timeMax: T) {
        println(timeZero.decrement().getValue())
        println(timeZero.decrement().getUnitUp()?.getValue())
        println(timeMax.getValue())
        println(timeMax.getUnitUp()?.getValue())
        assert(timeMax.increment().getValue() == timeZero.getValue())
        assert(timeMax.increment().getUnitUp() == timeZero.getUnitUp())
        assert((timeMax + timeOne).getValue() == timeZero.getValue())
        assert((timeMax + timeOne).getUnitUp() == timeZero.getUnitUp())
        assert(timeZero.decrement().getValue() == timeMax.getValue())
        assert(timeZero.decrement().getUnitUp() == timeMax.getUnitUp())
    }

    @Test
    fun addition_isCorrect() {
        // verify that plus === increment() and minus === decrement()
        checkIncrementDecrement(
            timeZero = Year(0),
            timeOne = Year(1)
        )
        checkIncrementDecrement(
            timeZero = Month(0),
            timeOne = Month(1)
        )
        checkIncrementDecrement(
            timeZero = Day(0),
            timeOne = Day(1)
        )
        checkIncrementDecrement(
            timeZero = Hour(0),
            timeOne = Hour(1)
        )
        checkIncrementDecrement(
            timeZero = Minute(0),
            timeOne = Minute(1)
        )

        // only for year, since the rest loop
        checkUnderflow(Year(0))

        // NOTE: timeOne is a SINGLE UNIT! Used for testing plus alongside increment()
        // Also: timeMax is (super = 0:value = MAX), while timeZero is (super = 1:value = 0)
        checkOverflowAndUnderflow(
            timeZero = Month(0, Year(1)),
            timeOne = Month(1),
            timeMax = Month(Month(0).getMax()))
        checkOverflowAndUnderflow(
            timeZero = Day(0, Month(1)),
            timeOne = Day(1),
            timeMax = Day(Day(0).getMax()))
        checkOverflowAndUnderflow(
            timeZero = Hour(0, Day(1)),
            timeOne = Hour(1),
            timeMax = Hour(Hour(0).getMax()))
        checkOverflowAndUnderflow(
            timeZero = Minute(0, Hour(1)),
            timeOne = Minute(1),
            timeMax = Minute(Minute(0).getMax()))
    }
}
