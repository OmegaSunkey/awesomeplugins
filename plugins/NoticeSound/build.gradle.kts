version = "1.0.1" // Plugin version. Increment this to trigger the updater
description = "Get notifications in-app with sounds!" // Plugin description that will be shown to user

aliucord {
    changelog.set("""
        # 1.0.0
	Initial release
    # 1.0.1
    * saves custom ping file as userping.mp3 instead of weird overrides that i didn't notice before lol
    """.trimIndent())

    // Excludes this plugin from the updater, meaning it won't show up for users.
    // Set this if the plugin is unfinished
    //excludeFromUpdaterJson.set(false)
}
