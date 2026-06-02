package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ZionBgSlate
import com.example.ui.theme.ZionBorderColor
import com.example.ui.theme.ZionErrorRed
import com.example.ui.theme.ZionPrimaryEmerald
import com.example.ui.theme.ZionSecondaryGold
import com.example.ui.theme.ZionSecondaryTeal
import com.example.ui.theme.ZionSurfaceDarker
import com.example.ui.theme.ZionSurfaceSlate
import com.example.ui.theme.ZionTextMuted
import com.example.ui.theme.ZionTextWhite

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ZionMainScreen()
            }
        }
    }
}

@Composable
fun ZionMainScreen() {
    val viewModel: ZionViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    var selectedTab by remember { mutableStateOf(0) }

    // Auto-dismiss notification after 4 seconds
    LaunchedEffect(state.notification) {
        if (state.notification != null) {
            kotlinx.coroutines.delay(4000)
            viewModel.clearNotification()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ZionBgSlate),
        bottomBar = {
            // High fidelity bottom navigation bar using clean, accessible tabs
            Column(
                modifier = Modifier
                    .background(ZionSurfaceDarker)
                    .navigationBarsPadding()
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = ZionSurfaceDarker,
                    contentColor = ZionPrimaryEmerald,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = ZionPrimaryEmerald
                        )
                    },
                    modifier = Modifier.border(
                        BorderStroke(1.dp, ZionBorderColor),
                        RoundedCornerShape(0.dp)
                    )
                ) {
                    val tabs = listOf(
                        "Zion OS" to Icons.Default.ElectricBolt,
                        "Coaching" to Icons.Default.School,
                        "Payments" to Icons.Default.Payments,
                        "Expansion" to Icons.Default.Map
                    )
                    tabs.forEachIndexed { index, pair ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = {
                                selectedTab = index
                                focusManager.clearFocus()
                            },
                            text = {
                                Text(
                                    text = pair.first,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            icon = {
                                Icon(
                                    pair.second,
                                    contentDescription = pair.first,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            selectedContentColor = ZionPrimaryEmerald,
                            unselectedContentColor = ZionTextMuted,
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("nav_tab_${pair.first.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
                .background(ZionBgSlate)
        ) {
            // Background ambient gradients for distinct technological styling
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .drawBehind {
                        val brush = Brush.radialGradient(
                            colors = listOf(Color(0x1F6750A4), Color.Transparent),
                            center = Offset(size.width * 0.8f, 0f),
                            radius = size.width * 0.9f
                        )
                        drawRect(brush)
                    }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Glow Headers representing "Zion AI" System
                HeaderSection(state = state)

                // Quick statistics card reflecting context awareness
                ContextStatsCard(state = state)

                // Render matching content tab with flawless screen transitions
                when (selectedTab) {
                    0 -> ZionOsTab(state = state, viewModel = viewModel)
                    1 -> ZionCoachingTab(state = state, viewModel = viewModel)
                    2 -> ZionPaymentsTab(state = state, viewModel = viewModel)
                    3 -> ZionExpansionTab(state = state, viewModel = viewModel)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Beautiful dynamic system notification banner
            AnimatedVisibility(
                visible = state.notification != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ZionPrimaryEmerald),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Notification",
                                tint = ZionBgSlate,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = state.notification ?: "",
                                color = ZionBgSlate,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        IconButton(
                            onClick = { viewModel.clearNotification() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = ZionBgSlate,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection(state: ZionUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(ZionPrimaryEmerald)
                        .border(1.dp, ZionPrimaryEmerald, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ZION AI",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    color = ZionTextWhite
                )
            }
            Text(
                text = "Africa's AI Operating System",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp,
                color = ZionPrimaryEmerald
            )
        }

        // Active Subscription Indicator
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (state.isPremium) ZionSecondaryGold.copy(alpha = 0.2f) else ZionBorderColor)
                .border(
                    1.dp,
                    if (state.isPremium) ZionSecondaryGold else ZionBorderColor.copy(alpha = 0.5f),
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (state.isPremium) Icons.Default.WorkspacePremium else Icons.Default.Lock,
                    contentDescription = "Subscription Level",
                    tint = if (state.isPremium) ZionSecondaryGold else ZionTextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (state.isPremium) "GOLD PREMIUM" else "FREEMIUM",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.isPremium) ZionSecondaryGold else ZionTextMuted
                )
            }
        }
    }
}

@Composable
fun ContextStatsCard(state: ZionUiState) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ZionSurfaceSlate),
        border = BorderStroke(1.dp, ZionBorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "CONTEXT METRICS (LIVE OS)",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = ZionTextMuted,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Savings Vault Status
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Wallet,
                            contentDescription = "Wallet Balance",
                            tint = ZionSecondaryTeal,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Micro Vault Balance",
                            fontSize = 11.sp,
                            color = ZionTextMuted
                        )
                    }
                    Text(
                        text = "%,.2f ETB".format(state.savingsBalance),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ZionTextWhite
                    )
                }

                // Interactive User Streaks
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocalFireDepartment,
                            contentDescription = "Fire Streak",
                            tint = ZionSecondaryGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Growth Streak",
                            fontSize = 11.sp,
                            color = ZionTextMuted
                        )
                    }
                    Text(
                        text = "${state.streakDays} Days",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ZionSecondaryGold
                    )
                }

                // Score stats
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Reward Points",
                            tint = ZionPrimaryEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "OS Score",
                            fontSize = 11.sp,
                            color = ZionTextMuted
                        )
                    }
                    Text(
                        text = "${state.scorePoints} XP",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ZionPrimaryEmerald
                    )
                }
            }
        }
    }
}

