package com.smithware.lastdone.data
import android.content.Context
import androidx.room.*

@Database(entities=[Category::class,TrackedItem::class,CompletionRecord::class,Person::class,Pet::class,Vehicle::class,Location::class,ItemPersonCrossRef::class,ItemPetCrossRef::class,ItemVehicleCrossRef::class,ItemLocationCrossRef::class,AttachedPhoto::class,ReminderRule::class,CustomFieldDefinition::class,CustomFieldValue::class,AppSettings::class],version=1,exportSchema=true)
abstract class LastDoneDatabase:RoomDatabase(){abstract fun dao():LastDoneDao
 companion object { @Volatile private var instance:LastDoneDatabase?=null
  fun get(context:Context)=instance?:synchronized(this){instance?:Room.databaseBuilder(context.applicationContext,LastDoneDatabase::class.java,"lastdone.db").build().also{instance=it}}
 }
}
