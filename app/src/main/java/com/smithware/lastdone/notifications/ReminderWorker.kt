package com.smithware.lastdone.notifications
import android.app.*
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.smithware.lastdone.R
import java.util.concurrent.TimeUnit

class ReminderWorker(ctx:Context,params:WorkerParameters):CoroutineWorker(ctx,params){override suspend fun doWork():Result{val name=inputData.getString("name")?:"Tracked item";val id=inputData.getLong("id",0);val manager=applicationContext.getSystemService(NotificationManager::class.java);manager.createNotificationChannel(NotificationChannel("lastdone_reminders","LastDone reminders",NotificationManager.IMPORTANCE_DEFAULT));manager.notify(id.toInt(),NotificationCompat.Builder(applicationContext,"lastdone_reminders").setSmallIcon(R.mipmap.ic_launcher).setContentTitle("You may want to review $name").setContentText("Open LastDone to see when it was last completed.").setAutoCancel(true).build());return Result.success()}}
object ReminderScheduler{fun schedule(context:Context,item:com.smithware.lastdone.data.TrackedItem,last:Long?){WorkManager.getInstance(context).cancelUniqueWork("item-${item.id}");val days=item.expectedDays?:return;if(!item.reminderEnabled||last==null||days<=0)return;val due=last+TimeUnit.DAYS.toMillis(days.toLong());val delay=(due-System.currentTimeMillis()).coerceAtLeast(1_000);val request=OneTimeWorkRequestBuilder<ReminderWorker>().setInitialDelay(delay,TimeUnit.MILLISECONDS).setInputData(workDataOf("id" to item.id,"name" to item.name)).build();WorkManager.getInstance(context).enqueueUniqueWork("item-${item.id}",ExistingWorkPolicy.REPLACE,request)}fun test(context:Context){val request=OneTimeWorkRequestBuilder<ReminderWorker>().setInitialDelay(2,TimeUnit.SECONDS).setInputData(workDataOf("id" to 9876L,"name" to "Test reminder")).build();WorkManager.getInstance(context).enqueueUniqueWork("lastdone-test-reminder",ExistingWorkPolicy.REPLACE,request)}}
