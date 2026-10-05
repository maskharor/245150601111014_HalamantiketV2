package com.example.halamantiket_v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun TicketBookingScreen() {
    val ticketPrice = 17000
    var ticketCount by remember { mutableStateOf(1) }
    var buyerName by remember { mutableStateOf("") }

    var isProcessing by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("Silakan pesan tiket") }
    var statusType by remember { mutableStateOf("DEFAULT") }

    var triggerProcess by remember { mutableStateOf(false) }

    LaunchedEffect(triggerProcess) {
        if (triggerProcess) {
            isProcessing = true
            statusType = "PROCESSING"
            statusMessage = "Memproses pesanan..."

            delay(2000)

            statusType = "SUCCESS"
            statusMessage = "Tiket berhasil dipesan!"
            isProcessing = false
            triggerProcess = false
        }
    }

    val onOrderClick: () -> Unit = {
        if (buyerName.trim().isEmpty()) {
            statusType = "ERROR"
            statusMessage = "Nama harus diisi"
        } else {
            triggerProcess = true
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        TicketBookingContent(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            buyerName = buyerName,
            onNameChange = { buyerName = it },
            ticketCount = ticketCount,
            onIncreaseCount = { ticketCount++ },
            onDecreaseCount = { if (ticketCount > 1) ticketCount-- },
            isProcessing = isProcessing,
            statusType = statusType,
            statusMessage = statusMessage,
            onOrderClick = onOrderClick
        )
    }
}

@Composable
fun TicketBookingContent(
    modifier: Modifier = Modifier,
    buyerName: String,
    onNameChange: (String) -> Unit,
    ticketCount: Int,
    onIncreaseCount: () -> Unit,
    onDecreaseCount: () -> Unit,
    isProcessing: Boolean,
    statusType: String,
    statusMessage: String,
    onOrderClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF2F4F7))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF253C6D))
                .statusBarsPadding()
                .padding(vertical = 36.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.ConfirmationNumber,
                    contentDescription = "Logo Tiket",
                    tint = Color.White,
                    modifier = Modifier.size(35.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Travel PTI",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Book your ticket Now!",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Input Nama
            Column {
                Text(
                    text = "Nama",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = buyerName,
                    onValueChange = onNameChange,
                    placeholder = { Text("Masukkan nama Anda", color = Color.Gray) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    enabled = !isProcessing,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column {
                Text(
                    text = "Jumlah Tiket",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDecreaseCount,
                        enabled = !isProcessing,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8EEF9)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("-", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = "$ticketCount",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Button(
                        onClick = onIncreaseCount,
                        enabled = !isProcessing,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8EEF9)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("+", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onOrderClick,
                enabled = !isProcessing,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF253C6D),
                    disabledContainerColor = Color(0xFFB0BEC5)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Pesan Tiket",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val (bgColor, textColor) = when (statusType) {
                "PROCESSING" -> Pair(Color(0xFFEBF3FC), Color(0xFF1976D2))
                "SUCCESS" -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
                "ERROR" -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
                else -> Pair(Color(0xFFF5F6F8), Color(0xFF616161))
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (statusType) {
                        "PROCESSING" -> CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = textColor,
                            strokeWidth = 2.dp
                        )
                        "SUCCESS" -> Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = textColor,
                            modifier = Modifier.size(22.dp)
                        )
                        "ERROR" -> Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = textColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    if (statusType != "DEFAULT") {
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF212121))) {
                                append("Status: ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = if (statusType == "DEFAULT") FontWeight.Normal else FontWeight.Medium,
                                    color = textColor
                                )
                            ) {
                                append(statusMessage)
                            }
                        },
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}