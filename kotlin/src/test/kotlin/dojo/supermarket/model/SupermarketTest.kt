package dojo.supermarket.model

import org.junit.jupiter.api.Test
import supermarket.ReceiptPrinter
import supermarket.model.*

class SupermarketTest {
    private val printer = ReceiptPrinter()

    @Test
    fun `should be able to buy things`() {
        val catalog = FakeCatalog()
        val toothbrush = Product("toothbrush", ProductUnit.Each)
        catalog.addProduct(toothbrush, 0.99)
        val apples = Product("apples", ProductUnit.Kilo)
        catalog.addProduct(apples, 1.99)

        val teller = Teller(catalog)
        teller.addSpecialOffer(SpecialOfferType.TenPercentDiscount, toothbrush, 10.0)

        val cart = ShoppingCart()
        cart.addItemQuantity(apples, 2.5)
        cart.addItemQuantity(toothbrush, 2.0)

        val receipt = teller.checksOutArticlesFrom(cart)

        val totalPrice = receipt.totalPrice
        assert(totalPrice != null)
        assert(totalPrice == 1.99 * 2.5 + 0.99 * 0.9 * 2)
        assert(
            printer.printReceipt(receipt) == """
            apples                              4.98
              1.99 * 2.500
            toothbrush                          1.98
              0.99 * 2
            10.0% off(toothbrush)              -0.20

            Total:                              6.76
        """.trimIndent()
        )
    }

    @Test
    fun `should not count anything on empty cart`() {
        val catalog = FakeCatalog()
        val teller = Teller(catalog)

        val cart = ShoppingCart()

        val receipt = teller.checksOutArticlesFrom(cart)
        val totalPrice = receipt.totalPrice
        assert(totalPrice != null)
        assert(totalPrice == 0.0)
        println(printer.printReceipt(receipt))
        assert(
            printer.printReceipt(receipt) == """    

    Total:                              0.00
        """.trimIndent()
        )
    }

    @Test
    fun `should calculate negative price on product`() {
        val catalog = FakeCatalog()
        val toothbrush = Product("toothbrush", ProductUnit.Each)
        catalog.addProduct(toothbrush, -0.99)
        val teller = Teller(catalog)

        val cart = ShoppingCart()
        cart.addItemQuantity(toothbrush, 1.0)
        val totalPrice = teller.checksOutArticlesFrom(cart).totalPrice
        assert(totalPrice != null)
        assert(totalPrice == -0.99)
    }

    @Test
    fun `should be able to calculate correctly special offers no1`() {

        val catalog = FakeCatalog()
        val toothbrush = Product("toothbrush", ProductUnit.Each)
        catalog.addProduct(toothbrush, 1.0)
        val teller = Teller(catalog)

        teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, toothbrush, 6.0)
        val cart = ShoppingCart()

