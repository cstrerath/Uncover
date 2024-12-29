// ui/base/BaseActivity.kt
package com.github.cstrerath.uncover.ui.base

import androidx.activity.ComponentActivity
import com.github.cstrerath.uncover.domain.auth.LoginManager
import com.github.cstrerath.uncover.domain.quest.mainquest.QuestManager
import com.github.cstrerath.uncover.domain.quest.randquest.RandQuestManager
import com.github.cstrerath.uncover.utils.navigation.NavigationManager

abstract class BaseActivity : ComponentActivity() {
    protected val navigationManager by lazy { NavigationManager(this) }
    protected val loginManager by lazy { LoginManager(applicationContext) }
    protected val questManager by lazy { QuestManager(applicationContext) }
    protected val randQuestManager by lazy { RandQuestManager(applicationContext) }
}
