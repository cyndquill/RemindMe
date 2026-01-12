package com.cyndquil.remindme.util

/**
 * Enum representing each day of the week (`SUNDAY`=0 -> `SATURDAY`=6)
 */
enum class DayOfWeek() {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    /**
     * Gets the number of days in a week
     */
    fun length(): Int = entries.size
    /**
     * Increments the day of the week by one (loops forwards)
     */
    fun increment(): DayOfWeek = entries[Math.floorMod(ordinal + 1, length())]
    /**
     * Decrements the day of the week by one (loops backwards)
     */
    fun decrement(): DayOfWeek = entries[Math.floorMod(ordinal - 1, length())]
    /**
     * Adds `value` days to the current day of the week
     */
    operator fun plus(value: Int): DayOfWeek = entries[Math.floorMod(ordinal + value, length())]
    /**
     * Represents the day of the week as a string
     */
    fun asString(): String = name
    /**
     * Gets the position of the day in the week
     */
    fun getValue(): Int = ordinal
}

/**
 * Enum representing each month of the year (`JANUARY`=0 -> `DECEMBER`=11)
 *
 * @param days The number of days (typically) in a month (`FEBRUARY`=28)
 * @param isLeapYear Whether the current year is a leap year (for `FEBRUARY`)
 */
enum class MonthOfYear(private val days: Int) {
    JANUARY(31),
    FEBRUARY(28),
    MARCH(31),
    APRIL(30),
    MAY(31),
    JUNE(30),
    JULY(31),
    AUGUST(31),
    SEPTEMBER(30),
    OCTOBER(31),
    NOVEMBER(30),
    DECEMBER(31);

    /**
     * Gets the number of months in a year
     */
    fun length(): Int = entries.size
    /**
     * Increments the month of the year by one (loops forwards)
     */
    fun increment(): MonthOfYear = entries[Math.floorMod(ordinal + 1, length())]
    /**
     * Decrements the month of the year by one (loops backwards)
     */
    fun decrement(): MonthOfYear = entries[Math.floorMod(ordinal - 1, length())]
    /**
     * Adds `value` months to the current month of the year
     */
    operator fun plus(value: Int): MonthOfYear = entries[Math.floorMod(ordinal + value, length())]
    /**
     * Represents the month of the year as a string
     */
    fun asString(): String = name
    /**
     * Gets the position of the month in the year
     */
    fun getValue(): Int = ordinal
    /**
     * Gets the number of days in a month
     */
    fun getDays(isLeapYear: Boolean): Int = if (isLeapYear && this == FEBRUARY) (days + 1) else days

    companion object {
        /**
         * Creates a MonthOfYear from an Int
         */
        fun fromInt(value: Int): MonthOfYear = entries[value]
    }
}

/**
 * An abstract class representing a time unit
 *
 * @param value The value of the current unit
 * @param unitUp A TimeUnit object for a single unit above the current TimeUnit
 */
abstract class TimeUnit(private val value: Int, private val unitUp: TimeUnit ?= null) {
    /**
     * Gets the current value of this time unit
     */
    fun getValue(): Int = value
    /**
     * Gets the instance of the TimeUnit object unitUp
     */
    fun getUnitUp(): TimeUnit? = unitUp
    /**
     * Adds a TimeUnit to a TimeUnit of the same instance
     * @return The sum of the values of the two TimeUnits
     */
    abstract operator fun plus(that: TimeUnit): TimeUnit
    /**
     * A helper function for the plus operator
     */
    protected fun addTimeUnit(that: TimeUnit): Int = getValue() + that.getValue()
    /**
     * Adds one to the value of TimeUnit
     */
    abstract fun increment(): TimeUnit
    /**
     * Helper function for increment
     */
    protected fun addOne(): Int = getValue() + 1
    /**
     * Subtracts one to the value of TimeUnit
     */
    abstract fun decrement(): TimeUnit
    /**
     * Helper function for decrement
     */
    protected fun subtractOneOrNone(): Int = if (getValue() == 0) 0 else getValue() - 1
    /**
     * Check for equality between TimeUnit types
     */
    override fun equals(other: Any?): Boolean {
        if (other === null) return false
        if (other is TimeUnit) {
            val otherUnitUp = other.getUnitUp()
            val unitUpEqual = ((otherUnitUp == null) && (unitUp == null)) || (otherUnitUp == unitUp)
            return (value == other.getValue()) && unitUpEqual
        } else return false
    }
}

