package com.bignerdranch.android.codapizza.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bignerdranch.android.codapizza.R
import com.bignerdranch.android.codapizza.model.Pizza
import com.bignerdranch.android.codapizza.model.PizzaSize
import com.bignerdranch.android.codapizza.model.Topping
import java.text.NumberFormat

@Preview
@Composable
fun PizzaBuilderScreen(
    modifier: Modifier = Modifier
) {

    var pizza by rememberSaveable { mutableStateOf(Pizza(emptyMap(),"",0.0)) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        }

    ) { padding ->
        LazyColumn(
            Modifier
                .padding(padding)
                .fillMaxWidth()
        ){}

        Column() {
            ToppingsList(
                pizza = pizza,
                onEditPizza = { pizza = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            )


        }
    }
}

@Composable
private fun ToppingsList(
    pizza: Pizza,
    onEditPizza: (Pizza) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val smallSize = context.getString(PizzaSize.Small.size)
    val mediumSize = context.getString(PizzaSize.Medium.size)
    val defaultSize = context.getString(PizzaSize.Default.size)
    val extraLargeSize = context.getString(PizzaSize.ExtraLarge.size)


    var isExpanded by rememberSaveable() {
        mutableStateOf(false)
    }
    var selection by rememberSaveable() {
        mutableStateOf(defaultSize)
    }

    var toppingBeingAdded by rememberSaveable { mutableStateOf<Topping?>(null) }
    val dropDownIcon =
        if(isExpanded) Icons.Filled.KeyboardArrowDown
        else Icons.Filled.KeyboardArrowRight

    Column(modifier = Modifier
        .padding(1.dp)
        .fillMaxWidth()
    ) {
        Row {
            Text(
                text = stringResource(R.string.size_of_pizza)
            )
            Text(
                text = selection
            )

            Icon(dropDownIcon,
                contentDescription = "A dropdown icon",
                modifier = Modifier.clickable {
                    isExpanded = !isExpanded
                })
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            DropdownMenuItem(onClick = {
                isExpanded = false
                selection = smallSize


            }) {
                Text(text = smallSize)
            }

            DropdownMenuItem(onClick = {
                isExpanded = false
                selection = mediumSize

            }) {
                Text(text = mediumSize)
            }

            DropdownMenuItem(onClick = {
                isExpanded = false
                selection = defaultSize

            }) {

                Text(text = defaultSize)
            }

            DropdownMenuItem(onClick = {
                isExpanded = false
                selection = extraLargeSize

            }) {
                Text(text = extraLargeSize)

            }
        }




    }


    toppingBeingAdded?.let { topping ->
        ToppingPlacementDialog(
            topping = topping,
            onSetToppingPlacement = { placement ->
                onEditPizza(pizza.withTopping(topping, placement))
            },
            onDismissRequest = {
                toppingBeingAdded = null
            }
        )
    }

    LazyColumn(
        modifier = modifier
    ) {
        item {
            PizzaHeroImage(
                pizza = pizza,
                modifier = Modifier.padding(16.dp)
            )
        }

        items(Topping.values()) { topping ->
            ToppingCell(
                topping = topping,
                placement = pizza.toppings[topping],
                onClickTopping = {
                    toppingBeingAdded = topping
                }
            )
        }
    }
    pizza.pizzaSize = selection

    OrderButton(
        pizza = pizza,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )

}

@Composable
private fun OrderButton(
    pizza: Pizza,
    modifier: Modifier = Modifier

) {
    val context = LocalContext.current
    Button(
        modifier = modifier,
        onClick = {
            Toast.makeText(context, R.string.order_placed_toast, Toast.LENGTH_LONG)
                .show()
        }
    ) {
        val currencyFormatter = remember { NumberFormat.getCurrencyInstance() }
        pizza.pizzaPrices()
        val price = currencyFormatter.format(pizza.price)
        Text(
            text = stringResource(R.string.place_order_button, price)
                .toUpperCase(Locale.current)
        )
    }
}
