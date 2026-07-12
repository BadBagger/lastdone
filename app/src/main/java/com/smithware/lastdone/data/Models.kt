package com.smithware.lastdone.data

import androidx.room.*

@Entity(tableName="categories", indices=[Index("name", unique=true)])
data class Category(@PrimaryKey(autoGenerate=true) val id:Long=0, val name:String, val icon:String="●", val color:Long=0xFF637B52, val builtIn:Boolean=false, val hidden:Boolean=false)

@Entity(tableName="items", foreignKeys=[ForeignKey(entity=Category::class,parentColumns=["id"],childColumns=["categoryId"],onDelete=ForeignKey.RESTRICT)], indices=[Index("categoryId"),Index("archived")])
data class TrackedItem(@PrimaryKey(autoGenerate=true) val id:Long=0, val name:String, val description:String="", val categoryId:Long, val icon:String="✓", val photoUri:String?=null, val expectedDays:Int?=null, val reminderEnabled:Boolean=false, val notes:String="", val location:String="", val associatedPerson:String="", val associatedVehicle:String="", val associatedPet:String="", val estimatedCostCents:Long?=null, val archived:Boolean=false, val createdAt:Long=System.currentTimeMillis())

@Entity(tableName="completions", foreignKeys=[ForeignKey(entity=TrackedItem::class,parentColumns=["id"],childColumns=["itemId"],onDelete=ForeignKey.RESTRICT)], indices=[Index("itemId"),Index("completedAt")])
data class CompletionRecord(@PrimaryKey(autoGenerate=true) val id:Long=0, val itemId:Long, val completedAt:Long, val note:String="", val photoUri:String?=null, val costCents:Long?=null, val measurement:String="", val location:String="", val person:String="", val serviceProvider:String="", val customDetails:String="")

@Entity(tableName="people") data class Person(@PrimaryKey(autoGenerate=true) val id:Long=0,val name:String,val relationship:String="",val notes:String="",val photoUri:String?=null)
@Entity(tableName="pets") data class Pet(@PrimaryKey(autoGenerate=true) val id:Long=0,val name:String,val animalType:String="",val notes:String="",val photoUri:String?=null)
@Entity(tableName="vehicles") data class Vehicle(@PrimaryKey(autoGenerate=true) val id:Long=0,val name:String,val make:String="",val model:String="",val year:Int?=null,val mileage:Long?=null,val notes:String="",val photoUri:String?=null)
@Entity(tableName="locations") data class Location(@PrimaryKey(autoGenerate=true) val id:Long=0,val name:String,val address:String="",val notes:String="",val photoUri:String?=null)
@Entity(primaryKeys=["itemId","personId"]) data class ItemPersonCrossRef(val itemId:Long,val personId:Long)
@Entity(primaryKeys=["itemId","petId"]) data class ItemPetCrossRef(val itemId:Long,val petId:Long)
@Entity(primaryKeys=["itemId","vehicleId"]) data class ItemVehicleCrossRef(val itemId:Long,val vehicleId:Long)
@Entity(primaryKeys=["itemId","locationId"]) data class ItemLocationCrossRef(val itemId:Long,val locationId:Long)
@Entity(tableName="photos",indices=[Index("itemId"),Index("completionId")]) data class AttachedPhoto(@PrimaryKey(autoGenerate=true) val id:Long=0,val itemId:Long?=null,val completionId:Long?=null,val uri:String,val caption:String="")
@Entity(tableName="reminder_rules",foreignKeys=[ForeignKey(entity=TrackedItem::class,parentColumns=["id"],childColumns=["itemId"],onDelete=ForeignKey.CASCADE)],indices=[Index("itemId",unique=true)]) data class ReminderRule(@PrimaryKey val itemId:Long,val timing:String="ON_DUE",val offsetDays:Int=0,val repeatWeekly:Boolean=false,val hour:Int=9,val minute:Int=0)
@Entity(tableName="custom_field_definitions",indices=[Index("itemId")]) data class CustomFieldDefinition(@PrimaryKey(autoGenerate=true) val id:Long=0,val itemId:Long,val label:String,val type:String="TEXT")
@Entity(tableName="custom_field_values",indices=[Index("definitionId"),Index("completionId")]) data class CustomFieldValue(@PrimaryKey(autoGenerate=true) val id:Long=0,val definitionId:Long,val completionId:Long?=null,val value:String)
@Entity(tableName="app_settings") data class AppSettings(@PrimaryKey val id:Int=1,val comingDuePercent:Int=80,val overduePercent:Int=100,val quietStart:Int=22,val quietEnd:Int=8)

data class ItemSummary(@Embedded val item:TrackedItem,@ColumnInfo(name="categoryName") val categoryName:String,@ColumnInfo(name="categoryColor") val categoryColor:Long,@ColumnInfo(name="lastDone") val lastDone:Long?,@ColumnInfo(name="completionCount") val completionCount:Int)