@Composable
fun UserRoleChip(
    role: UserRole,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) ZionPrimaryEmerald else ZionSurfaceDarker)
            .border(
                1.dp,
                if (isSelected) ZionPrimaryEmerald else ZionBorderColor,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("role_chip_${role.name.lowercase()}"),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = role.displayName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) ZionBgSlate else ZionTextWhite
            )
        }
    }
}

// ==========================================
// TAB 1: ZION GENERAL OPERATING SYSTEM INTERFACE
// ==========================================
@Composable
fun ZionOsTab(state: ZionUiState, viewModel: ZionViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Platform Intent Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ZionSurfaceSlate),
            border = BorderStroke(1.dp, ZionBorderColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "ACTIVE OS PERSONALIZATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = ZionPrimaryEmerald,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dynamic advice adaptions based on your demographic workspace. Switch roles below:",
                    fontSize = 13.sp,
                    color = ZionTextMuted
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable/Wrapping role list
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UserRole.values().forEach { role ->
                        Box(modifier = Modifier.weight(1f)) {
                            UserRoleChip(
                                role = role,
                                isSelected = state.activeRole == role,
                                onClick = { viewModel.selectRole(role) }
                            )
                        }
                    }
                }
            }
        }

        // Selected Role Summary
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ZionSurfaceDarker),
            border = BorderStroke(1.dp, ZionBorderColor.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (state.activeRole) {
                        UserRole.STUDENT -> Icons.Default.School
                        UserRole.PROFESSIONAL -> Icons.Default.WorkspacePremium
                        UserRole.ENTREPRENEUR -> Icons.Default.Wallet
                        UserRole.ORGANIZATION -> Icons.Default.Group
                    },
                    contentDescription = state.activeRole.displayName,
                    tint = ZionSecondaryGold,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Role Focus: ${state.activeRole.displayName}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ZionTextWhite
                    )
                    Text(
                        text = state.activeRole.info,
                        fontSize = 12.sp,
                        color = ZionTextMuted
                    )
                }
            }
        }

        // Interactive Target Milestones
        MilestonesSection(state = state, viewModel = viewModel)

        // Integrated Zion Gamified Academy Hub
        ZionAcademySection(state = state, viewModel = viewModel)
    }
}

