# Architecture

LastDone uses a small clean-architecture split: Compose screens call `LastDoneViewModel`; the view model exposes flows and commands from `LastDoneRepository`; the repository owns transactional Room operations and reminder rescheduling; calculation logic lives in a platform-independent domain object. `SettingsStore` owns DataStore preferences. `ReminderWorker` is the local notification boundary and the widget receiver performs database updates off the main thread.

Room is the source of truth. UI summaries derive the newest completion with an indexed aggregate query, so editing or deleting history automatically recalculates elapsed time and status. Parent items with important history use restrict/archive behavior rather than destructive cascade deletion.
