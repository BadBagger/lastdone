package com.smithware.lastdone
import androidx.lifecycle.*
import com.smithware.lastdone.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LastDoneViewModel(private val repo:LastDoneRepository,private val store:SettingsStore):ViewModel(){
 val items=repo.items.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList());val categories=repo.categories.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList());val history=repo.allHistory.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList());val settings=store.values.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),Settings(CompletionMode.CONFIRM,false,false))
 init{viewModelScope.launch{repo.seed()}}
 fun add(name:String,categoryId:Long,days:Int?,last:Long?=null,notes:String="",person:String="",location:String=""){viewModelScope.launch{repo.addItem(TrackedItem(name=name.trim(),categoryId=categoryId,expectedDays=days,notes=notes,associatedPerson=person,location=location),last)}}
 fun done(id:Long,date:Long=System.currentTimeMillis(),note:String="",cost:Long?=null,measurement:String="",onSaved:(Long)->Unit={}){viewModelScope.launch{onSaved(repo.complete(id,date,note,cost,measurement))}}
 fun undo(id:Long)=viewModelScope.launch{repo.undoCompletion(id)}
 fun deleteCompletion(r:CompletionRecord)=viewModelScope.launch{repo.deleteCompletion(r)};fun archive(id:Long)=viewModelScope.launch{repo.archive(id)};fun demo()=viewModelScope.launch{repo.demo()};fun finishOnboarding()=viewModelScope.launch{store.finishOnboarding()};fun mode(v:CompletionMode)=viewModelScope.launch{store.setMode(v)};fun dark(v:Boolean)=viewModelScope.launch{store.setDark(v)}
 fun updateCompletion(r:CompletionRecord)=viewModelScope.launch{repo.updateCompletion(r)}
 fun duplicateCompletion(r:CompletionRecord)=viewModelScope.launch{repo.duplicateCompletion(r)}
 fun testReminder()=repo.testReminder()
 fun item(id:Long)=repo.item(id);fun itemHistory(id:Long)=repo.history(id)
}
class LastDoneVmFactory(private val app:LastDoneApp):ViewModelProvider.Factory{override fun<T:ViewModel>create(modelClass:Class<T>):T=@Suppress("UNCHECKED_CAST") LastDoneViewModel(app.repository,app.settings) as T}