@Composable
fun MilestonesSection(state: ZionUiState, viewModel: ZionViewModel) {
    var newGoalText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Learning") }
    val categories = listOf("Learning", "Finance", "Productivity", "Business")

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ZionSurfaceSlate),
        border = BorderStroke(1.dp, ZionBorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "CONTEXT-AWARE TARGETS & GOALS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = ZionTextWhite,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Completing targets adds birr to your Micro Vault and grows active system XP points.",
                fontSize = 12.sp,
                color = ZionTextMuted
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Text input to add user goals
            OutlinedTextField(
                value = newGoalText,
                onValueChange = { newGoalText = it },
                label = { Text("Enter a new milestone...") },
                placeholder = { Text("e.g., Master Chapa APIs") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ZionPrimaryEmerald,
                    unfocusedBorderColor = ZionBorderColor,
                    focusedLabelColor = ZionPrimaryEmerald,
                    unfocusedLabelColor = ZionTextMuted,
                    focusedTextColor = ZionTextWhite,
                    unfocusedTextColor = ZionTextWhite
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("new_goal_text_field"),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.addGoal(newGoalText, selectedCategory)
                    newGoalText = ""
                })
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Category Picker row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) ZionSecondaryTeal.copy(alpha = 0.3f) else ZionSurfaceDarker)
                            .border(
                                1.dp,
                                if (isSelected) ZionSecondaryTeal else ZionBorderColor,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) ZionSecondaryTeal else ZionTextMuted
                        )
                    }
                }
                IconButton(
                    onClick = {
                        viewModel.addGoal(newGoalText, selectedCategory)
                        newGoalText = ""
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(ZionPrimaryEmerald)
                        .testTag("add_goal_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Goal", tint = ZionBgSlate)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Task list
            if (state.goals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "No goals",
                            tint = ZionTextMuted,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No milestones locked. Add one above to start tracking!",
                            color = ZionTextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                state.goals.forEach { goal ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ZionSurfaceDarker)
                            .border(1.dp, ZionBorderColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .clickable { viewModel.toggleGoal(goal.id) }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(if (goal.completed) ZionPrimaryEmerald else Color.Transparent)
                                    .border(
                                        2.dp,
                                        if (goal.completed) ZionPrimaryEmerald else ZionTextMuted,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (goal.completed) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = "Completed",
                                        tint = ZionBgSlate,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = goal.text,
                                    fontSize = 13.sp,
                                    color = if (goal.completed) ZionTextMuted else ZionTextWhite,
                                    fontWeight = if (goal.completed) FontWeight.Normal else FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                // Badge Category
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ZionBorderColor)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = goal.category.uppercase(),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ZionTextMuted
                                    )
                                }
                            }
                        }
                        IconButton(
                            onClick = { viewModel.deleteGoal(goal.id) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = ZionErrorRed,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// TAB 2: ZION COHERENT INTERACTIVE COACHING MODULE
// ==========================================
@Composable
fun ZionCoachingTab(state: ZionUiState, viewModel: ZionViewModel) {
    var customQuery by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Active Coach Console Advice Card
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = ZionSurfaceSlate),
            border = BorderStroke(1.dp, ZionBorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.SupportAgent,
                            contentDescription = "Zion Coach",
                            tint = ZionPrimaryEmerald,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ZION INTELLIGENCE FEED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = ZionTextWhite,
                            letterSpacing = 1.sp
                        )
                    }

                    // Refresh Button
                    IconButton(
                        onClick = { viewModel.onRefreshCoach() },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ZionSurfaceDarker)
                    ) {
                        if (state.isAiLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = ZionPrimaryEmerald,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = ZionPrimaryEmerald,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // AI Response block
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ZionSurfaceDarker)
                        .padding(12.dp)
                ) {
                    if (state.isAiLoading && state.aiResponse.isBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = ZionPrimaryEmerald,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Querying Zion core cloud networks...",
                                fontSize = 12.sp,
                                color = ZionTextMuted
                            )
                        }
                    } else {
                        Column {
                            // Indicate if live key or beautiful simulation is used
                            val key = try {
                                com.example.BuildConfig.GEMINI_API_KEY
                            } catch (e: Exception) {
                                ""
                            }
                            val isSimulated = key.isBlank() || key == "MY_GEMINI_API_KEY"

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "LIVE ADVICE FOR THE ${state.activeRole.name}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = ZionSecondaryGold
                                )
                                Text(
                                    text = if (isSimulated) "MOCK ENGINE ACTIVE" else "LIVE GEMINI ACTIVE",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (isSimulated) ZionSecondaryTeal else ZionPrimaryEmerald
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = state.aiResponse,
                                fontSize = 13.sp,
                                color = ZionTextWhite,
                                lineHeight = 18.sp,
                                modifier = Modifier.testTag("ai_coach_advice_text")
                            )
                        }
                    }
                }
            }
        }

        // Interactive Prompt Terminal Room
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ZionSurfaceSlate),
            border = BorderStroke(1.dp, ZionBorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "ZION CHAT & INTERACTION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = ZionTextWhite,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ask questions regarding business, learning systems, savings tips, or technology.",
                    fontSize = 12.sp,
                    color = ZionTextMuted
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Chat Log history
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ZionSurfaceDarker)
                        .border(1.dp, ZionBorderColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.chatHistory.forEach { chat ->
                        val isUser = chat.first == "User"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 8.dp,
                                            topEnd = 8.dp,
                                            bottomStart = if (isUser) 8.dp else 0.dp,
                                            bottomEnd = if (isUser) 0.dp else 8.dp
                                        )
                                    )
                                    .background(if (isUser) ZionPrimaryEmerald else ZionSurfaceSlate)
                                    .padding(8.dp)
                                    .fillMaxWidth(0.85f)
                            ) {
                                Column {
                                    Text(
                                        text = chat.first,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUser) ZionBgSlate else ZionSecondaryGold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = chat.second,
                                        fontSize = 12.sp,
                                        color = if (isUser) ZionBgSlate else ZionTextWhite
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Chat Input Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = customQuery,
                        onValueChange = { customQuery = it },
                        placeholder = { Text("Ask Zion...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ZionTextWhite,
                            unfocusedTextColor = ZionTextWhite,
                            focusedBorderColor = ZionPrimaryEmerald,
                            unfocusedBorderColor = ZionBorderColor,
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            viewModel.onSendChatMessage(customQuery)
                            customQuery = ""
                        })
                    )

                    IconButton(
                        onClick = {
                            viewModel.onSendChatMessage(customQuery)
                            customQuery = ""
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ZionPrimaryEmerald)
                            .testTag("chat_send_button")
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Send",
                            tint = ZionBgSlate,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Integrated AI Skill Gap Analyzer Module
        ZionSkillGapSection(state = state, viewModel = viewModel)
    }
}

