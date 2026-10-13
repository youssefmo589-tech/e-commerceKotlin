package com.route.ecommerce_c19_mon_thu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.route.ecommerce_c19_mon_thu.ui.theme.EcommerceTheme

private val Navy = Color(0xFF004182)
private val Muted = Color(0xFF7F7F7F)
private val Pale = Color(0xFFF7F8FA)
private val BannerYellow = Color(0xFFFEDA00)
private val categories = listOf(
    Category("Women’s fashion", "👗", Color(0xFFFCE4EC)), Category("Men’s fashion", "👕", Color(0xFFE3F2FD)),
    Category("Laptops", "💻", Color(0xFFE8EAF6)), Category("Baby & Toys", "🧸", Color(0xFFFFF3E0)),
    Category("Beauty", "💄", Color(0xFFF3E5F5)), Category("Headphones", "🎧", Color(0xFFE0F2F1)),
    Category("Skincare", "🧴", Color(0xFFE8F5E9)), Category("Camera", "📷", Color(0xFFFFF8E1))
)
private val products = listOf(
    Product("Wireless Headphones", "EGP 2,499", "4.8", R.drawable.headphone_ad),
    Product("Studio Headphones", "EGP 1,899", "4.6", R.drawable.headphone_category),
    Product("Everyday Headphones", "EGP 1,299", "4.5", R.drawable.headphone_ad)
)
private data class Category(val name: String, val emoji: String, val tint: Color)
private data class Product(val name: String, val price: String, val rating: String, val image: Int)
private enum class Tab { Home, Categories, Favorites, Profile }

class EcommerceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { EcommerceTheme(dynamicColor = false) { StoreApp() } }
    }
}

