package com.smithware.lastdone.data
import org.junit.Assert.*
import org.junit.Test

class BackupCodecTest{
 private val category=Category(1,"Maintenance",builtIn=true)
 private val item=TrackedItem(id=2,name="Air filter",categoryId=1,expectedDays=90)
 private val completion=CompletionRecord(id=3,itemId=2,completedAt=1_700_000_000_000,note="MERV 11",costCents=1299)
 @Test fun roundTripPreservesCoreData(){val decoded=BackupCodec.decode(BackupCodec.encode(LastDoneBackup(listOf(category),listOf(item),listOf(completion))));assertEquals(category,decoded.categories.single());assertEquals(item,decoded.items.single());assertEquals(completion,decoded.completions.single())}
 @Test fun rejectsWrongFormat(){assertThrows(IllegalArgumentException::class.java){BackupCodec.decode("{\"format\":\"other\",\"version\":1,\"categories\":[],\"items\":[],\"completions\":[]}")}}
 @Test fun rejectsBrokenForeignKey(){val text="""{"format":"com.smithware.lastdone.backup","version":1,"categories":[],"items":[{"id":2,"name":"Bad","categoryId":99}],"completions":[]}""";assertThrows(IllegalArgumentException::class.java){BackupCodec.decode(text)}}
 @Test fun rejectsFutureCompletion(){val future=System.currentTimeMillis()+86_400_000;val text=BackupCodec.encode(LastDoneBackup(listOf(category),listOf(item),listOf(completion.copy(completedAt=future))));assertThrows(IllegalArgumentException::class.java){BackupCodec.decode(text)}}
}