// ==========================================
// TAB 3: ZION COMPREHENSIVE PAYMENT PORTAL
// ==========================================
@Composable
fun ZionPaymentsTab(state: ZionUiState, viewModel: ZionViewModel) {
    val currentMethod = state.payment.selectedMethod

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Active Sub Level Status
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ZionSurfaceSlate),
            border = BorderStroke(1.dp, ZionBorderColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "MEMBERSHIP DECLARED PLANS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = ZionSecondaryGold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SubscriptionPlan.values().forEach { plan ->
                        val isSelected = state.activePlan == plan
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) ZionSurfaceDarker else ZionSurfaceSlate
                            ),
                            border = BorderStroke(
                                1.5.dp,
                                if (isSelected) ZionSecondaryGold else ZionBorderColor.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.executePayment(plan) }
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = plan.displayName.split(" ").first(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ZionTextWhite
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = plan.price,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = ZionSecondaryGold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = plan.detail,
                                    fontSize = 8.sp,
                                    color = ZionTextMuted,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 10.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // Expanded Payment Channels Grid
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ZionSurfaceSlate),
            border = BorderStroke(1.dp, ZionBorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "EXPANDED PAYMENT CHANNELS - ALL PHASES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = ZionTextWhite,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select any payment method based on your location to unlock premium packages:",
                    fontSize = 12.sp,
                    color = ZionTextMuted
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Phase 1: Ethiopia Area
                Text(
                    text = "PHASE 1: ETHIOPIA (LAUNCH AREA)",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = ZionPrimaryEmerald
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val localPartners = listOf("Telebirr", "Chapa", "CBEBirr")
                    localPartners.forEach { partner ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (currentMethod == partner) ZionPrimaryEmerald.copy(alpha = 0.2f) else ZionSurfaceDarker)
                                .border(
                                    1.2.dp,
                                    if (currentMethod == partner) ZionPrimaryEmerald else ZionBorderColor,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.selectPaymentMethod(partner) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = partner,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (currentMethod == partner) ZionPrimaryEmerald else ZionTextWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Phase 2: Africa Area
                Text(
                    text = "PHASE 2: FAST REGIONAL AFRICA EXPANSION",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = ZionSecondaryGold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val regionalChannels = listOf("M-Pesa", "Flutterwave")
                    regionalChannels.forEach { partner ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (currentMethod == partner) ZionSecondaryGold.copy(alpha = 0.15f) else ZionSurfaceDarker)
                                .border(
                                    1.2.dp,
                                    if (currentMethod == partner) ZionSecondaryGold else ZionBorderColor.copy(alpha = 0.5f),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.selectPaymentMethod(partner) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = partner,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (currentMethod == partner) ZionSecondaryGold else ZionTextWhite
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Phase 3: Global Area
                Text(
                    text = "PHASE 3: GLOBAL CHANNELS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = ZionSecondaryTeal
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val globalChannels = listOf("Stripe", "PayPal")
                    globalChannels.forEach { partner ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (currentMethod == partner) ZionSecondaryTeal.copy(alpha = 0.15f) else ZionSurfaceDarker)
                                .border(
                                    1.2.dp,
                                    if (currentMethod == partner) ZionSecondaryTeal else ZionBorderColor.copy(alpha = 0.5f),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.selectPaymentMethod(partner) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = partner,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (currentMethod == partner) ZionSecondaryTeal else ZionTextWhite
                            )
                        }
                    }
                }
            }
        }

        // Active Payment Simulator Dialog panel
        PaymentFormSection(state = state, viewModel = viewModel)
    }
}

