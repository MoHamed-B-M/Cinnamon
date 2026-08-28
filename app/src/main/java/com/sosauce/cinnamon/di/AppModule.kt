package com.sosauce.cinnamon.di

import android.content.Context
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import androidx.room.Room
import androidx.work.WorkManager
import com.sosauce.cinnamon.data.local.db.datastore.UserPreferences
import com.sosauce.cinnamon.data.local.db.room.contactSettings.ContactSettingsDao
import com.sosauce.cinnamon.data.local.db.room.contactSettings.ContactSettingsDatabase
import com.sosauce.cinnamon.data.local.db.room.conversationSettings.ConversationSettingsDao
import com.sosauce.cinnamon.data.local.db.room.conversationSettings.ConversationSettingsDatabase
import com.sosauce.cinnamon.data.local.db.room.scheduledMessages.ScheduledMessagesDao
import com.sosauce.cinnamon.data.local.db.room.scheduledMessages.ScheduledMessagesDatabase
import com.sosauce.cinnamon.data.repository.ContactsRepository
import com.sosauce.cinnamon.data.repository.ConversationsRepository
import com.sosauce.cinnamon.data.repository.DialerRepository
import com.sosauce.cinnamon.data.repository.MessagesRepository
import com.sosauce.cinnamon.data.repository.SimsRepository
import com.sosauce.cinnamon.data.repository.VoicemailsRepository
import com.sosauce.cinnamon.data.telephony.PhoneNumberNormalizer
import com.sosauce.cinnamon.data.telephony.message.CuteTelephonyManager
import com.sosauce.cinnamon.data.telephony.message.MessageNotificationManager
import com.sosauce.cinnamon.data.telephony.phone.CallManager
import com.sosauce.cinnamon.data.telephony.phone.CallNotificationManager
import com.sosauce.cinnamon.presentation.components.SimsViewModel
import com.sosauce.cinnamon.presentation.screens.archived.ArchivedViewModel
import com.sosauce.cinnamon.presentation.screens.contacts.ContactDetailsViewModel
import com.sosauce.cinnamon.presentation.screens.contacts.ContactsViewModel
import com.sosauce.cinnamon.presentation.screens.contacts.editor.EditContactViewModel
import com.sosauce.cinnamon.presentation.screens.dialer.DialerViewModel
import com.sosauce.cinnamon.presentation.screens.dialer.DialpadViewModel
import com.sosauce.cinnamon.presentation.screens.messages.ConversationDetailsViewModel
import com.sosauce.cinnamon.presentation.screens.messages.ConversationsViewModel
import com.sosauce.cinnamon.presentation.screens.messages.components.bottombar.BottomBarViewModel
import com.sosauce.cinnamon.presentation.screens.phone.CallingViewModel
import com.sosauce.cinnamon.presentation.screens.settings.MigrationViewModel
import com.sosauce.cinnamon.presentation.screens.starter.StartConversationViewModel
import com.sosauce.cinnamon.presentation.screens.voicemail.VoicemailViewModel
import com.sosauce.cinnamon.presentation.screens.wallpaper.ThemingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single<ConversationSettingsDao> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = ConversationSettingsDatabase::class.java,
            name = "conversationSettings.db"
        ).build().dao
    }

    single<ScheduledMessagesDao> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = ScheduledMessagesDatabase::class.java,
            name = "scheduledMessages.db"
        ).build().dao
    }

    single<ContactSettingsDao> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = ContactSettingsDatabase::class.java,
            name = "contactSettings.db"
        ).build().dao
    }

    single { CoroutineScope(SupervisorJob()) }
    single { WorkManager.getInstance(androidContext()) }

    single<TelecomManager> {
        androidContext().getSystemService(Context.TELECOM_SERVICE) as TelecomManager
    }
    single<TelephonyManager> {
        androidContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    single {
        PhoneNumberNormalizer(
            telephonyManager = get()
        )
    }

    singleOf(::UserPreferences)
    singleOf(::CallManager)
    singleOf(::MessageNotificationManager)
    singleOf(::CallNotificationManager)
    singleOf(::CuteTelephonyManager)
    singleOf(::ConversationsRepository)
    singleOf(::SimsRepository)
    singleOf(::ContactsRepository)
    singleOf(::MessagesRepository)
    singleOf(::DialerRepository)
    singleOf(::VoicemailsRepository)


    viewModelOf(::ContactsViewModel)
    viewModelOf(::ContactDetailsViewModel)
    viewModelOf(::ConversationDetailsViewModel)
    viewModelOf(::ThemingViewModel)
    viewModelOf(::ConversationsViewModel)
    viewModelOf(::ArchivedViewModel)
    viewModelOf(::DialerViewModel)
    viewModelOf(::VoicemailViewModel)
    viewModelOf(::DialpadViewModel)
    viewModelOf(::CallingViewModel)
    viewModelOf(::BottomBarViewModel)
    viewModelOf(::SimsViewModel)
    viewModelOf(::MigrationViewModel)
    viewModelOf(::StartConversationViewModel)
    viewModelOf(::EditContactViewModel)
}