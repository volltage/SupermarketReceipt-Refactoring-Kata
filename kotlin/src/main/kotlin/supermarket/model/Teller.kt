package supermarket.model


class Teller(private val catalog: SupermarketCatalog) {
    private val offers = HashMap<Product, Offer>()

    fun addSpecialOffer(offerType: SpecialOfferType, product: Product, argument: Double) {
        this.offers[product] = Offer(offerType, product, argument)
    }

    fun checksOutArticlesFrom(theCart: ShoppingCart): Receipt {
        val receipt = Receipt()
        theCart.getItems()
            .forEach { cartItem ->
                val unitPrice = this.catalog.getUnitPrice(cartItem.product)
                receipt.addProduct(cartItem.product, cartItem.quantity, unitPrice)
            }

        receipt.addDiscounts(this.offers)
        return receipt
    }


}
