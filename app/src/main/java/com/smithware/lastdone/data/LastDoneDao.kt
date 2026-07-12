package com.smithware.lastdone.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao interface LastDoneDao {
 @Query("SELECT i.*, c.name categoryName, c.color categoryColor, MAX(r.completedAt) lastDone, COUNT(r.id) completionCount FROM items i JOIN categories c ON c.id=i.categoryId LEFT JOIN completions r ON r.itemId=i.id GROUP BY i.id ORDER BY CASE WHEN MAX(r.completedAt) IS NULL THEN 0 ELSE 1 END, MAX(r.completedAt)") fun observeItems():Flow<List<ItemSummary>>
 @Query("SELECT i.*, c.name categoryName, c.color categoryColor, MAX(r.completedAt) lastDone, COUNT(r.id) completionCount FROM items i JOIN categories c ON c.id=i.categoryId LEFT JOIN completions r ON r.itemId=i.id WHERE i.id=:id GROUP BY i.id") fun observeItem(id:Long):Flow<ItemSummary?>
 @Query("SELECT * FROM completions WHERE itemId=:itemId ORDER BY completedAt DESC") fun observeHistory(itemId:Long):Flow<List<CompletionRecord>>
 @Query("SELECT * FROM completions ORDER BY completedAt DESC") fun observeAllHistory():Flow<List<CompletionRecord>>
 @Query("SELECT * FROM categories WHERE hidden=0 ORDER BY builtIn DESC,name") fun observeCategories():Flow<List<Category>>
 @Insert suspend fun insertCategory(value:Category):Long
 @Insert suspend fun insertItem(value:TrackedItem):Long
 @Update suspend fun updateItem(value:TrackedItem)
 @Insert suspend fun insertCompletion(value:CompletionRecord):Long
 @Update suspend fun updateCompletion(value:CompletionRecord)
 @Delete suspend fun deleteCompletion(value:CompletionRecord)
 @Query("DELETE FROM completions WHERE id=:id") suspend fun deleteCompletionById(id:Long)
 @Query("UPDATE items SET archived=1 WHERE id=:id") suspend fun archive(id:Long)
 @Query("DELETE FROM items WHERE id=:id AND NOT EXISTS(SELECT 1 FROM completions WHERE itemId=:id)") suspend fun deleteEmptyItem(id:Long):Int
 @Query("SELECT * FROM categories ORDER BY id") suspend fun categoriesOnce():List<Category>
 @Query("SELECT * FROM items ORDER BY id") suspend fun itemsOnce():List<TrackedItem>
 @Query("SELECT * FROM completions ORDER BY completedAt") suspend fun completionsOnce():List<CompletionRecord>
 @Query("SELECT * FROM items WHERE id=:id") suspend fun itemOnce(id:Long):TrackedItem?
 @Query("SELECT MAX(completedAt) FROM completions WHERE itemId=:id") suspend fun lastDone(id:Long):Long?
 @Query("SELECT i.*, c.name categoryName, c.color categoryColor, MAX(r.completedAt) lastDone, COUNT(r.id) completionCount FROM items i JOIN categories c ON c.id=i.categoryId LEFT JOIN completions r ON r.itemId=i.id WHERE i.archived=0 GROUP BY i.id ORDER BY i.createdAt, i.id LIMIT 1") suspend fun widgetItem():ItemSummary?
 @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun restoreCategories(values:List<Category>)
 @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun restoreItems(values:List<TrackedItem>)
 @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun restoreCompletions(values:List<CompletionRecord>)
 @Query("DELETE FROM completions") suspend fun clearCompletions()
 @Query("DELETE FROM items") suspend fun clearItems()
 @Query("DELETE FROM categories") suspend fun clearCategories()
 @Transaction suspend fun replaceCoreData(categories:List<Category>,items:List<TrackedItem>,completions:List<CompletionRecord>){clearCompletions();clearItems();clearCategories();restoreCategories(categories);restoreItems(items);restoreCompletions(completions)}
}
