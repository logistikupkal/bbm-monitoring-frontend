package com.plnnusadaya.bbmmonitoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BBMMonitoringApp()
        }
    }
}


/*
 * ============================================================
 * MAIN APP
 * ============================================================
 */

@Composable
fun BBMMonitoringApp() {

    var isLoggedIn by remember {
        mutableStateOf(false)
    }

    MaterialTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {

            if (isLoggedIn) {

                DashboardScreen()

            } else {

                LoginScreen(
                    onLoginSuccess = {
                        isLoggedIn = true
                    }
                )
            }
        }
    }
}


/*
 * ============================================================
 * LOGIN SCREEN
 * ============================================================
 */

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var rememberMe by remember {
        mutableStateOf(false)
    }

    var loginError by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val blueDark = Color(0xFF004A87)
    val bluePrimary = Color(0xFF087AC1)
    val blueLight = Color(0xFFEAF5FF)
    val textGray = Color(0xFF60758A)

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(blueLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            // ====================================================
            // HEADER & WELCOME WORDS SECTION
            // ====================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 28.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.danantara),
                        contentDescription = "Danantara Indonesia",
                        modifier = Modifier
                            .width(115.dp)
                            .height(45.dp),
                        contentScale = ContentScale.Fit
                    )

                    Image(
                        painter = painterResource(id = R.drawable.pln_nusa_daya),
                        contentDescription = "PLN Nusa Daya",
                        modifier = Modifier
                            .width(115.dp)
                            .height(45.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Selamat Datang di",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = blueDark
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "BBM Monitoring",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = blueDark
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Penerimaan BBM yang Akurat\ndemi Operasional yang Andal",
                    fontSize = 14.sp,
                    color = textGray,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(36.dp))
            }

            // ====================================================
            // LOGIN WHITE CARD/SHEET (BOTTOM CONTAINER)
            // ====================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .padding(24.dp)
            ) {

                Text(
                    text = "Login",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = blueDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Masuk untuk mengakses sistem Monitoring Penerimaan BBM",
                    fontSize = 13.sp,
                    color = textGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                /*
                 * USERNAME FIELD
                 */
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        loginError = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Username", color = Color(0xFF9EAEBD))
                    },
                    leadingIcon = {
                        Text("👤", fontSize = 16.sp, color = textGray)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = bluePrimary,
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = blueDark,
                        unfocusedTextColor = blueDark
                    ),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                /*
                 * PASSWORD FIELD
                 */
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        loginError = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Password", color = Color(0xFF9EAEBD))
                    },
                    leadingIcon = {
                        Text("🔒", fontSize = 16.sp, color = textGray)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(
                            onClick = { passwordVisible = !passwordVisible },
                            enabled = !isLoading
                        ) {
                            Text(
                                text = if (passwordVisible) "🙈" else "👁",
                                fontSize = 18.sp,
                                color = textGray
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = bluePrimary,
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = blueDark,
                        unfocusedTextColor = blueDark
                    ),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(12.dp))

                /*
                 * REMEMBER ME & FORGOT PASSWORD
                 */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            enabled = !isLoading,
                            colors = CheckboxDefaults.colors(checkedColor = bluePrimary)
                        )

                        Text(
                            text = "Ingat saya",
                            fontSize = 13.sp,
                            color = Color(0xFF526577)
                        )
                    }

                    TextButton(
                        onClick = { /* Fitur lupa password */ },
                        enabled = !isLoading
                    ) {
                        Text(
                            text = "Lupa password?",
                            fontSize = 13.sp,
                            color = bluePrimary,
                            style = TextStyle(textDecoration = TextDecoration.Underline)
                        )
                    }
                }

                if (loginError.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = loginError,
                        fontSize = 12.sp,
                        color = Color(0xFFD32F2F),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                /*
                 * LOGIN BUTTON
                 */
                Button(
                    onClick = {
                        if (username.isBlank() || password.isBlank()) {
                            loginError = "Username/email dan password wajib diisi."
                            return@Button
                        }
                        coroutineScope.launch {
                            isLoading = true
                            loginError = ""
                            val result = BackendApi.login(email = username.trim(), password = password)
                            if (result.success && !result.userId.isNullOrBlank() && !result.email.isNullOrBlank() && !result.accessToken.isNullOrBlank()) {
                                SessionManager.saveSession(
                                    context = context,
                                    userId = result.userId,
                                    email = result.email,
                                    accessToken = result.accessToken,
                                    refreshToken = result.refreshToken
                                )
                                isLoading = false
                                onLoginSuccess()
                            } else {
                                isLoading = false
                                loginError = result.error ?: "Login gagal. Silakan coba lagi."
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = bluePrimary),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Menghubungkan...", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "Login", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "→", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                /*
                 * DIVIDER
                 */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFE2E8F0)))
                    Text(
                        text = "atau login dengan",
                        fontSize = 12.sp,
                        color = textGray,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFE2E8F0)))
                }

                Spacer(modifier = Modifier.height(20.dp))

                /*
                 * ALTERNATIVE LOGIN
                 */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* Google Login */ },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Text(text = "G", fontWeight = FontWeight.Bold, color = Color(0xFFEA4335), modifier = Modifier.padding(end = 8.dp))
                        Text(text = "Google", fontSize = 14.sp, color = Color(0xFF334155), fontWeight = FontWeight.Medium)
                    }

                    OutlinedButton(
                        onClick = { /* Scan QR */ },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Text(text = "㗊", color = bluePrimary, modifier = Modifier.padding(end = 8.dp))
                        Text(text = "Scan QR", fontSize = 14.sp, color = Color(0xFF334155), fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "v1.0.0", fontSize = 12.sp, color = Color(0xFF94A3B8))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    BBMMonitoringApp()
}
