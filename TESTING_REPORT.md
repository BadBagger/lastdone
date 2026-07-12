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
- Release/signature, GitHub, and DevHub v0.1.0 checks passed. v0.1.1 repeats build/signature/release verification after these refinements.

Room instrumentation scaffolding is included through `room-testing`; the broader requested UI/Room matrix remains tracked in `KNOWN_LIMITATIONS.md`.
