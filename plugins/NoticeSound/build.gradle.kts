version = "1.0.0" // Plugin version. Increment this to trigger the updater
description = "Get notifications in-app with sounds!" // Plugin description that will be shown to user

aliucord {
    changelog.set("""
        # 1.0.0
	Initial release
    """.trimIndent())

    // Excludes this plugin from the updater, meaning it won't show up for users.
    // Set this if the plugin is unfinished
    excludeFromUpdaterJson.set(false)
}
