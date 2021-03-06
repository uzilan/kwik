package com.github.jcornaz.kwik.generator.stdlib

import com.github.jcornaz.kwik.generator.api.Generator
import com.github.jcornaz.kwik.generator.api.withSamples
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.temporal.ChronoField
import kotlin.random.Random

private const val MAX_NANOSECONDS = 999_999_999
private val MIN_DURATION = Duration.ofSeconds(Long.MIN_VALUE)
private val MAX_DURATION = Duration.ofSeconds(Long.MAX_VALUE, MAX_NANOSECONDS.toLong())

/**
 * Returns a generator of [Instant] between [min] and [max] (inclusive)
 */
fun Generator.Companion.instants(
    min: Instant = Instant.MIN,
    max: Instant = Instant.MAX
): Generator<Instant> {
    require(max >= min) {
        "Max must be equal or after min but min was $min and max was $max"
    }

    val range = min..max

    val samples = mutableListOf(min, max)

    if (Instant.EPOCH in range && Instant.EPOCH !in samples) {
        samples += Instant.EPOCH
    }

    return Generator { random: Random ->
        val seconds =
            if (min.epochSecond == max.epochSecond) min.epochSecond
            else random.nextLong(from = min.epochSecond, until = max.epochSecond)

        val instant = Instant.ofEpochSecond(seconds)

        val minNano =
            if (instant.epochSecond == min.epochSecond) min.nano
            else 0

        val maxNano =
            if (instant.epochSecond == instant.epochSecond) max.nano
            else MAX_NANOSECONDS

        instant.with(
            ChronoField.NANO_OF_SECOND,
            random.nextLong(from = minNano.toLong(), until = maxNano.toLong() + 1)
        )
    }.withSamples(samples)
}

/**
 * Returns a generator of [Duration] between [min] and [max] (inclusive)
 */
fun Generator.Companion.durations(
    min: Duration = MIN_DURATION,
    max: Duration = MAX_DURATION
): Generator<Duration> {
    require(min <= max) {
        "Min must be shorter than max but min was $min and max was $max"
    }

    val range = min..max

    val samples = mutableListOf(min, max)

    if (Duration.ZERO in range && Duration.ZERO !in samples) {
        samples += Duration.ZERO
    }

    return Generator { random: Random ->
        val seconds =
            if (min.seconds == max.seconds) min.seconds
            else random.nextLong(from = min.seconds, until = max.seconds)

        val duration = Duration.ofSeconds(seconds)

        val minNano =
            if (duration.seconds == min.seconds) min.nano
            else 0

        val maxNano =
            if (duration.seconds == max.seconds) max.nano
            else MAX_NANOSECONDS

        duration.withNanos(random.nextInt(from = minNano, until = maxNano + 1))
    }.withSamples(samples)
}


/**
 * Returns a generator of [LocalTime] between [min] and [max] (inclusive)
 */

fun Generator.Companion.localTimes(
    min: LocalTime = LocalTime.MIN,
    max: LocalTime = LocalTime.MAX
): Generator<LocalTime> {
    require(max >= min) {
        "Max must be equal or after min but min was $min and max was $max"
    }

    val range = min..max

    val samples = mutableListOf(min, max)

    if (LocalTime.NOON in range && LocalTime.NOON !in samples) {
        samples += LocalTime.NOON
    }

    return Generator { random: Random ->
        LocalTime.ofNanoOfDay(random.nextLong(min.toNanoOfDay(), max.toNanoOfDay()))
    }.withSamples(samples)
}
