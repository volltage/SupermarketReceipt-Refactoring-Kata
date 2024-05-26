package supermarket.model

import java.util.HashMap


class Teller(private val catalog: SupermarketCatalog) {
    private val offers = HashMap<Product, Offer>()

    fun addSpecialOffer(offerType: SpecialOfferType, product: Product, argument: Double) {
        this.offers[product] = Offer(offerType, product, argument)
    }

    fun checksOutArticlesFrom(theCart: ShoppingCart): Receipt {
        val receipt = Receipt()
        val cartItems = theCart.getItems()
        for (cartItem in cartItems) {
            val product = cartItem.product
            val quantity = cartItem.quantity
            val unitPrice = this.catalog.getUnitPrice(product)
            val price = quantity * unitPrice
            receipt.addProduct(product, quantity, unitPrice, price)
        }
        theCart.handleOffers(receipt, this.offers, this.catalog)

        return receipt
    }

}
