package com.smithware.lastdone.data
import android.content.Context
import com.smithware.lastdone.notifications.ReminderScheduler
import kotlinx.coroutines.flow.Flow

class LastDoneRepository(private val context:Context,private val dao:LastDoneDao){
 val items=dao.observeItems(); val categories=dao.observeCategories(); val allHistory=dao.observeAllHistory()
 fun item(id:Long)=dao.observeItem(id); fun history(id:Long)=dao.observeHistory(id)
 suspend fun addItem(item:TrackedItem,lastDate:Long?=null):Long{val id=dao.insertItem(item); if(lastDate!=null)dao.insertCompletion(CompletionRecord(itemId=id,completedAt=lastDate)); schedule(id); return id}
 suspend fun complete(id:Long,date:Long=System.currentTimeMillis(),note:String="",cost:Long?=null,measurement:String=""):Long{val result=dao.insertCompletion(CompletionRecord(itemId=id,completedAt=date,note=note,costCents=cost,measurement=measurement));schedule(id);return result}
 suspend fun deleteCompletion(r:CompletionRecord){dao.deleteCompletion(r);schedule(r.itemId)}
 suspend fun undoCompletion(id:Long){dao.deleteCompletionById(id)}
 suspend fun archive(id:Long)=dao.archive(id)
 suspend fun seed(){if(dao.categoriesOnce().isEmpty()) listOf("Home","Cleaning","Maintenance","Vehicle","Health","Dental","Personal care","Family","Relationships","Pets","Technology","Finance","Work","Travel","Safety","Appliances","Seasonal","Other").forEachIndexed{i,n->dao.insertCategory(Category(name=n,icon=listOf("⌂","✦","⚙","◆","♥","◉")[i%6],color=listOf(0xFF637B52,0xFFC47A45,0xFF9A7B4F,0xFF55726C)[i%4],builtIn=true))}}
 suspend fun demo(){seed();val cats=dao.categoriesOnce().associateBy{it.name};val day=86_400_000L;listOf(Triple("Air filter","Maintenance",82),Triple("Called Mom","Relationships",11),Triple("Backed up laptop","Technology",37),Triple("Tire rotation","Vehicle",150),Triple("Dental cleaning","Dental",210),Triple("Flea treatment","Pets",26),Triple("Cleaned coffee maker","Cleaning",31)).forEach{(n,c,d)->addItem(TrackedItem(name=n,categoryId=cats.getValue(c).id,expectedDays=when(n){"Air filter"->90;"Flea treatment"->30;else->null},notes="Demo item"),System.currentTimeMillis()-d*day)}}
 private suspend fun schedule(id:Long){val item=dao.itemOnce(id)?:return; ReminderScheduler.schedule(context,item,dao.lastDone(id))}
}
