package supermarket.model

enum class SpecialOfferType(
    val calculateAmount: (Product, Offer, Double, Double) -> Discount,
    val conditionToStart: (Double) -> Boolean
) {
    ThreeForTwo(
        calculateAmount = { product, _, quantity, unitPrice ->
            val discountAmount =
                quantity.toInt() * unitPrice - ((quantity.toInt() / 3).toDouble() * 2.0 * unitPrice + quantity.toInt() % 3 * unitPrice)
            Discount(product, "3 for 2", discountAmount)
        },
        conditionToStart = { quantity -> quantity > 2 }
    ),
    TenPercentDiscount(
        calculateAmount = { product, offer, quantity, unitPrice ->
            val discountAmount = quantity * unitPrice * offer.argument / 100.0
            Discount(product, offer.argument.toString() + "% off", discountAmount)
        },
        conditionToStart = { _ -> true }
    ),
    TwoForAmount(
        calculateAmount = { product, offer, quantity, unitPrice ->
            val discountAmount =
                unitPrice * quantity - (offer.argument * (quantity.toInt() / 2) + quantity.toInt() % 2 * unitPrice)
            Discount(product, "2 for " + offer.argument, discountAmount)
        },
        conditionToStart = { quantity -> quantity.toInt() >= 2 }
    ),
    FiveForAmount(
        calculateAmount = { product, offer, quantity, unitPrice ->
            val discountAmount =
                unitPrice * quantity - (offer.argument * (quantity.toInt() / 5) + quantity.toInt() % 5 * unitPrice)
            Discount(product, 5.toString() + " for " + offer.argument, discountAmount)
        },
        conditionToStart = { quantity -> quantity.toInt() >= 5 }
    );
}
