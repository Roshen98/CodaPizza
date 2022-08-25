package com.bignerdranch.android.codapizza.model

import android.content.Context
import android.os.Parcelable
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.bignerdranch.android.codapizza.R
import com.bignerdranch.android.codapizza.model.ToppingPlacement.*
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pizza(
    val toppings: Map<Topping, ToppingPlacement> = emptyMap(),
    var pizzaSize: String,
    var pizzaPrice: Double
) : Parcelable {

    val price: Double
        get() =
            pizzaPrice + toppings.asSequence().sumOf { (_, toppingPlacement) ->
                when (toppingPlacement) {
                    Left, Right -> 0.5
                    All -> 1.0
                }
            }


    fun pizzaPrices() {
        pizzaPrice = when(pizzaSize){
            "Small($6.99)" -> 6.99
            "Medium($7.99)" -> 7.99
            "Extra Large($11.99)" -> 11.99
            else -> 9.99
        }
    }


    fun withTopping(topping: Topping, placement: ToppingPlacement?): Pizza {
        return copy(
            toppings = if (placement == null) {
                toppings - topping
            } else {
                toppings + (topping to placement)
            }
        )
    }
}
