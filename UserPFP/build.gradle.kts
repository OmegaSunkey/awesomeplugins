version = "1.1.3"
description = "UserPFP, for animated profile pictures"

aliucord {
    changelog.set(
        """
        - 1.0.0
		release; fix HalalKing source and add new db source.
		- 1.0.1
		changed regex to ignore ?=.
		- 1.0.2
		Added debug setting for knowing when a pfp is incorrect.
		- 1.0.3
		(Added fallback pfp to be used when an error occurs while trying to load pfp.) disabled for now.
		Made debug logs setting be false by default.
		- 1.1.0
		Switch to USRPFP database, which is used on Vencord and Vendetta.
		Unhook while on ChatView, to evade lag and crashes.
		- 1.1.1
		Make UserPFP work with SquareAvatars.
		- 1.1.2
		Use data.json instead of dist.css because it updates faster
		- 1.1.3
		Sometimes the plugin would show a badge instead of the avatar, this is fixed now
    """.trimIndent()
    )
    author("HalalKing", 0)
}
