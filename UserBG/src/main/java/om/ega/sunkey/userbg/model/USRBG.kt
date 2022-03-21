package om.ega.sunkey.userbg.model

import com.aliucord.PluginManager
import com.aliucord.api.PatcherAPI
import com.aliucord.api.SettingsAPI
import com.aliucord.patcher.Hook
import com.discord.utilities.icon.IconUtils
import com.discord.widgets.user.profile.UserProfileHeaderViewModel
import java.util.regex.Pattern

object USRBG : AbstractDatabase() {
    override val regex: String = ".*?\\(\"(.*?)\""
    override val url: String = "https://discord-custom-covers.github.io/usrbg/dist/usrbg.css"

    override var data: String = ""

    override val mapCache: MutableMap<Long, String> = HashMap()
    override val name: String = "USRBG"

    override fun runPatches(patcher: PatcherAPI, settings: SettingsAPI) {
        patcher.patch(
            IconUtils::class.java.getDeclaredMethod(
                "getForUserBanner",
                Long::class.javaPrimitiveType,
                String::class.java,
                Integer::class.java,
                Boolean::class.javaPrimitiveType
            ), Hook {
                if (it.result != null && settings.getBool(
                        "nitroBanner",
                        true
                    ) && bannerMatch.matcher(it.result.toString()).find()
                ) return@Hook   // could not get USRBG database in time or wasn't available
                val id = it.args[0] as Long
                if (mapCache.containsKey(id)) it.result = mapCache[id] else {
                    val matcher = Pattern.compile(
                        id.toString() + regex,
                        Pattern.DOTALL
                    ).matcher(data)
                    if (matcher.find()) {
                        matcher.group(1)?.let { it1 ->
                            mapCache[id] = it1
                            it.result = it1
                        }
                    }
                }
            }
        )

        if (PluginManager.isPluginEnabled("ViewProfileImages")) { // this code gets banner and makes it viewable ven
            patcher.patch(
                UserProfileHeaderViewModel.ViewState.Loaded::class.java.getDeclaredMethod(
                    "getBanner"
                ), Hook {
                    val user =
                        (it.thisObject as UserProfileHeaderViewModel.ViewState.Loaded).user
                    if (it.result == null && mapCache.containsKey(user.id) && settings.getBool(
                            "nitroBanner",
                            true
                        )
                    ) it.result = "https://usrbg.cumcord.com/"
                })
        }
    }

    private val bannerMatch =
        Pattern.compile("^https://cdn.discordapp.com/banners/\\d+/[a-z0-9_]+\\.\\w{3,5}\\?size=\\d+$")
}