@Composable
fun PaymentFormSection(state: ZionUiState, viewModel: ZionViewModel) {
    val pm = state.payment

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ZionSurfaceSlate),
        border = BorderStroke(1.dp, ZionBorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "ZION CHECKOUT INTEGRATOR (${pm.selectedMethod.uppercase()})",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = ZionTextWhite,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            when (pm.status) {
                "Idle" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "To test the checkout flow, specify details below. Zion automates integration processes.",
                            fontSize = 12.sp,
                            color = ZionTextMuted
                        )

                        // If Wallet method (Telebirr/M-Pesa/CBEBirr)
                        if (pm.selectedMethod in listOf("Telebirr", "CBEBirr", "M-Pesa")) {
                            OutlinedTextField(
                                value = pm.phoneNumber,
                                onValueChange = { viewModel.updatePaymentPhone(it) },
                                label = { Text("${pm.selectedMethod} Registered Number") },
                                placeholder = { Text("e.g., +251 912 345 678") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ZionPrimaryEmerald,
                                    unfocusedBorderColor = ZionBorderColor,
                                    focusedTextColor = ZionTextWhite,
                                    unfocusedTextColor = ZionTextWhite
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("checkout_phone_field"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )
                        } else {
                            // Credit Card or global dummy
                            Text(
                                text = "Channel maps sandbox verification. No manual credential entry required for Chapa/Stripe mock.",
                                fontSize = 12.sp,
                                color = ZionPrimaryEmerald,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Transfer sum: ${pm.amount} ETB",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ZionTextWhite
                            )

                            Button(
                                onClick = { viewModel.executePayment(SubscriptionPlan.BASIC) },
                                colors = ButtonDefaults.buttonColors(containerColor = ZionPrimaryEmerald),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("submit_payment_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Launch, contentDescription = "Pay", tint = ZionBgSlate, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Authorize", color = ZionBgSlate, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
                "Processing" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = ZionPrimaryEmerald, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Communicating with ${pm.selectedMethod} core nodes...",
                            color = ZionTextWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Awaiting verification push notify...",
                            color = ZionTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
                "Success" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = ZionPrimaryEmerald,
                            modifier = Modifier.size(42.dp)
                        )
                        Text(
                            text = "Transaction Complete!",
                            color = ZionTextWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Receipt ref: ${pm.referenceCode}",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            color = ZionSecondaryGold
                        )
                        Text(
                            text = "Unlocked Professional Plan. Live analytics now enabled.",
                            fontSize = 11.sp,
                            color = ZionTextMuted,
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = { viewModel.resetPayment() },
                            colors = ButtonDefaults.buttonColors(containerColor = ZionBorderColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Complete & Return", color = ZionTextWhite, fontSize = 12.sp)
                        }
                    }
                }
                else -> {
                    Text(text = "Unrecognized signal. Access reset.", color = ZionTextWhite)
                }
            }
        }
    }
}

