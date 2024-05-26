package supermarket.model

enum class SpecialOfferType(val function: (Offer, Double, Double) -> Double) {
    ThreeForTwo(
        function = { offer, quantity, unitPrice ->
            quantity.toInt() * unitPrice - ((quantity.toInt() / 3).toDouble() * 2.0 * unitPrice + quantity.toInt() % 3 * unitPrice)
        }
    ),
    TenPercentDiscount(
        function = { offer, quantity, unitPrice -> quantity * unitPrice * offer.argument / 100.0 }
    ),
    TwoForAmount(
        function = { offer, quantity, unitPrice ->
            unitPrice * quantity - (offer.argument * (quantity.toInt() / 2) + quantity.toInt() % 2 * unitPrice)
        }
    ),
    FiveForAmount(
        function = { offer, quantity, unitPrice ->
            unitPrice * quantity - (offer.argument * (quantity.toInt() / 5) + quantity.toInt() % 5 * unitPrice)
        }
    )
}
