package com.route.ecommerce_c19_mon_thu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

private val RouteBlue = Color(0xFF004182)
private val TextGray = Color(0xFF7F7F7F)
private val ScreenGray = Color(0xFFF6F7F9)
private val categoryNames = listOf(
    "Women’s fashion", "men’s fashion", "Laptops & Electronics", "Baby & Toys",
    "Beauty", "Headphones", "Skincare", "Camera"
)

private data class StoreProduct(val name: String, val price: String, val rating: String)

private val applianceProducts = listOf(
    StoreProduct("Wireless Headphones", "EGP 2,499", "4.8"),
    StoreProduct("Bluetooth Headphones", "EGP 1,899", "4.6"),
    StoreProduct("Studio Headphones", "EGP 1,299", "4.5")
)

private enum class StoreTab(val label: String, val icon: String) {
    Home("Home", "⌂"), Categories("Categories", "▦"), Favorites("Favorites", "♡"), Profile("Profile", "♙")
}

class EcommerceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcommerceTheme(dynamicColor = false) {
                StoreApp()
            }
        }
    }
}

@Composable
private fun StoreApp() {
    var selectedTab by remember { mutableStateOf(StoreTab.Home) }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomNavigationBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
        }
    ) { contentPadding ->
        if (selectedTab == StoreTab.Home) {
            HomeScreen(Modifier.padding(contentPadding))
        } else {
            // The assignment requests blank white pages for the other tabs.
            Box(Modifier.fillMaxSize().padding(contentPadding).background(Color.White))
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().background(Color.White).verticalScroll(rememberScrollState())
    ) {
        HomeHeader()
        OfferPager()
        SectionHeader(title = "Categories", action = "view all")
        CategoryRow()
        SectionHeader(title = "Home Appliance", action = "view all")
        HomeApplianceRow()
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun HomeHeader() {
    Column(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.route_logo),
                contentDescription = "Route",
                modifier = Modifier.width(88.dp).height(38.dp),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var searchText by remember { mutableStateOf("") }
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1f).height(56.dp),
                placeholder = { Text("What are you looking for?", color = TextGray, fontSize = 13.sp) },
                leadingIcon = { Text("⌕", color = RouteBlue, fontSize = 22.sp) },
                singleLine = true,
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RouteBlue,
                    unfocusedBorderColor = RouteBlue,
                    cursorColor = RouteBlue
                )
            )
            Spacer(Modifier.width(12.dp))
            Box(
                Modifier.size(48.dp).clip(CircleShape).background(ScreenGray),
                contentAlignment = Alignment.Center
            ) { Text("🛒", fontSize = 22.sp) }
        }
    }
}

@Composable
private fun OfferPager() {
    val pagerState = rememberPagerState(pageCount = { 1 })
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth().height(190.dp),
        contentPadding = PaddingValues(horizontal = 18.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.headphone_ad),
            contentDescription = "Headphones advertisement",
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun SectionHeader(title: String, action: String) {
    Row(
        Modifier.fillMaxWidth().padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, Modifier.weight(1f), color = RouteBlue, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(action, color = RouteBlue, fontSize = 13.sp)
    }
}

@Composable
private fun CategoryRow() {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth().height(188.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(categoryNames) { category -> CategoryItem(category) }
    }
}

@Composable
private fun CategoryItem(name: String) {
    Column(
        modifier = Modifier.width(78.dp).padding(vertical = 3.dp, horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.headphone_category),
            contentDescription = name,
            modifier = Modifier.size(62.dp).clip(CircleShape).background(ScreenGray),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(6.dp))
        Text(
            name, color = Color(0xFF333333), fontSize = 10.sp, lineHeight = 12.sp,
            textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun HomeApplianceRow() {
    LazyRow(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(applianceProducts.size) { index -> ApplianceProductCard(applianceProducts[index]) }
    }
}

/** Static presentation only: deliberately has no click handlers. */
@Composable
private fun ApplianceProductCard(product: StoreProduct) {
    Column(
        Modifier.width(158.dp).clip(RoundedCornerShape(15.dp))
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(15.dp)).padding(9.dp)
    ) {
        Box(Modifier.fillMaxWidth().height(112.dp).clip(RoundedCornerShape(11.dp)).background(ScreenGray)) {
            Image(
                painter = painterResource(R.drawable.headphone_category),
                contentDescription = product.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text("♡", color = RouteBlue, fontSize = 22.sp, modifier = Modifier.align(Alignment.TopEnd).padding(7.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(product.name, color = Color(0xFF222222), fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text("★ ${product.rating}", color = Color(0xFFFFA000), fontSize = 12.sp)
        Text(product.price, color = RouteBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun BottomNavigationBar(selectedTab: StoreTab, onTabSelected: (StoreTab) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
            .background(RouteBlue).navigationBarsPadding().padding(top = 10.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StoreTab.values().forEach { tab ->
            Column(
                modifier = Modifier.weight(1f).clickable { onTabSelected(tab) }.padding(vertical = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(tab.icon, color = if (selectedTab == tab) Color.White else Color(0xFFB8CDE0), fontSize = 23.sp)
                Text(
                    tab.label, color = if (selectedTab == tab) Color.White else Color(0xFFB8CDE0),
                    fontSize = 10.sp, fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

/** Standalone static product details layout. Only the quantity stepper changes state. */
@Composable
fun ProductDetailsScreen() {
    var quantity by remember { mutableStateOf(1) }

    Column(Modifier.fillMaxSize().background(Color.White)) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(18.dp)) {
            Image(
                painter = painterResource(R.drawable.headphone_category),
                contentDescription = "Wireless headphones",
                modifier = Modifier.fillMaxWidth().height(300.dp).clip(RoundedCornerShape(18.dp)).background(ScreenGray),
                contentScale = ContentScale.Fit
            )
            Text("Wireless Headphones", Modifier.padding(top = 18.dp), color = RouteBlue, fontSize = 21.sp, fontWeight = FontWeight.Bold)
            Text("EGP 2,499", Modifier.padding(top = 8.dp), color = RouteBlue, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text("★ 4.8  (124 reviews)", Modifier.padding(top = 8.dp), color = Color(0xFFFFA000), fontSize = 14.sp)
            Text(
                "Enjoy clear, balanced sound and a comfortable fit for everyday listening. Great for music, calls, and more.",
                Modifier.padding(top = 14.dp), color = TextGray, fontSize = 14.sp, lineHeight = 21.sp
            )
            Row(Modifier.fillMaxWidth().padding(top = 22.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Quantity", Modifier.weight(1f), color = RouteBlue, fontWeight = FontWeight.SemiBold)
                QuantityStepper(quantity = quantity, onQuantityChange = { quantity = it })
            }
        }
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp).height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RouteBlue)
        ) {
            Text("Add to Cart", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun QuantityStepper(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(
        Modifier.clip(RoundedCornerShape(12.dp)).background(ScreenGray).padding(horizontal = 5.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("−", Modifier.clickable { if (quantity > 1) onQuantityChange(quantity - 1) }.padding(horizontal = 12.dp, vertical = 5.dp), color = RouteBlue, fontSize = 20.sp)
        Text(quantity.toString(), Modifier.widthIn(min = 20.dp), color = RouteBlue, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Text("+", Modifier.clickable { onQuantityChange(quantity + 1) }.padding(horizontal = 12.dp, vertical = 5.dp), color = RouteBlue, fontSize = 20.sp)
    }
}
