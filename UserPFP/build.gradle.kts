version = "1.0.3"
description = "UserPFP, for animated profile pictures"

aliucord {
    changelog.set(
        """
        - 1.0.0
		release; fix HalalKing source and add new db source
		- 1.0.1
		changed regex to ignore ?=
		- 1.0.2
		Added debug setting for knowing when a pfp is incorrect
		- 1.0.3
		Added fallback pfp to be used when an error occurs while trying to load pfp
		Made debug logs setting be false by default
    """.trimIndent()
    )
    author("HalalKing", 0)
}
