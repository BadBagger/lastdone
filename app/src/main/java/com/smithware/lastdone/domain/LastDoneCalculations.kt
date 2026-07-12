package com.smithware.lastdone.domain
import java.time.*
import java.time.temporal.ChronoUnit

enum class ItemStatus { NEVER_RECORDED, RECENTLY_COMPLETED, ON_TRACK, COMING_DUE, REVIEW_RECOMMENDED, OVERDUE, NO_SCHEDULE }
object LastDoneCalculations {
 fun elapsedLabel(then:Long?,now:Long=System.currentTimeMillis(),zone:ZoneId=ZoneId.systemDefault()):String{
  if(then==null)return "Never recorded"
  val a=Instant.ofEpochMilli(then).atZone(zone).toLocalDate(); val b=Instant.ofEpochMilli(now).atZone(zone).toLocalDate(); val d=ChronoUnit.DAYS.between(a,b).coerceAtLeast(0)
  return when { d==0L->"Today"; d==1L->"Yesterday"; d<14->"$d days ago"; d<60->"${d/7} weeks ago"; d<730->"${ChronoUnit.MONTHS.between(a.withDayOfMonth(1),b.withDayOfMonth(1)).coerceAtLeast(1)} months ago"; else->"${ChronoUnit.YEARS.between(a,b).coerceAtLeast(1)} years ago" }
 }
 fun status(last:Long?,expectedDays:Int?,now:Long=System.currentTimeMillis(),coming:Int=80,overdue:Int=100):ItemStatus{
  if(last==null)return ItemStatus.NEVER_RECORDED; if(expectedDays==null)return ItemStatus.NO_SCHEDULE; if(expectedDays<=0)return ItemStatus.REVIEW_RECOMMENDED
  val days=Duration.between(Instant.ofEpochMilli(last),Instant.ofEpochMilli(now)).toDays().coerceAtLeast(0); val pct=days*100/expectedDays
  return when { pct>=overdue+25->ItemStatus.OVERDUE; pct>=overdue->ItemStatus.REVIEW_RECOMMENDED; pct>=coming->ItemStatus.COMING_DUE; pct<20->ItemStatus.RECENTLY_COMPLETED; else->ItemStatus.ON_TRACK }
 }
 fun nextSuggested(last:Long?,days:Int?)=if(last==null||days==null||days<=0)null else last+Duration.ofDays(days.toLong()).toMillis()
 fun intervals(records:List<Long>):List<Long> = records.sorted().zipWithNext{a,b->Duration.between(Instant.ofEpochMilli(a),Instant.ofEpochMilli(b)).toDays()}.filter{it>=0}
 fun averageInterval(records:List<Long>)=intervals(records).takeIf{it.isNotEmpty()}?.average()
 fun validateDate(value:Long,now:Long=System.currentTimeMillis())=value<=now+60_000
 fun totalCost(costs:List<Long?>)=costs.filterNotNull().sum()
}
