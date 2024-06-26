package supermarket.model

class Receipt {
    private val items = ArrayList<ReceiptItem>()
    private val discounts = ArrayList<Discount>()

    val totalPrice: Double
        get() {
            var total = 0.0
            for (item in this.items) {
                total += item.totalPrice
            }
            for (discount in this.discounts) {
                total -= discount.discountAmount
            }
            return total
        }

    fun addProduct(p: Product, quantity: Double, price: Double) {
        val element = ReceiptItem(p, quantity, price)
        this.items.add(element)
    }

    fun getItems(): List<ReceiptItem> {
        return ArrayList(this.items)
    }

    fun addDiscounts(offers2: Map<Product, Offer>) {
        val discounts2 = getDiscounts(offers2)
        this.discounts.addAll(discounts2)
    }

    private fun getDiscounts(offers: Map<Product, Offer>): List<Discount> {
        return items.groupBy { it.product }
            .values
            .map { receiptItems: List<ReceiptItem> ->
                receiptItems.reduce { acc, receiptItem ->
                    ReceiptItem(
                        acc.product,
                        receiptItem.quantity + acc.quantity,
                        acc.price
                    )
                }
            }
            .filter { item -> offers.containsKey(item.product) }
            .map {
                val offer = offers[it.product]!!
                val offerType = offer.offerType
                return@map if (offerType.conditionToStart.invoke(it.quantity)) {
                    offerType.calculateAmount.invoke(it.product, offer, it.quantity, it.price)
                } else {
                    null
                }
            }.filterNotNull()
    }


    fun getDiscounts(): List<Discount> {
        return discounts
    }

}