        cart.addItemQuantity(toothbrush, 12.0)
        val receipt = teller.checksOutArticlesFrom(cart)
        val totalPrice = receipt.totalPrice
        assert(totalPrice == 8.0)
        assert(
            printer.printReceipt(receipt) == """
            toothbrush                         12.00
              1.00 * c
            3 for 2(toothbrush)                -4.00

            Total:                              8.00
        """.trimIndent()
        )
    }

    @Test
    fun `should be able to calculate correctly special offers no2`() {
        val catalog = FakeCatalog()
        val toothbrush = Product("toothbrush", ProductUnit.Each)
        catalog.addProduct(toothbrush, 1.0)
        val teller = Teller(catalog)

        teller.addSpecialOffer(SpecialOfferType.FiveForAmount, toothbrush, 3.0)
        val cart = ShoppingCart()

        cart.addItemQuantity(toothbrush, 15.0)
        val receipt = teller.checksOutArticlesFrom(cart)
        val totalPrice = receipt.totalPrice
        assert(totalPrice == 9.0)
        assert(
            printer.printReceipt(receipt) == """
            toothbrush                         15.00
              1.00 * f
            5 for 3.0(toothbrush)              -6.00

            Total:                              9.00
        """.trimIndent()
        )
    }

    @Test
    fun `should be able to calculate correctly special offers no3`() {
        val catalog = FakeCatalog()
        val toothbrush = Product("toothbrush", ProductUnit.Each)
        catalog.addProduct(toothbrush, 1.0)
        val teller = Teller(catalog)

        teller.addSpecialOffer(SpecialOfferType.TwoForAmount, toothbrush, 0.5)
        val cart = ShoppingCart()

        cart.addItemQuantity(toothbrush, 16.0)
        val receipt = teller.checksOutArticlesFrom(cart)
        val totalPrice = receipt.totalPrice
        assert(totalPrice == 4.0)
        assert(
            printer.printReceipt(receipt) == """
            toothbrush                         16.00
              1.00 * 10
            2 for 0.5(toothbrush)             -12.00

            Total:                              4.00
        """.trimIndent()
        )
    }

    @Test
    fun `should be able to calculate correctly special offers no4`() {
        val catalog = FakeCatalog()
        val toothbrush = Product("toothbrush", ProductUnit.Each)
        catalog.addProduct(toothbrush, 1.0)
        val teller = Teller(catalog)

        teller.addSpecialOffer(SpecialOfferType.TenPercentDiscount, toothbrush, 30.0)
        val cart = ShoppingCart()

        cart.addItemQuantity(toothbrush, 10.0)
        val receipt = teller.checksOutArticlesFrom(cart)
        val totalPrice = receipt.totalPrice
        assert(totalPrice == 7.0)
        assert(
            printer.printReceipt(receipt) == """
            toothbrush                         10.00
              1.00 * a
            30.0% off(toothbrush)              -3.00

            Total:                              7.00
        """.trimIndent()
        )
    }

    @Test
    fun `should work with promotions on multiple objects`() {
        val printer = ReceiptPrinter()
        val catalog = FakeCatalog()
        val toothbrush = Product("toothbrush", ProductUnit.Each)
        catalog.addProduct(toothbrush, 1.0)
        val toothpaste = Product("toothpaste", ProductUnit.Each)
        catalog.addProduct(toothpaste, 10.0)
        val teller = Teller(catalog)

        teller.addSpecialOffer(SpecialOfferType.TenPercentDiscount, toothbrush, 30.0)
        teller.addSpecialOffer(SpecialOfferType.TwoForAmount, toothpaste, 15.0)
        val cart = ShoppingCart()

        cart.addItemQuantity(toothbrush, 10.0)
        cart.addItemQuantity(toothpaste, 2.0)
        val receipt = teller.checksOutArticlesFrom(cart)
        val totalPrice = receipt.totalPrice
        assert(totalPrice == 22.0)
        assert(
            printer.printReceipt(receipt) == """
            toothbrush                         10.00
              1.00 * a
            toothpaste                         20.00
              10.00 * 2
            30.0% off(toothbrush)              -3.00
            2 for 15.0(toothpaste)             -5.00

            Total:                             22.00
            """.trimIndent()
        )
    }

    @Test
    fun `should work with multiple promotions on one object`() {
        val catalog = FakeCatalog()
        val toothbrush = Product("toothbrush", ProductUnit.Each)
        catalog.addProduct(toothbrush, 10.0)
        val teller = Teller(catalog)

        teller.addSpecialOffer(SpecialOfferType.TenPercentDiscount, toothbrush, 30.0)
        teller.addSpecialOffer(SpecialOfferType.TwoForAmount, toothbrush, 5.0)
        val cart = ShoppingCart()

        cart.addItemQuantity(toothbrush, 10.0)
        val receipt = teller.checksOutArticlesFrom(cart)
        val totalPrice = receipt.totalPrice
        assert(totalPrice == 25.0)
        assert(
            printer.printReceipt(receipt) == """
            toothbrush                        100.00
              10.00 * a
            2 for 5.0(toothbrush)             -75.00
            
            Total:                             25.00
        """.trimIndent()
        )
    }

    @Test
    fun `should take in account promotions hierarchy`() {
        val catalog = FakeCatalog()
        val toothbrush = Product("toothbrush", ProductUnit.Each)
        catalog.addProduct(toothbrush, 10.0)
        val teller = Teller(catalog)

        teller.addSpecialOffer(SpecialOfferType.TwoForAmount, toothbrush, 5.0)
        teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, toothbrush, 5.0)
        val cart = ShoppingCart()

        cart.addItemQuantity(toothbrush, 9.0)
        val receipt = teller.checksOutArticlesFrom(cart)
        val totalPrice = receipt.totalPrice
        assert(totalPrice == 60.0)
        assert(
            printer.printReceipt(receipt) == """
            toothbrush                         90.00
              10.00 * 9
            3 for 2(toothbrush)               -30.00
            
            Total:                             60.00
        """.trimIndent()
        )
    }
}
