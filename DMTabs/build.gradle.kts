version = "1.0.1" // Plugin version. Increment this to trigger the updater
description = "Switches the DM button with the Friends button and viceversa" // Plugin description that will be shown to user

aliucord {
    changelog.set("""
        # 1.0.1
	DM button now switches you to Home Tab.
    """.trimIndent())

    // Excludes this plugin from the updater, meaning it won't show up for users.
    // Set this if the plugin is unfinished
    excludeFromUpdaterJson.set(false)
    author("razertexz", 0)
}