@Composable
private fun StoreApp() {
    var tab by remember { mutableStateOf(Tab.Home) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    val favorites = remember { mutableStateListOf<String>() }
    Surface(Modifier.fillMaxSize(), color = Color.White) {
        Scaffold(
            containerColor = Color.White,
            bottomBar = { if (selectedProduct == null) BottomNavigation(tab) { tab = it } }
        ) { insets ->
            Box(Modifier.fillMaxSize().padding(insets)) {
                if (selectedProduct != null) {
                    ProductDetailsScreen(selectedProduct!!, onBack = { selectedProduct = null })
                } else when (tab) {
                    Tab.Home -> HomeScreen(
                        favorites,
                        onProduct = { selectedProduct = it },
                        onCategories = { tab = Tab.Categories },
                        onFavorite = { product ->
                            if (favorites.contains(product.name)) favorites.remove(product.name)
                            else favorites.add(product.name)
                        }
                    )
                    Tab.Categories -> CategoriesScreen(onProduct = { selectedProduct = it })
                    Tab.Favorites -> FavoritesScreen(favorites, onProduct = { selectedProduct = it })
                    Tab.Profile -> ProfileScreen()
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(favorites: List<String>, onProduct: (Product) -> Unit, onCategories: () -> Unit, onFavorite: (Product) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(Color.White).padding(bottom = 18.dp)) {
        HomeHeader()
        OfferBanner()
        SectionHeading("Categories", "view all", onCategories)
        CategoryGrid(Modifier.height(258.dp))
        SectionHeading("Home Appliances", "see all", onCategories)
        LazyRow(contentPadding = PaddingValues(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(products.size) { index -> ProductCard(products[index], favorites.contains(products[index].name), { onProduct(products[index]) }, { onFavorite(products[index]) }) }
        }
        SectionHeading("Popular picks", "see all", onCategories)
        LazyRow(contentPadding = PaddingValues(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(products.size) { index -> ProductCard(products[index], favorites.contains(products[index].name), { onProduct(products[index]) }, { onFavorite(products[index]) }) }
        }
    }
}

@Composable
private fun HomeHeader() {
    Column(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Image(painterResource(R.drawable.route_logo), null, Modifier.height(38.dp).width(86.dp), contentScale = ContentScale.Fit)
            Spacer(Modifier.weight(1f))
            Text("🛒", fontSize = 25.sp, modifier = Modifier.clickable { })
        }
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(14.dp)).background(Pale).padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("⌕", color = Muted, fontSize = 25.sp)
            Spacer(Modifier.width(8.dp))
            Text("What are you looking for?", color = Muted, fontSize = 14.sp)
        }
    }
}

@Composable
private fun OfferBanner() {
    Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 5.dp).height(154.dp).clip(RoundedCornerShape(18.dp)).background(BannerYellow).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("UP TO", color = Navy, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("25% OFF", color = Navy, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(9.dp))
            Text("Shop Now  →", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.clip(RoundedCornerShape(18.dp)).background(Navy).clickable { }.padding(horizontal = 14.dp, vertical = 8.dp))
        }
        Image(painterResource(R.drawable.headphone_ad), null, Modifier.fillMaxHeight().weight(1f).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
    }
}

@Composable
private fun SectionHeading(title: String, action: String, onAction: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(start = 18.dp, end = 18.dp, top = 20.dp, bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(title, Modifier.weight(1f), color = Navy, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(action, color = Navy, fontSize = 13.sp, modifier = Modifier.clickable(onClick = onAction))
    }
}

@Composable
private fun CategoryGrid(modifier: Modifier = Modifier) {
    LazyVerticalGrid(columns = GridCells.Fixed(4), modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp), userScrollEnabled = false, verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        items(categories) { CategoryTile(it, Modifier.fillMaxWidth()) }
    }
}

@Composable
private fun CategoryTile(category: Category, modifier: Modifier = Modifier, selected: Boolean = false, onClick: () -> Unit = {}) {
    Column(modifier.clip(RoundedCornerShape(14.dp)).clickable(onClick = onClick).padding(vertical = 5.dp, horizontal = 2.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(58.dp).clip(CircleShape).background(category.tint).then(if (selected) Modifier.border(2.dp, Navy, CircleShape) else Modifier), contentAlignment = Alignment.Center) {
            Text(category.emoji, fontSize = 27.sp)
        }
        Spacer(Modifier.height(6.dp))
        Text(category.name, color = Color(0xFF333333), fontSize = 10.sp, lineHeight = 12.sp, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun ProductCard(product: Product, isFavorite: Boolean, onClick: () -> Unit, onFavorite: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.width(158.dp).clip(RoundedCornerShape(15.dp)).background(Color.White).border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(15.dp)).clickable(onClick = onClick).padding(9.dp)) {
        Box(Modifier.fillMaxWidth().height(112.dp).clip(RoundedCornerShape(11.dp)).background(Pale)) {
            Image(painterResource(product.image), product.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Text(if (isFavorite) "♥" else "♡", color = if (isFavorite) Color.Red else Navy, fontSize = 22.sp, modifier = Modifier.align(Alignment.TopEnd).padding(7.dp).clickable(onClick = onFavorite))
        }
        Spacer(Modifier.height(8.dp))
        Text(product.name, color = Color(0xFF222222), fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text("★ ${product.rating}", color = Color(0xFFFFA000), fontSize = 12.sp)
        Text(product.price, color = Navy, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CategoriesScreen(onProduct: (Product) -> Unit) {
    var selected by remember { mutableIntStateOf(0) }
    Column(Modifier.fillMaxSize().background(Color.White)) {
        Text("Categories", Modifier.padding(horizontal = 18.dp, vertical = 18.dp), color = Navy, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Row(Modifier.fillMaxSize()) {
            LazyColumn(Modifier.width(132.dp).fillMaxHeight().background(Pale), contentPadding = PaddingValues(vertical = 8.dp)) {
                items(categories.size) { index ->
                    val item = categories[index]
                    Row(Modifier.fillMaxWidth().background(if (selected == index) Color.White else Pale).clickable { selected = index }.padding(vertical = 14.dp, horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(item.emoji, fontSize = 18.sp)
                        Spacer(Modifier.width(7.dp))
                        Text(item.name, color = if (selected == index) Navy else Muted, fontSize = 11.sp, fontWeight = if (selected == index) FontWeight.Bold else FontWeight.Normal, lineHeight = 14.sp)
                    }
                }
            }
            Column(Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState()).padding(12.dp)) {
                Text(categories[selected].name, color = Navy, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text("Popular products", color = Muted, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp, bottom = 12.dp))
                products.forEach { ProductCard(it, false, { onProduct(it) }, {}, Modifier.fillMaxWidth()) ; Spacer(Modifier.height(10.dp)) }
            }
        }
    }
}

@Composable
private fun FavoritesScreen(favorites: List<String>, onProduct: (Product) -> Unit) {
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        Text("Favorites", color = Navy, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Spacer(Modifier.height(16.dp))
        val saved = products.filter { favorites.contains(it.name) }
        if (saved.isEmpty()) Text("Your favorite products will appear here.", color = Muted, fontSize = 14.sp)
        else LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) { items(saved.size) { ProductCard(saved[it], true, { onProduct(saved[it]) }, {}) } }
    }
}

@Composable
private fun ProfileScreen() {
    Column(Modifier.fillMaxSize().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(84.dp).clip(CircleShape).background(Pale), contentAlignment = Alignment.Center) { Text("👤", fontSize = 38.sp) }
        Text("Welcome to Route", Modifier.padding(top = 14.dp), color = Navy, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text("Your account", Modifier.padding(top = 5.dp), color = Muted)
        listOf("My orders", "Delivery addresses", "Payment methods", "Help & support").forEach { label ->
            Row(Modifier.fillMaxWidth().padding(top = 18.dp).clip(RoundedCornerShape(12.dp)).background(Pale).padding(16.dp)) { Text(label, Modifier.weight(1f), color = Navy); Text("›", color = Navy) }
        }
    }
}

@Composable
private fun ProductDetailsScreen(product: Product, onBack: () -> Unit) {
    var quantity by remember { mutableIntStateOf(1) }
    var selectedSize by remember { mutableStateOf("M") }
    Column(Modifier.fillMaxSize().background(Color.White)) {
        Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("‹", color = Navy, fontSize = 32.sp, modifier = Modifier.clickable(onClick = onBack))
            Text("Product details", Modifier.padding(start = 14.dp), color = Navy, fontWeight = FontWeight.Bold, fontSize = 19.sp)
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)) {
            Box(Modifier.fillMaxWidth().height(300.dp).clip(RoundedCornerShape(18.dp)).background(Pale)) {
                Image(painterResource(product.image), product.name, Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
                Text("♡", Modifier.align(Alignment.TopEnd).padding(14.dp), color = Navy, fontSize = 26.sp)
            }
            Row(Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.Center) { repeat(4) { index -> Box(Modifier.padding(3.dp).size(if (index == 0) 9.dp else 7.dp).clip(CircleShape).background(if (index == 0) Navy else Color.LightGray)) } }
            Text(product.name, Modifier.padding(top = 16.dp), color = Navy, fontWeight = FontWeight.Bold, fontSize = 21.sp)
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("★ ${product.rating}", color = Color(0xFFFFA000), fontWeight = FontWeight.Bold)
                Text("  (124 reviews)", color = Muted, fontSize = 12.sp)
                Spacer(Modifier.weight(1f))
                Text(product.price, color = Navy, fontWeight = FontWeight.Bold, fontSize = 19.sp)
            }
            Text("Enjoy clear, balanced sound and a comfortable fit for everyday listening. A simple, reliable choice for music, calls, and more.", Modifier.padding(top = 14.dp), color = Muted, fontSize = 14.sp, lineHeight = 21.sp)
            Text("Choose size", Modifier.padding(top = 18.dp, bottom = 9.dp), color = Navy, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) { listOf("S", "M", "L", "XL").forEach { size -> Box(Modifier.size(42.dp).clip(RoundedCornerShape(10.dp)).background(if (selectedSize == size) Navy else Color.White).border(1.dp, if (selectedSize == size) Navy else Color.LightGray, RoundedCornerShape(10.dp)).clickable { selectedSize = size }, contentAlignment = Alignment.Center) { Text(size, color = if (selectedSize == size) Color.White else Navy) } } }
            Row(Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Quantity", Modifier.weight(1f), color = Navy, fontWeight = FontWeight.SemiBold)
                Stepper(quantity, onMinus = { if (quantity > 1) quantity-- }, onPlus = { quantity++ })
            }
        }
        Button(onClick = { }, Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp).height(54.dp), colors = ButtonDefaults.buttonColors(containerColor = Navy), shape = RoundedCornerShape(14.dp)) { Text("Add to Cart", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun Stepper(quantity: Int, onMinus: () -> Unit, onPlus: () -> Unit) {
    Row(Modifier.clip(RoundedCornerShape(12.dp)).background(Pale).padding(horizontal = 5.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("−", Modifier.clickable(onClick = onMinus).padding(horizontal = 12.dp, vertical = 5.dp), color = Navy, fontSize = 20.sp)
        Text(quantity.toString(), Modifier.widthIn(min = 20.dp), color = Navy, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Text("+", Modifier.clickable(onClick = onPlus).padding(horizontal = 12.dp, vertical = 5.dp), color = Navy, fontSize = 20.sp)
    }
}

@Composable
private fun BottomNavigation(selected: Tab, onSelect: (Tab) -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)).background(Navy).navigationBarsPadding().padding(top = 10.dp, bottom = 8.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
        listOf(Triple(Tab.Home, "⌂", "Home"), Triple(Tab.Categories, "▦", "Categories"), Triple(Tab.Favorites, "♡", "Favorites"), Triple(Tab.Profile, "♙", "Profile")).forEach { (tab, icon, label) ->
            Column(Modifier.weight(1f).clickable { onSelect(tab) }.padding(vertical = 2.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(icon, color = if (selected == tab) Color.White else Color(0xFFB8CDE0), fontSize = 23.sp)
                Text(label, color = if (selected == tab) Color.White else Color(0xFFB8CDE0), fontSize = 10.sp, fontWeight = if (selected == tab) FontWeight.Bold else FontWeight.Normal)
            }
        }
    }
}
