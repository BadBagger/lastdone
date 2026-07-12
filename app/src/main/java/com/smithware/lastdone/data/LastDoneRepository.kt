package com.smithware.lastdone.data
import android.content.Context
import com.smithware.lastdone.notifications.ReminderScheduler
import kotlinx.coroutines.flow.Flow

class LastDoneRepository(private val context:Context,private val dao:LastDoneDao){
 val items=dao.observeItems(); val categories=dao.observeCategories(); val allHistory=dao.observeAllHistory()
 fun item(id:Long)=dao.observeItem(id); fun history(id:Long)=dao.observeHistory(id)
 suspend fun addItem(item:TrackedItem,lastDate:Long?=null):Long{val id=dao.insertItem(item); if(lastDate!=null)dao.insertCompletion(CompletionRecord(itemId=id,completedAt=lastDate)); schedule(id); return id}
 suspend fun complete(id:Long,date:Long=System.currentTimeMillis(),note:String="",cost:Long?=null,measurement:String=""):Long{require(com.smithware.lastdone.domain.LastDoneCalculations.validateDate(date)){"A completion cannot be in the future"};val result=dao.insertCompletion(CompletionRecord(itemId=id,completedAt=date,note=note,costCents=cost,measurement=measurement));schedule(id);com.smithware.lastdone.widget.refreshWidgets(context);return result}
 suspend fun deleteCompletion(r:CompletionRecord){dao.deleteCompletion(r);schedule(r.itemId);com.smithware.lastdone.widget.refreshWidgets(context)}
 suspend fun updateCompletion(r:CompletionRecord){require(com.smithware.lastdone.domain.LastDoneCalculations.validateDate(r.completedAt)){"A completion cannot be in the future"};dao.updateCompletion(r);schedule(r.itemId);com.smithware.lastdone.widget.refreshWidgets(context)}
 suspend fun duplicateCompletion(r:CompletionRecord):Long{val copy=r.copy(id=0,completedAt=System.currentTimeMillis());val id=dao.insertCompletion(copy);schedule(r.itemId);com.smithware.lastdone.widget.refreshWidgets(context);return id}
 suspend fun undoCompletion(id:Long){dao.deleteCompletionById(id);com.smithware.lastdone.widget.refreshWidgets(context)}
 suspend fun archive(id:Long)=dao.archive(id)
 suspend fun seed(){if(dao.categoriesOnce().isEmpty()) listOf("Home","Cleaning","Maintenance","Vehicle","Health","Dental","Personal care","Family","Relationships","Pets","Technology","Finance","Work","Travel","Safety","Appliances","Seasonal","Other").forEachIndexed{i,n->dao.insertCategory(Category(name=n,icon=listOf("⌂","✦","⚙","◆","♥","◉")[i%6],color=listOf(0xFF637B52,0xFFC47A45,0xFF9A7B4F,0xFF55726C)[i%4],builtIn=true))}}
 suspend fun demo(){seed();val cats=dao.categoriesOnce().associateBy{it.name};val day=86_400_000L;listOf(Triple("Air filter","Maintenance",82),Triple("Called Mom","Relationships",11),Triple("Backed up laptop","Technology",37),Triple("Tire rotation","Vehicle",150),Triple("Dental cleaning","Dental",210),Triple("Flea treatment","Pets",26),Triple("Cleaned coffee maker","Cleaning",31)).forEach{(n,c,d)->addItem(TrackedItem(name=n,categoryId=cats.getValue(c).id,expectedDays=when(n){"Air filter"->90;"Flea treatment"->30;else->null},notes="Demo item"),System.currentTimeMillis()-d*day)}}
 fun testReminder()=ReminderScheduler.test(context)
 suspend fun createBackup():String=BackupCodec.encode(LastDoneBackup(dao.categoriesOnce(),dao.itemsOnce(),dao.completionsOnce()))
 suspend fun createCsv():String{val items=dao.itemsOnce().associateBy{it.id};return buildString{appendLine("item,date_time_epoch_ms,note,cost,measurement,location,person,service_provider");dao.completionsOnce().forEach{r->appendLine(listOf(items[r.itemId]?.name.orEmpty(),r.completedAt.toString(),r.note,r.costCents?.let{"%.2f".format(java.util.Locale.US,it/100.0)}.orEmpty(),r.measurement,r.location,r.person,r.serviceProvider).joinToString(","){csv(it)})}}}
 suspend fun validateBackup(text:String)=BackupCodec.decode(text)
 suspend fun restoreReplace(backup:LastDoneBackup){dao.replaceCoreData(backup.categories,backup.items,backup.completions);backup.items.forEach{schedule(it.id)};com.smithware.lastdone.widget.refreshWidgets(context)}
 private fun csv(value:String)="\"${value.replace("\"","\"\"")}\""
 private suspend fun schedule(id:Long){val item=dao.itemOnce(id)?:return; ReminderScheduler.schedule(context,item,dao.lastDone(id))}
}
