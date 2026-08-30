package com.sosauce.cinnamon.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.sosauce.cinnamon.core.datastore.rememberAppTheme
import com.sosauce.cinnamon.app.navigation.Nav
import com.sosauce.cinnamon.setup.SetupScreen
import com.sosauce.cinnamon.core.ui.CinnamonTheme
import com.sosauce.cinnamon.core.utils.CuteTheme
import com.sosauce.cinnamon.core.utils.hasBothRoles

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            CinnamonTheme {

                var hasBothRoles by remember { mutableStateOf(hasBothRoles()) }
                if (hasBothRoles) {
                    Nav(
                        intent = intent
                    )
                } else {
                    SetupScreen { hasBothRoles = true }
                }
            }
        }
    }

}

