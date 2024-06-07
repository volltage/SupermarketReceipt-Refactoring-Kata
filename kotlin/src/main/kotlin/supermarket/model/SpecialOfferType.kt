package supermarket.model

enum class SpecialOfferType(
    val calculateAmount: (Product, Offer, Double, Double) -> Discount
) {
    ThreeForTwo(
        calculateAmount = { product, offer, quantity, unitPrice ->
            val discountAmount =
                quantity.toInt() * unitPrice - ((quantity.toInt() / 3).toDouble() * 2.0 * unitPrice + quantity.toInt() % 3 * unitPrice)
            Discount(product, "3 for 2", discountAmount)
        }
    ),
    TenPercentDiscount(
        calculateAmount = { product, offer, quantity, unitPrice ->
            val discountAmount = quantity * unitPrice * offer.argument / 100.0
            Discount(
                product,
                offer.argument.toString() + "% off",
                discountAmount
            )
        }
    ),
    TwoForAmount(
        calculateAmount = { product, offer, quantity, unitPrice ->
            val discountAmount =
                unitPrice * quantity - (offer.argument * (quantity.toInt() / 2) + quantity.toInt() % 2 * unitPrice)
            Discount(product, "2 for " + offer.argument, discountAmount)
        }
    ),
    FiveForAmount(
        calculateAmount = { product, offer, quantity, unitPrice ->
            val discountAmount =
                unitPrice * quantity - (offer.argument * (quantity.toInt() / 5) + quantity.toInt() % 5 * unitPrice)
            Discount(product, 5.toString() + " for " + offer.argument, discountAmount)
        }
    );

}


object DiscountFactory {
    fun getDiscoutns(offer: Offer, quantity: Double, unitPrice: Double, product: Product): Discount? {
        return if (offer.offerType === SpecialOfferType.TwoForAmount && quantity.toInt() >= 2) {
            offer.offerType.calculateAmount.invoke(product, offer, quantity, unitPrice)
        } else if (offer.offerType === SpecialOfferType.ThreeForTwo && quantity > 2) {
            offer.offerType.calculateAmount.invoke(product, offer, quantity, unitPrice)
        } else if (offer.offerType === SpecialOfferType.TenPercentDiscount) {
            return offer.offerType.calculateAmount.invoke(product, offer, quantity, unitPrice)
        } else if (offer.offerType === SpecialOfferType.FiveForAmount && quantity.toInt() >= 5) {
            offer.offerType.calculateAmount.invoke(product, offer, quantity, unitPrice)
        } else {
            null

        }
    }
}