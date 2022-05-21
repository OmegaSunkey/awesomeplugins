version = "1.0.0" // Plugin version. Increment this to trigger the updater
description = "Search in Rule34.xxx." // Plugin description that will be shown to user

aliucord {
    // Changelog of your plugin
    changelog.set("""
        # 1.0.0 
        • initial release
    """.trimIndent())
    // Image or Gif that will be shown at the top of your changelog page
    //changelogMedia.set("https://cdn.discordapp.com/attachments/929565544334647356/957419019500146708/Screenshot_20220326-182112113.jpg")

    // Add additional authors to this plugin
    // author("Name", 0)
    // author("Name", 0)

    // Excludes this plugin from the updater, meaning it won't show up for users.
    // Set this if the plugin is unfinished
    excludeFromUpdaterJson.set(true)
}
