package com.smithware.lastdone.widget
import android.app.*
import android.appwidget.*
import android.content.*
import android.widget.RemoteViews
import com.smithware.lastdone.*
import com.smithware.lastdone.data.*
import com.smithware.lastdone.domain.LastDoneCalculations
import kotlinx.coroutines.*

class LastDoneWidget:AppWidgetProvider(){override fun onUpdate(context:Context,manager:AppWidgetManager,ids:IntArray){ids.forEach{update(context,manager,it)}}
 companion object {fun update(context:Context,manager:AppWidgetManager,id:Int){val views=RemoteViews(context.packageName,R.layout.lastdone_widget);views.setOnClickPendingIntent(R.id.widget_title,PendingIntent.getActivity(context,0,Intent(context,MainActivity::class.java),PendingIntent.FLAG_IMMUTABLE));val done=Intent(context,WidgetDoneReceiver::class.java);views.setOnClickPendingIntent(R.id.widget_done,PendingIntent.getBroadcast(context,0,done,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT));manager.updateAppWidget(id,views)}}}
class WidgetDoneReceiver:BroadcastReceiver(){override fun onReceive(context:Context,intent:Intent){val pending=goAsync();CoroutineScope(Dispatchers.IO).launch{val dao=LastDoneDatabase.get(context).dao();val first=dao.itemsOnce().firstOrNull{!it.archived};if(first!=null)dao.insertCompletion(CompletionRecord(itemId=first.id,completedAt=System.currentTimeMillis()));AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context,LastDoneWidget::class.java)).forEach{LastDoneWidget.update(context,AppWidgetManager.getInstance(context),it)};pending.finish()}}}
