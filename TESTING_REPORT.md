# Testing report — v0.1.0-mvp

- JVM tests: elapsed labels, neutral due thresholds, average intervals, costs, future validation, leap day, and DST calendar-day behavior.
- Debug build: passed.
- Emulator: installed and launched on Android x86_64; onboarding and demo loading passed.
- Main flow: recorded Dental cleaning through Done Now; elapsed label changed to Today.
- Persistence: force-stopped and relaunched; Today remained visible from Room.
- Crash check: no LastDone fatal exception in captured logcat.
- Widget: installed from the Pixel Launcher widget picker, rendered live Room data, and its Done Now action changed the selected item to Today.
- Reminder: Settings > Send test reminder executed through WorkManager; Android notification state contained `You may want to review Test reminder`.
- Backdating: recorded an Air filter completion 10 days in the past; the app displayed `10 days ago`.
- Backup: system document picker created a versioned JSON backup containing 7 items and 10 completions.
- CSV: system document picker created a CSV with real item names, timestamps, notes, costs, measurements, and relationship fields.
- Restore: added an 11th completion after backup, reopened the backup, received a validation summary and explicit replacement confirmation, restored it, then re-exported exactly 7 items and 10 completions.
- Backup unit tests cover round trip, wrong format, broken foreign keys, and future completion rejection.
- Completion editing: changed Backed up laptop from 37 days ago to 5 days ago and added a note; detail elapsed time updated to `5 days ago`.
- Completion duplication: duplicated the edited entry; history displayed two independent records and the newest became `Today`.
- Completion deletion: confirmation explained recalculation impact; deleting the duplicate returned history to one record and elapsed time to `5 days ago`.
- Release/signature, GitHub, and DevHub onboarding checks passed. Each follow-up release repeats build and signature verification.

Room instrumentation scaffolding is included through `room-testing`; the broader requested UI/Room matrix remains tracked in `KNOWN_LIMITATIONS.md`.
