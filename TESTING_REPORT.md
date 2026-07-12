# Testing report — v0.1.0-mvp

- JVM tests: elapsed labels, neutral due thresholds, average intervals, costs, future validation, leap day, and DST calendar-day behavior.
- Debug build: passed.
- Emulator: installed and launched on Android x86_64; onboarding and demo loading passed.
- Main flow: recorded Dental cleaning through Done Now; elapsed label changed to Today.
- Persistence: force-stopped and relaunched; Today remained visible from Room.
- Crash check: no LastDone fatal exception in captured logcat.
- Release/signature, GitHub, widget, reminder, and DevHub results are appended to release notes after final verification.

Room instrumentation scaffolding is included through `room-testing`; the broader requested UI/Room matrix remains tracked in `KNOWN_LIMITATIONS.md`.
