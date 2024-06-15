version = "1.2.1"
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
		- 1.1.4
		Changed DB url to UserPFP/UserPFP/main/source/data.json
		- 1.2.0
		Use a worker for getting a static version of your UserPFP, now you can see your UaerPFP in chat without Aliucord exploding!
		- 1.2.1
		fix insane bug that spammed logs with null cast
    """.trimIndent()
    )
    author("HalalKing", 0)
}
