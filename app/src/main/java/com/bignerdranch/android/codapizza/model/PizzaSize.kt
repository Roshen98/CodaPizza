package com.bignerdranch.android.codapizza.model

import androidx.annotation.StringRes
import com.bignerdranch.android.codapizza.R

enum class PizzaSize(
    @StringRes val size: Int
) {
    Small(R.string.small_size),
    Medium(R.string.medium_size),
    Default(R.string.default_size),
    ExtraLarge(R.string.extra_large_size)
}