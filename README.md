# LastDone

LastDone is Smithware Studios' calm, local-first answer to “When did I last do this?” It tracks occasional maintenance, care, personal check-ins, and other easy-to-forget activities without streaks, guilt, accounts, ads, or a mandatory connection.

## MVP highlights

- Kotlin, Jetpack Compose, Material 3, Room, DataStore, WorkManager, MVVM
- Today, Items, History, and Settings navigation
- Two-tap Done Now flow with note, cost, measurement, and Undo
- Neutral elapsed-time and due-status calculations
- 60+ built-in templates and 18 categories
- Local demo data, persistent history, interval statistics, archive protection
- Local reminder scheduling and a quick-completion home-screen widget
- Backdated completions, a Settings test-reminder action, and a stable live-data widget target
- System-picker JSON backups with validation and confirmed replacement restore
- CSV completion-history export with quoted fields
- Warm cream, moss, amber, copper, and charcoal visual identity

Build with `./gradlew :app:testDebugUnitTest :app:assembleDebug`. Release builds intentionally fail closed unless ignored `keystore.properties` contains a real local signing configuration.

See [ARCHITECTURE.md](ARCHITECTURE.md), [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md), [PRIVACY.md](PRIVACY.md), [TESTING_REPORT.md](TESTING_REPORT.md), [KNOWN_LIMITATIONS.md](KNOWN_LIMITATIONS.md), and [ROADMAP.md](ROADMAP.md).
