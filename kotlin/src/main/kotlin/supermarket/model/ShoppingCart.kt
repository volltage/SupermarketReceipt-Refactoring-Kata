package supermarket.model

class ShoppingCart {

    private val items = ArrayList<ProductQuantity>()
    internal var productQuantities: MutableMap<Product, Double> = HashMap()


    internal fun getItems(): List<ProductQuantity> {
        return ArrayList(items)
    }

    internal fun addItem(product: Product) {
        this.addItemQuantity(product, 1.0)
    }

    private fun productQuantities(): Map<Product, Double> {
        return productQuantities
    }


    fun addItemQuantity(product: Product, quantity: Double) {
        items.add(ProductQuantity(product, quantity))
        if (productQuantities.containsKey(product)) {
            productQuantities[product] = productQuantities[product]!! + quantity
        } else {
            productQuantities[product] = quantity
        }
    }

    internal fun handleOffers(receipt: Receipt, offers: Map<Product, Offer>, catalog: SupermarketCatalog) {
        productQuantities().keys
            .filter { offers.containsKey(it) }
            .forEach { product ->
                val quantity = productQuantities[product]!!
                val offer = offers[product]!!
                val unitPrice = catalog.getUnitPrice(product)
                var discount: Discount? = null

                if (offer.offerType === SpecialOfferType.TwoForAmount && quantity.toInt() >= 2) {
                    val discountN =
                        offer.offerType.function.invoke(offer, quantity, unitPrice)
                    discount = Discount(product, "2 for " + offer.argument, discountN)
                } else if (offer.offerType === SpecialOfferType.ThreeForTwo && quantity > 2) {
                    val discountAmount =
                        offer.offerType.function.invoke(offer, quantity, unitPrice)
                    discount = Discount(product, "3 for 2", discountAmount)
                } else if (offer.offerType === SpecialOfferType.TenPercentDiscount) {
                    val discountTotal = offer.offerType.function.invoke(offer, quantity, unitPrice)
                    discount = Discount(
                        product,
                        offer.argument.toString() + "% off",
                        discountTotal
                    )
                } else if (offer.offerType === SpecialOfferType.FiveForAmount && quantity.toInt() >= 5) {
                    val discountTotal = offer.offerType.function.invoke(offer, quantity, unitPrice)
                    discount = Discount(product, 5.toString() + " for " + offer.argument, discountTotal)
                }

                if (discount != null)
                    receipt.addDiscount(discount)
            }
    }
}

data class Metod(val condition: SpecialOfferType, val quantityCondition: (Int) -> Boolean)