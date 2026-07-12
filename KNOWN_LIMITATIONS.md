# Known limitations

This MVP prioritizes the complete everyday record-and-recall loop. The following requested breadth is not yet complete:

- Done Now supports simple day-based backdating; a full date/time picker and photo attachment are not wired yet.
- Supporting people, pets, vehicles, locations, photos, custom fields, and reminder-rule tables exist, but their full CRUD screens are not yet exposed.
- Completion edit/duplicate/detail filters and item edit/delete confirmation are limited; completion delete and item archive are functional.
- CSV, JSON, printable, text, and photo-inclusive backup/restore validation UI is planned, not shipped.
- Widget v1 records a stable first-created active item and does not yet offer small/medium/large selection screens.
- WorkManager scheduling and a verified test reminder are implemented, but per-item configuration, quiet hours, quick notification actions, and permission-denied explanation need expansion.
- The requested full Room and Compose instrumentation matrix is not yet implemented; domain tests and emulator main-flow evidence are included.
