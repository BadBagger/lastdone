package com.smithware.lastdone.domain
import org.junit.Assert.*
import org.junit.Test
import java.time.*
class LastDoneCalculationsTest{
 private val zone=ZoneId.of("America/New_York");private fun at(y:Int,m:Int,d:Int)=LocalDate.of(y,m,d).atStartOfDay(zone).toInstant().toEpochMilli()
 @Test fun labels(){assertEquals("Today",LastDoneCalculations.elapsedLabel(at(2026,1,1),at(2026,1,1),zone));assertEquals("Yesterday",LastDoneCalculations.elapsedLabel(at(2025,12,31),at(2026,1,1),zone));assertEquals("3 weeks ago",LastDoneCalculations.elapsedLabel(at(2026,1,1),at(2026,1,22),zone));assertEquals("2 months ago",LastDoneCalculations.elapsedLabel(at(2026,1,1),at(2026,3,2),zone))}
 @Test fun statuses(){val now=at(2026,4,11);assertEquals(ItemStatus.NEVER_RECORDED,LastDoneCalculations.status(null,90,now));assertEquals(ItemStatus.COMING_DUE,LastDoneCalculations.status(now-Duration.ofDays(80).toMillis(),90,now));assertEquals(ItemStatus.REVIEW_RECOMMENDED,LastDoneCalculations.status(now-Duration.ofDays(91).toMillis(),90,now));assertEquals(ItemStatus.OVERDUE,LastDoneCalculations.status(now-Duration.ofDays(120).toMillis(),90,now))}
 @Test fun averagesCosts(){val values=listOf(at(2026,1,1),at(2026,1,11),at(2026,1,31));assertEquals(15.0,LastDoneCalculations.averageInterval(values)!!,0.01);assertEquals(350,LastDoneCalculations.totalCost(listOf(100,null,250)))}
 @Test fun edgeDates(){assertFalse(LastDoneCalculations.validateDate(at(2027,1,1),at(2026,1,1)));assertEquals("Yesterday",LastDoneCalculations.elapsedLabel(at(2024,2,29),at(2024,3,1),zone));assertEquals("Yesterday",LastDoneCalculations.elapsedLabel(at(2026,3,8),at(2026,3,9),zone))}
}