/**
 * A TimeUnit for representing Years
 * @param value The value of the year (A.D.)
 */
class Year(private val value: Int): TimeUnit(value, null) {
    /**
     * Returns whether this year is a leap year or not
     */
    fun isLeapYear(): Boolean = ((getValue() % 4) == 0) && (getValue() != 0)
    override operator fun plus(that: TimeUnit): Year = Year(this.addTimeUnit(that))
    override fun increment(): Year = Year(addOne())
    override fun decrement(): Year = Year(subtractOneOrNone())
}

/**
 * A class for Looping Numbers around a max value
 * @param value The current value of the number
 * @param max The max value the number can hold
 */
class LoopingNumber(private val value: Int, private val max: Int) {
    private val realMax = max + 1
    operator fun plus(that: Int): LoopingNumber = LoopingNumber(Math.floorMod(value + that, realMax), max)
    operator fun minus(that: Int): LoopingNumber = LoopingNumber(Math.floorMod(value - that, realMax), max)
    fun willOverflow(): Boolean = value == max
    fun willUnderflow(): Boolean = value == 0
    fun toInt(): Int = value
}

/**
 * An abstract class representing a looping time unit
 *
 * @param value The value of the looping time unit (where value < max)
 * @param max The maximum value the looping time unit can be
 * @param unitUp See TimeUnit.unitUp
 */
abstract class LoopingTimeUnit(
    private val value: Int,
    private val max: Int,
    private val unitUp: TimeUnit?,
): TimeUnit(value, unitUp) {
    private val loopingValue = LoopingNumber(value, max)
    /**
     * Gets the max value a LoopingTimeUnit can be set to
     */
    fun getMax(): Int = max
    /**
     * A helper function for the plus operator
     * @return A pair containing if unitUp is null, it's updated value, and the sum of the addition
     */
    protected fun addLoopingTimeUnit(that: TimeUnit): Pair<Int?, Int> {
        val nextValue = (loopingValue + that.getValue()).toInt()
        val unitUpRollover = if (nextValue < getValue()) 1 else 0
        val thatUnitUp = that.getUnitUp()?.getValue()
        val nextUnitUp = if (unitUp != null) (if (thatUnitUp != null) (unitUp.getValue() + unitUpRollover + thatUnitUp) else null) else null
        return Pair(nextUnitUp, nextValue)
    }
    /**
     * Converts an Int? object to an Int
     * @return The value of `value`, or 0 if `null`
     */
    protected fun valueOrZero(value: Int?): Int = if (value == null) 0 else value
    /**
     * Helper function for increment
     */
    protected fun incrementHelper(): Pair<Int, Boolean> {
        return Pair((loopingValue + 1).toInt(), loopingValue.willOverflow())
    }
    /**
     * Helper function for decrement
     */
    protected fun decrementHelper(): Pair<Int, Boolean> {
        return Pair((loopingValue - 1).toInt(), loopingValue.willUnderflow())
    }
}

/**
 * A TimeUnit for representing Months
 * @param month The value of the month, as a MonthOfYear
 * @param year The value of the year, as a Year
 */
class Month(
    private val month: MonthOfYear,
    private val year: Year,
): LoopingTimeUnit(
    value = month.getValue(),
    max = month.length()-1,
    unitUp = year,
) {
    constructor(value: Int, year: Year): this(MonthOfYear.fromInt(value), year)
    constructor(value: Int): this(MonthOfYear.fromInt(value), Year(0))
    /**
     * Getter for the MonthOfYear object
     */
    fun getMonthOfYear(): MonthOfYear = month
    /**
     * Getter for the Year object
     */
    fun getYear(): Year = year
    override operator fun plus(that: TimeUnit): Month {
        val (nextUnitUp, nextValue) = addLoopingTimeUnit(that)
        return Month(nextValue, Year(valueOrZero(nextUnitUp)))
    }
    override fun increment(): Month {
        val (nextValue, willOverflow) = incrementHelper()
        return Month(nextValue, if (willOverflow) getYear().increment() else getYear())
    }
    override fun decrement(): Month {
        val (nextValue, willUnderflow) = decrementHelper()
        return Month(nextValue, if (willUnderflow) getYear().decrement() else getYear())
    }
}

