package com.marsof.bertra

enum class WorkoutSetType(val value: Int) {
    /**
     * Warm-up set.
     */
    WARM_UP(0),

    /**
     * Working set.
     */
    WORKING(1),

    /**
     * Unspecified type.
     */
    UNSPECIFIED(-1)
}