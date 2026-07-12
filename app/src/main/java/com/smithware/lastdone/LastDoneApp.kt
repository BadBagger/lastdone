package com.smithware.lastdone
import android.app.Application
import com.smithware.lastdone.data.*
class LastDoneApp:Application(){val database by lazy{LastDoneDatabase.get(this)};val repository by lazy{LastDoneRepository(this,database.dao())};val settings by lazy{SettingsStore(this)}}