// ==========================================
// TAB 4: ZION ROADMAP & LONG TERM EXPANSION
// ==========================================
@Composable
fun ZionExpansionTab(state: ZionUiState, viewModel: ZionViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Stage Progress Visualizer
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ZionSurfaceSlate),
            border = BorderStroke(1.dp, ZionBorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "ROADMAP STAGES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = ZionTextWhite,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Zion AI is more than a tool: It is Africa's connected Operating System. Here is our growth trajectory:",
                    fontSize = 12.sp,
                    color = ZionTextMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                val stages = listOf(
                    Triple("STAGE 1: LAUNCH", "Ethiopia regional node (Telebirr, CBEBirr, Chapa integrations core).", "ACTIVE NOW"),
                    Triple("STAGE 2: EXPANSION", "East Africa scale (Kenya, Uganda, Rwanda with M-Pesa channels).", "Q3 2026 PLANS"),
                    Triple("STAGE 3: PAN-AFRICA", "Whole-continent payment frameworks (Flutterwave connections).", "2027 PROJECTIONS"),
                    Triple("STAGE 4: GLOBAL", "International payment activations (Stripe, Paypal) to sell African services.", "2028 FUTURE")
                )

                stages.forEachIndexed { idx, stage ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(if (idx == 0) ZionPrimaryEmerald else ZionBorderColor)
                                    .border(
                                        2.dp,
                                        if (idx == 0) ZionPrimaryEmerald else ZionTextMuted,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (idx == 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(ZionBgSlate)
                                    )
                                }
                            }
                            if (idx < 3) {
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(44.dp)
                                        .background(ZionBorderColor)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stage.first,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (idx == 0) ZionPrimaryEmerald else ZionTextWhite
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (idx == 0) ZionPrimaryEmerald.copy(alpha = 0.2f) else ZionBorderColor)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = stage.third,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (idx == 0) ZionPrimaryEmerald else ZionTextMuted
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stage.second,
                                fontSize = 11.sp,
                                color = ZionTextMuted,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // Long term vision statement
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ZionSurfaceDarker),
            border = BorderStroke(1.dp, ZionPrimaryEmerald.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "LONG-TERM VISION STATEMENT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = ZionSecondaryGold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Zion AI builds economic, professional, and personal empowerment pipelines. By establishing unified micro-saving gateways and deep intelligent recommendations, Zion allows users across Africa to build verifiable business tracks and unlock global markets transparently.",
                    fontSize = 12.sp,
                    color = ZionTextWhite,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

// ===============================================
// ACADEMY & GAMIFIED LEARNING SECTION (TAB 1)
// ===============================================
@Composable
fun ZionAcademySection(state: ZionUiState, viewModel: ZionViewModel) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ZionSurfaceSlate),
        border = BorderStroke(1.dp, ZionBorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.School,
                    contentDescription = "Zion Academy",
                    tint = ZionPrimaryEmerald,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ZION ACADEMY & GAMIFICATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = ZionTextWhite,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Complete interactive micro-learning modules to claim XP, unlock commemorative badges, and climb the sorted community leaderboard.",
                fontSize = 12.sp,
                color = ZionTextMuted
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Gamified metrics panel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ZionSurfaceDarker)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text("TOTAL SCORE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ZionTextMuted)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("${state.scorePoints} XP", fontSize = 16.sp, fontWeight = FontWeight.Black, color = ZionPrimaryEmerald)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(ZionBorderColor.copy(alpha = 0.5f)))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text("ACTIVE STREAK", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ZionTextMuted)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("🔥 ${state.streakDays} Days", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ZionSecondaryGold)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(ZionBorderColor.copy(alpha = 0.5f)))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    val currentRank = state.getLeaderboard().find { it.isCurrentUser }?.rank ?: 3
                    Text("LEADERBOARD", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ZionTextMuted)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Rank #$currentRank", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ZionTextWhite)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Badges Block
            Text(
                text = "COMMEMORATIVE PROGRESS BADGES",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = ZionTextWhite
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.badges.forEach { badge ->
                    val opacity = if (badge.unlocked) 1f else 0.4f
                    val bgColor = if (badge.unlocked) ZionSecondaryTeal.copy(alpha = 0.2f) else ZionSurfaceDarker
                    val borderColor = if (badge.unlocked) ZionPrimaryEmerald else ZionBorderColor.copy(alpha = 0.5f)
                    
                    Box(
                        modifier = Modifier
                            .width(115.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor)
                            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                            .alpha(opacity)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(badge.icon, fontSize = 24.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = badge.title,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ZionTextWhite,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = badge.description,
                                fontSize = 8.sp,
                                color = ZionTextMuted,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                lineHeight = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lessons Block
            Text(
                text = "INTERACTIVE ACADEMY TRACKS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = ZionTextWhite
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.lessons.forEach { lesson ->
                    val isPremiumLocked = lesson.isPremium && !state.isPremium
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ZionSurfaceDarker)
                            .border(1.dp, ZionBorderColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = lesson.courseName.uppercase(),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ZionPrimaryEmerald,
                                    letterSpacing = 0.5.sp
                                )
                                if (lesson.isPremium) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(ZionSecondaryGold.copy(alpha = 0.2f))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text("PREMIUM", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = ZionSecondaryGold)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = lesson.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ZionTextWhite
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Awards +${lesson.points} XP on completion",
                                fontSize = 10.sp,
                                color = ZionTextMuted
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Interaction Actions
                        if (lesson.completed) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(ZionPrimaryEmerald.copy(alpha = 0.2f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.CheckCircle, 
                                        contentDescription = "Completed", 
                                        tint = ZionPrimaryEmerald, 
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("DONE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ZionPrimaryEmerald)
                                }
                            }
                        } else if (isPremiumLocked) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(ZionBorderColor.copy(alpha = 0.5f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Lock, 
                                        contentDescription = "Premium Locked", 
                                        tint = ZionTextMuted, 
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("LOCK", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ZionTextMuted)
                                }
                            }
                        } else {
                            Button(
                                onClick = { viewModel.completeLesson(lesson.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = ZionPrimaryEmerald),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .height(30.dp)
                                    .testTag("complete_lesson_btn_${lesson.id}")
                            ) {
                                Text("COMPLETE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Leaderboard details
            Text(
                text = "ZION LEADERBOARD RANKINGS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = ZionTextWhite
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ZionSurfaceDarker)
                    .padding(8.dp)
            ) {
                state.getLeaderboard().forEach { entrant ->
                    val isYou = entrant.isCurrentUser
                    val rowBg = if (isYou) ZionPrimaryEmerald.copy(alpha = 0.15f) else Color.Transparent
                    val textColor = if (isYou) ZionPrimaryEmerald else ZionTextWhite
                    val textWeight = if (isYou) FontWeight.Black else FontWeight.Normal
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(rowBg)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val badgeIcon = when (entrant.rank) {
                                1 -> "🥇"
                                2 -> "🥈"
                                3 -> "🥉"
                                else -> "🔥"
                            }
                            Text(
                                text = "$badgeIcon #${entrant.rank}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ZionSecondaryGold,
                                modifier = Modifier.width(42.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = entrant.name,
                                fontSize = 12.sp,
                                fontWeight = textWeight,
                                color = textColor
                            )
                        }
                        Text(
                            text = "${entrant.points} XP",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}

// ===============================================
// AI-POWERED SKILL GAP ANALYZER SECTION (TAB 2)
// ===============================================
@Composable
fun ZionSkillGapSection(state: ZionUiState, viewModel: ZionViewModel) {
    var resumeInputText by remember { mutableStateOf(state.skillGapInput) }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ZionSurfaceSlate),
        border = BorderStroke(1.dp, ZionBorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Skill Analysis",
                    tint = ZionPrimaryEmerald,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI SKILL GAP ANALYZER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = ZionTextWhite,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Supply your active programming, fintech, or business capabilities. Zion AI parses your experience profile to identify tech vacancies relative to Africa's emerging ecosystems, recommending tailored study paths.",
                fontSize = 12.sp,
                color = ZionTextMuted
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Multiline resume text block
            OutlinedTextField(
                value = resumeInputText,
                onValueChange = {
                    resumeInputText = it
                    viewModel.updateSkillGapInput(it)
                },
                label = { Text("Paste current skills, tech background, or full resume...") },
                placeholder = { Text("e.g. Martha Kebede, basic bookkeeping, elementary python...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ZionPrimaryEmerald,
                    unfocusedBorderColor = ZionBorderColor,
                    focusedLabelColor = ZionPrimaryEmerald,
                    unfocusedLabelColor = ZionTextMuted,
                    focusedTextColor = ZionTextWhite,
                    unfocusedTextColor = ZionTextWhite
                ),
                minLines = 3,
                maxLines = 6,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("resume_input_field")
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quick sample load link
                TextButton(
                    onClick = {
                        val sample = """
                            Full Name: Daniel Abebe
                            Skills: Basic Python programming, SQL databases, customer service.
                            Experience: Managed local cafe bookkeeping using digital tools.
                        """.trimIndent()
                        resumeInputText = sample
                        viewModel.updateSkillGapInput(sample)
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = ZionPrimaryEmerald),
                    modifier = Modifier.testTag("load_sample_resume_btn")
                ) {
                    Text("Load Sample Resume", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Call Analyzer
                Button(
                    onClick = { viewModel.analyzeSkillGap(resumeInputText) },
                    colors = ButtonDefaults.buttonColors(containerColor = ZionPrimaryEmerald),
                    modifier = Modifier.testTag("run_analysis_btn")
                ) {
                    if (state.isSkillGapLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Analyzing...", fontSize = 12.sp)
                    } else {
                        Text("Analyze Gaps", fontSize = 12.sp)
                    }
                }
            }

            // Render Result
            if (state.skillGapReport.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ZionSurfaceDarker),
                    border = BorderStroke(1.dp, ZionBorderColor.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "ZION AUTOMATED APPRAISAL PROFILE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = ZionSecondaryGold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = state.skillGapReport,
                            fontSize = 12.sp,
                            color = ZionTextWhite,
                            lineHeight = 18.sp
                        )

                        if (state.skillGapRecommendations.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "RECOMMENDED STUDY CHANNELS TO BRIDGE GAPS:",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = ZionPrimaryEmerald
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                state.skillGapRecommendations.forEach { rec ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(ZionPrimaryEmerald.copy(alpha = 0.15f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = rec,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ZionPrimaryEmerald
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
