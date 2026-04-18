package mohalim.billing.we.ui.new_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mohalim.billing.we.R

@Composable
fun LandlineHomeScreen(viewModel: NewMainViewModel, onLogout: () -> Unit) {
    val primaryColor = Color(0xFF4A148C)
    val secondaryColor = Color(0xFF7B1FA2)
    val surfaceColor = Color(0xFFF8F9FA)
    val accentColor = Color(0xFF00BFA5)

    val welcomeName by viewModel.userName.collectAsState("")
    val phoneNumber by viewModel.serviceNumber.collectAsState("")
    val currentPlan by viewModel.currentPlan.collectAsState("")
    val balance by viewModel.balance.collectAsState("0")
    val totalGB by viewModel.totalGB.collectAsState(0f)
    val remainingGB by viewModel.remainingGB.collectAsState(0f)
    
    val percentage = if (totalGB > 0) remainingGB / totalGB else 0f
    val usedGB = totalGB - remainingGB

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {
        // Gradient Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(primaryColor, secondaryColor)
                    ),
                    shape = RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Welcome Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = " $welcomeName",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.landline),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                
                Surface(
                    onClick = onLogout,
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout, 
                        contentDescription = stringResource(R.string.logout), 
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Usage Circular Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.data_usage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { percentage },
                            modifier = Modifier.size(160.dp),
                            strokeWidth = 14.dp,
                            color = accentColor,
                            trackColor = Color(0xFFF0F0F0),
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${(percentage * 100).toInt()}%",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                color = primaryColor
                            )
                            Text(
                                text = stringResource(R.string.remaining),
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        UsageItem(label = stringResource(R.string.used), value = String.format("%.1f GB", usedGB), color = Color.Gray)
                        UsageItem(label = stringResource(R.string.total), value = String.format("%.1f GB", totalGB), color = primaryColor)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Details Section
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    InfoRow(icon = Icons.Default.Phone, label = stringResource(R.string.service_number), value = phoneNumber)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF5F5F5))
                    InfoRow(icon = Icons.AutoMirrored.Filled.Assignment, label = stringResource(R.string.current_plan), value = currentPlan)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF5F5F5))
                    InfoRow(icon = Icons.Default.AccountBalanceWallet, label = stringResource(R.string.current_balance), value = balance)
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
