package dojo.supermarket.model

import org.junit.jupiter.api.Test
import supermarket.model.*

class SupermarketTest {

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
        val totalPrice = teller.checksOutArticlesFrom(cart).totalPrice
        assert(totalPrice == 8.0)
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
        val totalPrice = teller.checksOutArticlesFrom(cart).totalPrice
        assert(totalPrice == 9.0)
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
        val totalPrice = teller.checksOutArticlesFrom(cart).totalPrice
        assert(totalPrice == 4.0)
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
        val totalPrice = teller.checksOutArticlesFrom(cart).totalPrice
        assert(totalPrice == 7.0)
    }

    @Test
    fun `should work with promotions on multiple objects`() {
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
        cart.addItemQuantity(toothpaste, 1.0)
        val totalPrice = teller.checksOutArticlesFrom(cart).totalPrice
        assert(totalPrice == 17.0)
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
        val totalPrice = teller.checksOutArticlesFrom(cart).totalPrice
        assert(totalPrice == 25.0)
    }

}
