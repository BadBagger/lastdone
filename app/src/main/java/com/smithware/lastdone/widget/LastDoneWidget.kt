package com.smithware.lastdone.widget
import android.app.*
import android.appwidget.*
import android.content.*
import android.widget.RemoteViews
import com.smithware.lastdone.*
import com.smithware.lastdone.data.*
import com.smithware.lastdone.domain.LastDoneCalculations
import kotlinx.coroutines.*

class LastDoneWidget:AppWidgetProvider(){override fun onUpdate(context:Context,manager:AppWidgetManager,ids:IntArray){val pending=goAsync();CoroutineScope(Dispatchers.IO).launch{ids.forEach{update(context,manager,it)};pending.finish()}}override fun onEnabled(context:Context){refreshWidgets(context)}}
fun refreshWidgets(context:Context){CoroutineScope(Dispatchers.IO).launch{val manager=AppWidgetManager.getInstance(context);manager.getAppWidgetIds(ComponentName(context,LastDoneWidget::class.java)).forEach{update(context,manager,it)}}}
private suspend fun update(context:Context,manager:AppWidgetManager,id:Int){val summary=LastDoneDatabase.get(context).dao().widgetItem();val views=RemoteViews(context.packageName,R.layout.lastdone_widget);views.setTextViewText(R.id.widget_title,summary?.item?.name?:"LastDone");views.setTextViewText(R.id.widget_elapsed,summary?.let{LastDoneCalculations.elapsedLabel(it.lastDone)}?:"Add an item in LastDone");views.setOnClickPendingIntent(R.id.widget_title,PendingIntent.getActivity(context,0,Intent(context,MainActivity::class.java),PendingIntent.FLAG_IMMUTABLE));val done=Intent(context,WidgetDoneReceiver::class.java).putExtra("itemId",summary?.item?.id?:0L);views.setOnClickPendingIntent(R.id.widget_done,PendingIntent.getBroadcast(context,id,done,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT));manager.updateAppWidget(id,views)}
class WidgetDoneReceiver:BroadcastReceiver(){override fun onReceive(context:Context,intent:Intent){val pending=goAsync();CoroutineScope(Dispatchers.IO).launch{val dao=LastDoneDatabase.get(context).dao();val requested=intent.getLongExtra("itemId",0);val item=if(requested>0)dao.itemOnce(requested) else dao.widgetItem()?.item;if(item!=null)dao.insertCompletion(CompletionRecord(itemId=item.id,completedAt=System.currentTimeMillis()));refreshWidgets(context);pending.finish()}}}
