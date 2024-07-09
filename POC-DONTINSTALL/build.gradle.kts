version = "1.0.0" // Plugin version. Increment this to trigger the updater
description = "funny proof of concept" // Plugin description that will be shown to user

aliucord {
    changelog.set("""
        # 1.0.0
	Initial release. Do not install or you will fuck up your aliucord.
    """.trimIndent())

    // Excludes this plugin from the updater, meaning it won't show up for users.
    // Set this if the plugin is unfinished
    excludeFromUpdaterJson.set(true)
}
