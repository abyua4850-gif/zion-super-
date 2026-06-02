package com.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.Content
import com.example.api.GenerateContentRequest
import com.example.api.Part
import com.example.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class UserRole(val displayName: String, val info: String) {
    STUDENT("Student", "Learning, Certifications, Career prep"),
    PROFESSIONAL("Professional", "Productivity, Career growth, Skill development"),
    ENTREPRENEUR("Entrepreneur", "Business management, Financial planning, Corporate design"),
    ORGANIZATION("Organization", "Team management, Employee development, Analytics")
}

enum class SubscriptionPlan(val displayName: String, val price: String, val detail: String) {
    FREE("Basic (Freemium)", "Free", "Essential tracking & standard AI coaching"),
    BASIC("Professional Plan", "$5 / 250 ETB mo", "Advanced AI recommendations & premium guides"),
    ENTERPRISE("Enterprise Tier", "$29 / 1500 ETB mo", "Organizations, analytics, team portals")
}

data class Goal(
    val id: String,
    val text: String,
    val completed: Boolean,
    val category: String
)

data class PaymentState(
    val selectedMethod: String = "Telebirr", // Telebirr, Chapa, CBEBirr, M-Pesa, Flutterwave, Stripe, PayPal
    val phoneNumber: String = "",
    val amount: String = "250",
    val status: String = "Idle", // Idle, Processing, Success, Failed
    val referenceCode: String = ""
)

data class Lesson(
    val id: String,
    val title: String,
    val courseName: String,
    val points: Int,
    val completed: Boolean,
    val isPremium: Boolean = false
)

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val unlocked: Boolean
)

data class LeaderboardEntry(
    val name: String,
    val points: Int,
    val rank: Int,
    val isCurrentUser: Boolean = false
)

data class ZionUiState(
    val activeRole: UserRole = UserRole.STUDENT,
    val activePlan: SubscriptionPlan = SubscriptionPlan.FREE,
    val isPremium: Boolean = false,
    val goals: List<Goal> = listOf(
        Goal("1", "Complete AI Foundations learning modules", false, "Learning"),
        Goal("2", "Save 1,500 Birr to CBE micro-vault", true, "Finance"),
        Goal("3", "Maintain a 5-day productive study streak", false, "Productivity"),
        Goal("4", "Assess export potentials for local craft business", false, "Business")
    ),
    val savingsBalance: Double = 4250.0, // Ethiopian Birr
    val streakDays: Int = 5,
    val scorePoints: Int = 120,
    val isAiLoading: Boolean = false,
    val aiResponse: String = "",
    val payment: PaymentState = PaymentState(),
    val notification: String? = null,
    val chatHistory: List<Pair<String, String>> = listOf(
        "Zion AI" to "Greetings! I am Zion AI, Africa's AI Operating System. Choose your role below and click 'Ask Coach' for personalized productivity, learning, and financial advice."
    ),
    val lessons: List<Lesson> = listOf(
        Lesson("l1", "Python Syntax & Basic Data Structures", "Python Pro", 50, false),
        Lesson("l2", "Control Flows, Functions & Modules", "Python Pro", 50, false),
        Lesson("l3", "Jetpack Compose: Modern Android UIs", "Android UI-UX", 100, false),
        Lesson("l4", "State Management with ViewModels & Flows", "Android UI-UX", 100, false),
        Lesson("l5", "Telebirr Payment Gateway SDK in Kotlin", "Fintech Scale", 80, false),
        Lesson("l6", "Chapa High-Frequency API Webhooks", "Fintech Scale", 80, false),
        Lesson("l7", "Building Custom Vector Database Pipelines", "Advanced AI", 150, false, isPremium = true)
    ),
    val badges: List<Badge> = listOf(
        Badge("b1", "Zion Pioneer", "Welcomed into the Zion OS system.", "🚀", true),
        Badge("b2", "Python Prodigy", "Complete all Python Pro lessons.", "🐍", false),
        Badge("b3", "Fintech Architect", "Complete any Fintech Scale lessons.", "💳", false),
        Badge("b4", "XP Overlord", "Amass more than 300 total reward points.", "🏆", false)
    ),
    val skillGapInput: String = "",
    val skillGapReport: String = "",
    val isSkillGapLoading: Boolean = false,
    val skillGapRecommendations: List<String> = emptyList()
) {
    fun getLeaderboard(): List<LeaderboardEntry> {
        val userPoints = scorePoints
        val all = listOf(
            LeaderboardEntry("You (Current User)", userPoints, 0, isCurrentUser = true),
            LeaderboardEntry("Dagim Yared", 420, 0),
            LeaderboardEntry("Bethelhem Getnet", 310, 0),
            LeaderboardEntry("Yonas Alemayehu", 190, 0),
            LeaderboardEntry("Meron Tesfaye", 120, 0)
        ).sortedByDescending { it.points }
        
        return all.mapIndexed { index, item ->
            item.copy(rank = index + 1)
        }
    }
}

class ZionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ZionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Load initial coaching based on current context
        onRefreshCoach()
    }

    fun selectRole(role: UserRole) {
        _uiState.update { it.copy(activeRole = role) }
        onRefreshCoach()
    }

    fun addGoal(text: String, category: String) {
        if (text.isBlank()) return
        val newGoal = Goal(
            id = System.currentTimeMillis().toString(),
            text = text,
            completed = false,
            category = category
        )
        _uiState.update { state ->
            state.copy(
                goals = state.goals + newGoal,
                scorePoints = state.scorePoints + 15,
                notification = "New target tracked: '${text.take(20)}...'"
            )
        }
    }

    fun toggleGoal(id: String) {
        _uiState.update { state ->
            var pointsDelta = 0
            var savingsDelta = 0.0
            val updated = state.goals.map {
                if (it.id == id) {
                    val nextCompleted = !it.completed
                    pointsDelta = if (nextCompleted) 30 else -30
                    savingsDelta = if (nextCompleted) 120.0 else -120.0
                    it.copy(completed = nextCompleted)
                } else it
            }
            state.copy(
                goals = updated,
                scorePoints = state.scorePoints + pointsDelta,
                savingsBalance = state.savingsBalance + savingsDelta,
                notification = "Goal updated!"
            )
        }
    }

    fun deleteGoal(id: String) {
        _uiState.update { state ->
            state.copy(
                goals = state.goals.filter { it.id != id },
                notification = "Goal removed."
            )
        }
    }

    fun clearNotification() {
        _uiState.update { it.copy(notification = null) }
    }

    fun selectPaymentMethod(method: String) {
        _uiState.update { state ->
            val amt = when (method) {
                "Telebirr", "CBEBirr" -> "250"
                "Chapa" -> "1500"
                "M-Pesa" -> "400"
                "Flutterwave", "Stripe" -> "5"
                else -> "29"
            }
            state.copy(
                payment = state.payment.copy(
                    selectedMethod = method,
                    amount = amt,
                    status = "Idle"
                )
            )
        }
    }

    fun updatePaymentPhone(phone: String) {
        _uiState.update { state ->
            state.copy(payment = state.payment.copy(phoneNumber = phone))
        }
    }

    fun executePayment(plan: SubscriptionPlan) {
        val method = _uiState.value.payment.selectedMethod
        val phone = _uiState.value.payment.phoneNumber

        if ((method == "Telebirr" || method == "CBEBirr" || method == "M-Pesa") && phone.isBlank()) {
            _uiState.update { it.copy(notification = "Please specify a valid mobile wallet number!") }
            return
        }

        _uiState.update { state ->
            state.copy(payment = state.payment.copy(status = "Processing"))
        }

        viewModelScope.launch {
            kotlinx.coroutines.delay(2000) // Beautiful visual simulated networking delay
            val randRef = "ZION-" + (10000..99999).random().toString()
            _uiState.update { state ->
                state.copy(
                    activePlan = plan,
                    isPremium = plan != SubscriptionPlan.FREE,
                    savingsBalance = state.savingsBalance - state.payment.amount.toDoubleOrNull().let { it ?: 0.0 },
                    notification = "Subscription activated! $randRef via $method",
                    payment = state.payment.copy(
                        status = "Success",
                        referenceCode = randRef
                    )
                )
            }
        }
    }

    fun resetPayment() {
        _uiState.update { state ->
            state.copy(payment = PaymentState(selectedMethod = state.payment.selectedMethod))
        }
    }

    fun onSendChatMessage(userText: String) {
        if (userText.isBlank()) return
        _uiState.update { state ->
            state.copy(
                chatHistory = state.chatHistory + ("User" to userText),
                isAiLoading = true
            )
        }

        val role = _uiState.value.activeRole
        val goalsText = _uiState.value.goals.joinToString { "[${it.category}] ${it.text} (Completed: ${it.completed})" }
        val promptContext = """
            User Role: ${role.displayName} (${role.info})
            User Streak: ${_uiState.value.streakDays} days
            Point balance: ${_uiState.value.scorePoints} points
            Active user goals: $goalsText
            User inquiry: $userText
        """.trimIndent()

        callGeminiApi(promptContext, isChat = true, userQuery = userText)
    }

    fun onRefreshCoach() {
        _uiState.update { it.copy(isAiLoading = true) }
        val role = _uiState.value.activeRole
        val goalsText = _uiState.value.goals.joinToString { "[${it.category}] ${it.text} (Completed: ${it.completed})" }
        val promptContext = """
            Give me tailored productivity, career success, and financial advice for a ${role.displayName} in Eastern Africa.
            User Profile Background:
            - Streak: ${_uiState.value.streakDays} days
            - Active Targets: $goalsText
            Provide 3 specific actionable learning points + 1 motivational sentence.
        """.trimIndent()

        callGeminiApi(promptContext, isChat = false)
    }

    private fun callGeminiApi(prompt: String, isChat: Boolean, userQuery: String? = null) {
        viewModelScope.launch {
            val apiKey = try {
                com.example.BuildConfig.GEMINI_API_KEY
            } catch (e: Exception) {
                ""
            }

            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                // Return majestic fallback coaching matching African ecosystem context
                kotlinx.coroutines.delay(1200)
                val mockAdvice = generateMockCoachingResponse(_uiState.value.activeRole, userQuery)
                _uiState.update { state ->
                    val updatedChat = if (isChat && userQuery != null) {
                        state.chatHistory + ("Zion AI" to mockAdvice)
                    } else state.chatHistory
                    state.copy(
                        isAiLoading = false,
                        aiResponse = mockAdvice,
                        chatHistory = updatedChat
                    )
                }
            } else {
                try {
                    val systemInstruction = "You are Zion AI, an advanced Pan-African AI Operating System specialized in helping eastern African students, professionals, and entrepreneurs learn skills, plan finances, build systems, leverage mobile monies (Telebirr, CBEBirr, M-Pesa, Chapa), and optimize daily productivity. Keep your responses highly contextual, supportive, concise (maximum 150 words), and rich in actionable advice."
                    val request = GenerateContentRequest(
                        contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                        systemInstruction = Content(parts = listOf(Part(text = systemInstruction)))
                    )
                    val response = RetrofitClient.service.generateContent(apiKey, request)
                    val outText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                        ?: "No active system signal. Please try again."

                    _uiState.update { state ->
                        val updatedChat = if (isChat && userQuery != null) {
                            state.chatHistory + ("Zion AI" to outText)
                        } else state.chatHistory
                        state.copy(
                            isAiLoading = false,
                            aiResponse = outText,
                            chatHistory = updatedChat
                        )
                    }
                } catch (e: Exception) {
                    val errorAdvice = "Unable to connect to active intelligence stream: ${e.message}. Using offline operating core. Standard tips active for ${_uiState.value.activeRole.displayName} profile."
                    _uiState.update { state ->
                        val updatedChat = if (isChat && userQuery != null) {
                            state.chatHistory + ("Zion AI" to errorAdvice)
                        } else state.chatHistory
                        state.copy(
                            isAiLoading = false,
                            aiResponse = errorAdvice,
                            chatHistory = updatedChat
                        )
                    }
                }
            }
        }
    }

    private fun generateMockCoachingResponse(role: UserRole, query: String?): String {
        if (query != null) {
            // General QA simulation
            val q = query.lowercase()
            return when {
                q.contains("telebirr") || q.contains("payment") || q.contains("chapa") || q.contains("cbe") -> {
                    "Zion AI payment services automatically integrate with Chapa API, Telebirr, and CBEBirr. To configure live keys for Ethiopia operations, navigate to the Zion Payment Hub to generate secure checkouts easily to monetize your micro-learning models."
                }
                q.contains("job") || q.contains("career") || q.contains("work") -> {
                    "Zion AI analyzes skill gaps for the African labor workforce. For a ${role.displayName}, we recommend mastering digital toolsets (like Python, Jetpack Compose, or financial accounting), earning verifiable micro-credentials, and building localized portfolios."
                }
                q.contains("money") || q.contains("save") || q.contains("loan") || q.contains("finance") -> {
                    "Your financial coaching is active. Save daily using micro-wallets. Since your balance of Birr is currently ${_uiState.value.savingsBalance} ETB, consider locking 10% inside the secure CBEBirr vault to safeguard emergency reserves and grow your credit rating."
                }
                else -> {
                    "Interesting inquiry on '$query'. Guided by our Zion AI Knowledge Vault: Keep tracking your milestones daily. For a ${role.displayName}, breaking major roadmaps down to weekly micro-deliverables is the absolute fastest way to accelerate growth in East Africa!"
                }
            }
        }

        // Default role coaching suggestions
        return when (role) {
            UserRole.STUDENT -> """
                1. 📚 **Digital Upskilling**: Prioritize Android Kotlin and Jetpack Compose tracks. Verifiable digital credentials represent 73% higher freelance conversion rates in Ethiopia.
                2. ⏱️ **Focus Mastery**: Work in 25-minute sprints. Clear your active milestone "Complete AI Foundations learning modules" to earn 30 system score points.
                3. 💰 **Micro-Savings**: Set aside 50 Birr weekly from Telebirr into savings. Small deposits generate compound discipline.
                
                *Actionable Guidance: Excellence is built in consecutive steps; your 5-day streak is the perfect engine!*
            """.trimIndent()

            UserRole.PROFESSIONAL -> """
                1. 🚀 **Value Acceleration**: Map monthly contributions directly to business revenues. Professionals who track outcomes see 45% faster promotions.
                2. 🔋 **Time Inversion**: Spend the first 90 minutes of your workday on high-leverage product creation, avoiding email backlogs.
                3. 💸 **Tech Toolkit**: Explore CBEBirr API integrations to streamline client billings and invoice closures automatically.
                
                *Actionable Guidance: Your professionalism shapes the future of Africa's digital economy. Keep pushing boundary lines!*
            """.trimIndent()

            UserRole.ENTREPRENEUR -> """
                1. 📈 **System Monetization**: Integrate Telebirr and Chapa on day one of launching products. Chapa offers streamlined web checkouts in Ethiopia for card/mobile pay.
                2. 👥 **Lean Execution**: Validate consumer demand using digital surveys before building physical inventory.
                3. 🏛️ **Capital Lock-in**: Maintain a 3-month operating cash cushion locally inside sound banking products.
                
                *Actionable Guidance: Build companies that convert structural challenges into localized opportunities. You create the jobs of tomorrow.*
            """.trimIndent()

            UserRole.ORGANIZATION -> """
                1. 📊 **Telemetry over Intuition**: Drive team KPIs through real-time metrics instead of micro-management loops.
                2. 🎓 **Continuous Upskilling**: Reward employees who finish certification pathways (e.g., Python / Cloud architecture) with flexible hours.
                3. 🛡️ **Payment Redundancy**: Set up triple-routing options (Telebirr + M-Pesa + Stripe) to maximize payment collection success ratios.
                
                *Actionable Guidance: Scalable organizations do not rely on superstar individuals; they succeed through robust, predictable systems.*
            """.trimIndent()
        }
    }

    fun completeLesson(lessonId: String) {
        _uiState.update { state ->
            val lesson = state.lessons.find { it.id == lessonId }
            if (lesson == null) return@update state
            if (lesson.isPremium && !state.isPremium) {
                return@update state.copy(
                    notification = "Access Denied: '${lesson.title}' requires Professional Subtier!"
                )
            }
            if (lesson.completed) return@update state
            
            val updatedLessons = state.lessons.map {
                if (it.id == lessonId) it.copy(completed = true) else it
            }
            
            val newPoints = state.scorePoints + lesson.points
            
            // Re-evaluate badge unlocks
            val updatedBadges = state.badges.map { badge ->
                when (badge.id) {
                    "b2" -> {
                        val allPythonCompleted = updatedLessons.filter { it.courseName == "Python Pro" }.all { it.completed }
                        badge.copy(unlocked = badge.unlocked || allPythonCompleted)
                    }
                    "b3" -> {
                        val anyFintechCompleted = updatedLessons.filter { it.courseName == "Fintech Scale" }.any { it.completed }
                        badge.copy(unlocked = badge.unlocked || anyFintechCompleted)
                    }
                    "b4" -> {
                        badge.copy(unlocked = badge.unlocked || newPoints > 300)
                    }
                    else -> badge
                }
            }
            
            state.copy(
                lessons = updatedLessons,
                scorePoints = newPoints,
                badges = updatedBadges,
                notification = "Lesson completed! +${lesson.points} XP added!"
            )
        }
    }

    fun updateSkillGapInput(text: String) {
        _uiState.update { it.copy(skillGapInput = text) }
    }

    fun analyzeSkillGap(userInput: String) {
        if (userInput.isBlank()) {
            _uiState.update { it.copy(notification = "Please specify your skills or paste a resume!") }
            return
        }
        
        _uiState.update { it.copy(isSkillGapLoading = true, skillGapReport = "") }
        
        viewModelScope.launch {
            val apiKey = try {
                com.example.BuildConfig.GEMINI_API_KEY
            } catch (e: Exception) {
                ""
            }
            
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                kotlinx.coroutines.delay(1800) // Simulated AI think-time
                val mockReport = generateMockSkillGapReport(userInput)
                val recommendations = generateMockRecommendations(userInput)
                _uiState.update { state ->
                    state.copy(
                        isSkillGapLoading = false,
                        skillGapInput = userInput,
                        skillGapReport = mockReport,
                        skillGapRecommendations = recommendations,
                        notification = "Skill Gap Analysis completed!"
                    )
                }
            } else {
                try {
                    val systemInstruction = "You are Zion AI Skill Analyst. Analyze current skills or resume profiles to identify critical technology/fintech skill gaps in the African digital marketplace and suggest recommendations. Keep responses structured, professional, and succinct (maximum 220 words)."
                    val promptText = """
                        Analyze this user resume/skills profile: "$userInput"
                        Provide a clear 3-bullet core summary on Gaps and recommend 2-3 specific learning modules.
                    """.trimIndent()
                    val request = GenerateContentRequest(
                        contents = listOf(Content(parts = listOf(Part(text = promptText)))),
                        systemInstruction = Content(parts = listOf(Part(text = systemInstruction)))
                    )
                    val response = RetrofitClient.service.generateContent(apiKey, request)
                    val outText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                        ?: "Analysis completed offline. Target gaps: Fintech, Cloud & modern system architectures."
                    
                    val parsedRecs = generateMockRecommendations(userInput) // Supplement with structured lessons
                    
                    _uiState.update { state ->
                        state.copy(
                            isSkillGapLoading = false,
                            skillGapReport = outText,
                            skillGapRecommendations = parsedRecs,
                            notification = "AI Skill Analysis completed successfully!"
                        )
                    }
                } catch (e: Exception) {
                    val fallbackText = "Offline core analysis: Identified gaps in Android Jetpack UI & API integration pipelines. Recommended actions: Focus on learning state management and transaction security modules."
                    _uiState.update { state ->
                        state.copy(
                            isSkillGapLoading = false,
                            skillGapReport = fallbackText,
                            skillGapRecommendations = listOf("Android UI-UX", "Fintech Scale"),
                            notification = "Analysis completed via offline diagnostic core."
                        )
                    }
                }
            }
        }
    }

    private fun generateMockSkillGapReport(userInput: String): String {
        val ui = userInput.lowercase()
        val field = when {
            ui.contains("python") || ui.contains("code") || ui.contains("program") -> "Software Engineering & Data Science"
            ui.contains("sale") || ui.contains("marketing") || ui.contains("business") || ui.contains("finance") -> "Fintech & Business Development"
            else -> "Mobile Application Development"
        }
        
        return """
            📊 **Zion AI Skill Gap Report** (${field})
            
            • **Current Profile Strengths**: Shows solid baseline foundational awareness, initiative, and localized execution capabilities.
            
            • **Identified Gaps for East Africa**: 
              1. **Modern Frontend Systems**: Lacking declarative UI frameworks (like Jetpack Compose) for fast system iterations.
              2. **Localized API Hubs**: Need familiarity with Chapa checkout webhooks and Telebirr direct integration channels.
              3. **Distributed Logic**: Gaps in managing offloaded AI context models like Gemini API.
              
            • **Action Plan**: Dedicate 45 minutes daily to Zion Academy lessons. Focus heavily on mobile finance integrations and interactive state designs.
        """.trimIndent()
    }

    private fun generateMockRecommendations(userInput: String): List<String> {
        val ui = userInput.lowercase()
        return when {
            ui.contains("python") || ui.contains("code") || ui.contains("program") -> listOf("Python Pro", "Advanced AI")
            ui.contains("sale") || ui.contains("marketing") || ui.contains("business") || ui.contains("finance") -> listOf("Fintech Scale", "Python Pro")
            else -> listOf("Android UI-UX", "Fintech Scale")
        }
    }
}
