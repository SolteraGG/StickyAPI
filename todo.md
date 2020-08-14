<!-- ### Scheduler

Support for incremental task IDs for the cancellation of tasks

### Fields

- `tasks` - represents unfinished tasks
- `nextTaskId` - represents the next ID the next task will be assigned to

#### Methods
- `runTask` - accepts a callable and executes it in a separate thread
- `schedule` - accepts a callable and executes it at a later date in a separate thread
- `scheduleTimer` - accepts a timer and schedules it to be executed
- `cancel` - cancel a scheduled task -->

### Localization

- `LocaleProvider` - class for accessing the current localization, will interface with a default locale when none is set explicitly

- setLocale
- getLocale

- `Locale` - constructable class that wraps a configuration file to provide localization values
