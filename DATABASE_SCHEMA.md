# Database schema

Room version 1 defines `categories`, `items`, `completions`, `people`, `pets`, `vehicles`, `locations`, four item relationship cross-reference tables, `photos`, `reminder_rules`, `custom_field_definitions`, `custom_field_values`, and `app_settings`.

Item/category and completion/item foreign keys are restrictive; reminder rules cascade because they have no independent history. Foreign-key columns and common sort/filter columns are indexed. Future versions will ship explicit `Migration` objects and checked-in schema snapshots before changing production tables.