/**
 * A TimeUnit for representing Days
 * @param day The value of the day, as an Int
 * @param dayOfWeek The value of the day of the week, as a DayOfWeek?
 * @param month The value of the month, as a Month
 */
class Day(
    private val value: Int,
    private val dayOfWeek: DayOfWeek?,
    private val month: Month,
): LoopingTimeUnit(
    value = value,
    max = month.getMonthOfYear().getDays(month.getYear().isLeapYear())-1,
    unitUp = month,
) {
    constructor(value: Int, month: Month): this(value, null, month)
    constructor(value: Int): this(value, null, Month(0))
    /**
     * Getter for the DayOfWeek object (may be null)
     */
    fun getDayOfWeek(): DayOfWeek? = dayOfWeek
    /**
     * Getter for the Month object
     */
    fun getMonth(): Month = month
    override operator fun plus(that: TimeUnit): Day {
        val (nextUnitUp, nextValue) = addLoopingTimeUnit(that)
        val nextDayOfWeek = dayOfWeek?.let { dayOfWeek + that.getValue() }
        val nextMonth = getMonth() + Month(valueOrZero(nextUnitUp))
        return Day(nextValue, nextDayOfWeek, nextMonth)
    }
    override fun increment(): Day {
        val (nextValue, willOverflow) = incrementHelper()
        return Day(nextValue, getDayOfWeek()?.increment(), if (willOverflow) getMonth().increment() else getMonth())
    }
    override fun decrement(): Day {
        val (nextValue, willUnderflow) = decrementHelper()
        return Day(nextValue, getDayOfWeek()?.decrement(), if (willUnderflow) getMonth().decrement() else getMonth())
    }
}

class Hour(
    private val value: Int,
    private val day: Day,
): LoopingTimeUnit(
    value = value,
    max = 23,
    unitUp = day,
) {
    constructor(value: Int): this(value, Day(0))
    /**
     * Getter for the Day object
     */
    fun getDay(): Day = day
    /**
     * Gets hour in military (24 hour) time
     */
    fun getMilitary(): Int = value
    /**
     * Gets hour in AM/PM (12 hour) time
     */
    fun getTwelve(): Int = if (value == 0) 12 else value
    /**
     * Gets military time as a string
     */
    fun asStringMilitary(): String = getMilitary().toString()
    /**
     * Gets AM/PM time as a string
     */
    fun asStringTwelve(): String = getTwelve().toString() + (if (getMilitary() > 11) " P.M." else " A.M.")
    override operator fun plus(that: TimeUnit): Hour {
        val (nextUnitUp, nextValue) = addLoopingTimeUnit(that)
        val nextDay = getDay() + Day(valueOrZero(nextUnitUp))
        return Hour(nextValue, nextDay)
    }
    override fun increment(): Hour {
        val (nextValue, willOverflow) = incrementHelper()
        return Hour(nextValue, if (willOverflow) getDay().increment() else getDay())
    }
    override fun decrement(): Hour {
        val (nextValue, willUnderflow) = decrementHelper()
        return Hour(nextValue, if (willUnderflow) getDay().decrement() else getDay())
    }
}

class Minute(
    private val value: Int,
    private val hour: Hour,
): LoopingTimeUnit(
    value = value,
    max = 59,
    unitUp = hour,
) {
    constructor(value: Int): this(value, Hour(0))
    fun getHour(): Hour = hour
    override operator fun plus(that: TimeUnit): Minute {
        val (nextUnitUp, nextValue) = addLoopingTimeUnit(that)
        val nextHour = getHour() + Hour(valueOrZero(nextUnitUp))
        return Minute(nextValue, nextHour)
    }
    override fun increment(): Minute {
        val (nextValue, willOverflow) = incrementHelper()
        return Minute(nextValue, if (willOverflow) getHour().increment() else getHour())
    }
    override fun decrement(): Minute {
        val (nextValue, willUnderflow) = decrementHelper()
        return Minute(nextValue, if (willUnderflow) getHour().decrement() else getHour())
    }
}
