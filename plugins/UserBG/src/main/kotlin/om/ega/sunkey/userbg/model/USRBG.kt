package om.ega.sunkey.userbg.model

import com.aliucord.PluginManager
import com.aliucord.Utils
import com.aliucord.api.PatcherAPI
import om.ega.sunkey.userbg.UserBG
import com.aliucord.api.SettingsAPI
import com.aliucord.patcher.Hook
import com.discord.utilities.icon.IconUtils
import com.discord.widgets.user.profile.UserProfileHeaderViewModel
import java.util.regex.Pattern

object USRBG : AbstractDatabase() {
    override val regex: String = ".*?\"(http?s:\\/\\/[\\w.\\/-]*)\""
    override val url: String = "https://usrbg.is-hardly.online/users"

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
                ) return@Hook   // explode

                val id = it.args[0] as Long
                if (mapCache.containsKey(id)) it.result = mapCache[id] else {
                    val matcher = Pattern.compile(
                        id.toString(),
                        Pattern.DOTALL
                    ).matcher(data) //matches id
                    if (matcher.find()) {
                            mapCache[id] = "https://usrbg.is-hardly.online/usrbg/v2/" + id.toString()
                            it.result = "https://usrbg.is-hardly.online/usrbg/v2/" + id.toString() //inserts the result
                    }
                } 
            }
        )

        if (PluginManager.isPluginEnabled("ViewProfileImages")) { // this code gets banner and makes it viewable via viewprofileimages ven
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
                    ) it.result = "https://usrbg.cumcord.com/" //dont ask
	 })
     }
 }
    private val bannerMatch =
        Pattern.compile("^https://cdn.discordapp.com/banners/\\d+/[a-z0-9_]+\\.\\w{3,5}\\?size=\\d+$") //this compiles og banner 
}
