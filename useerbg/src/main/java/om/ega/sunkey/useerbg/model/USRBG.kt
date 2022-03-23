package om.ega.sunkey.useerbg.model

import com.aliucord.PluginManager
import com.aliucord.Utils
import com.aliucord.api.PatcherAPI
import om.ega.sunkey.useerbg.usrbg
import com.aliucord.api.SettingsAPI
import com.aliucord.patcher.Hook
import com.discord.utilities.icon.IconUtils
import com.discord.widgets.user.profile.UserProfileHeaderViewModel
import java.util.regex.Pattern

object USRBG : AbstractDatabase() {
    override val regex: String = ".*?\\(\"(.*?)\""
    override val url: String = "https://github.com/Discord-Custom-Covers/usrbg/blob/master/dist/usrbg.json"

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
		usrbg.log.debug(it.args.toString() + " it args array")
		usrbg.log.debug(it.args[0].toString() + " it args 0") 
		usrbg.log.debug(it.args[1].toString() + " it args 1")
		usrbg.log.debug(it.args[2].toString() + " it args 2")
		usrbg.log.debug(id.toString() + " id")
		usrbg.log.debug(it.result.toString() + " result")
                if (!mapCache.containsKey(id)) it.result = mapCache[id] else {
                    val matcher = Pattern.compile(
                        id.toString() + regex,
			Pattern.DOTALL
                    ).matcher(data)
                    if (matcher.find()) {
                        matcher.group(1)?.let { it1 ->
                            mapCache[id] = it1
                            it.result = it1
			    usrbg.log.debug(it1.toString() + "it1")
			    usrbg.log.debug(it.result.toString() + "itresult")
			    usrbg.log.debug(id.toString() + "id")                                
			    usrbg.log.debug(it.args[1].toString() + "itargs0")
                        }
                    } else {
			    it.result = "https://media.discordapp.net/attachments/929565544334647356/956283792333615175/Screenshot_20220322-224827638.jpg"
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
