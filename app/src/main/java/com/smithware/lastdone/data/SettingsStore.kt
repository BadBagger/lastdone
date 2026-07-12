package com.smithware.lastdone.data
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
private val Context.dataStore by preferencesDataStore("lastdone_settings")
enum class CompletionMode { CONFIRM, IMMEDIATE, IMMEDIATE_UNDO }
class SettingsStore(private val context:Context){
 private val mode=stringPreferencesKey("completion_mode"); private val onboarding=booleanPreferencesKey("onboarding_done"); private val dark=booleanPreferencesKey("dark_mode")
 val values=context.dataStore.data.map{Settings(it[mode]?.let(CompletionMode::valueOf)?:CompletionMode.CONFIRM,it[onboarding]?:false,it[dark]?:false)}
 suspend fun setMode(v:CompletionMode)=context.dataStore.edit{it[mode]=v.name}; suspend fun finishOnboarding()=context.dataStore.edit{it[onboarding]=true}; suspend fun setDark(v:Boolean)=context.dataStore.edit{it[dark]=v}
}
data class Settings(val completionMode:CompletionMode,val onboardingDone:Boolean,val darkMode:Boolean)
