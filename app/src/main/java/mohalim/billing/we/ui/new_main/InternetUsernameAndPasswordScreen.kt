package mohalim.billing.we.ui.new_main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mohalim.billing.we.R

@Composable
fun InternetUsernameAndPasswordScreen(
    viewModel: NewMainViewModel,
    onBack: () -> Unit
) {
    val primaryColor = Color(0xFF4A148C)
    val secondaryColor = Color(0xFF7B1FA2)
    val surfaceColor = Color(0xFFF8F9FA)

    val serviceNumber by viewModel.serviceNumber.collectAsState()
    val username by viewModel.internetUsername.collectAsState()
    val password by viewModel.internetPassword.collectAsState()

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {
        // Gradient Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
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
            Spacer(modifier = Modifier.height(40.dp))

            // Custom Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onBack,
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.router_credentials),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Main Credentials Card
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
                    Icon(
                        Icons.Default.Router,
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.internet_service_settings),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    Text(
                        text = stringResource(R.string.service_number_label, serviceNumber),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    // Username Section
                    CredentialItem(
                        label = stringResource(R.string.pppoe_username),
                        value = username,
                        icon = Icons.Default.AccountCircle,
                        onCopy = {
                            clipboardManager.setText(AnnotatedString(username))
                            Toast.makeText(context, context.getString(R.string.username_copied), Toast.LENGTH_SHORT).show()
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password Section
                    var passwordVisible by remember { mutableStateOf(false) }
                    CredentialItem(
                        label = stringResource(R.string.pppoe_password),
                        value = password,
                        icon = Icons.Default.Lock,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onToggleVisibility = { passwordVisible = !passwordVisible },
                        onCopy = {
                            clipboardManager.setText(AnnotatedString(password))
                            Toast.makeText(context, context.getString(R.string.password_copied), Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Instructions Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFEFEBE9).copy(alpha = 0.5f)),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = secondaryColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.how_to_use),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = secondaryColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.how_to_use_desc),
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun CredentialItem(
    label: String,
    value: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onToggleVisibility: () -> Unit = {},
    onCopy: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF3E5F5).copy(alpha = 0.5f),
            border = null
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color(0xFF4A148C),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                
                val displayText = if (isPassword && !passwordVisible) {
                    "••••••••"
                } else {
                    value
                }
                
                Text(
                    text = displayText,
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                if (isPassword) {
                    IconButton(onClick = onToggleVisibility) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password),
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                IconButton(onClick = onCopy) {
                    Icon(
                        Icons.Default.ContentCopy,
                        contentDescription = stringResource(R.string.copy),
                        tint = Color(0xFF00BFA5),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
