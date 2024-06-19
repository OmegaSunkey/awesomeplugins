package om.ega.sunkey.userpfp.model

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.OvalShape
import android.widget.ImageView
import java.net.URLEncoder
import com.aliucord.api.PatcherAPI
import com.aliucord.api.SettingsAPI
import com.aliucord.PluginManager
import com.aliucord.patcher.Hook
import com.discord.utilities.icon.IconUtils
import com.facebook.drawee.view.SimpleDraweeView
import java.util.regex.Pattern
import b.f.g.e.s
import om.ega.sunkey.userpfp.UserPFP
import com.discord.utilities.images.MGImages
import kotlin.random.Random


object APFP : AbstractDatabase() {
    override val regex: String = ".*(https:\\/\\/[\\w.\\/-]*)" //".*(https?:.*Avatars.*(?:gif|png|jpe?g|webp))" //*(https:\\/\\/[\\w.\\/-]*)" //.*(https:\\/\\/.*\\.gif).*(https:\\/\\/.*\\.png|https:\\/\\/.*\\.jpg)
    override val url: String = "https://raw.githubusercontent.com/UserPFP/UserPFP/main/source/data.json" //"https://raw.githubusercontent.com/OmegaSunkey/UserPFP-Discord/main/UserPFP.txt"

    override var data: String = ""

    override val mapCache: MutableMap<Long, PFP> = HashMap()
    override val name: String = "APFP"

    var hash = List(20){Random.nextInt(1, 10)}.joinToString("")

    override fun runPatches(patcher: PatcherAPI, settings: SettingsAPI) {
        patcher.patch(
            IconUtils::class.java.getDeclaredMethod(
                "getForUser",
                java.lang.Long::class.java,
                String::class.java,
                Integer::class.java,
                Boolean::class.javaPrimitiveType,
                Integer::class.java
            ), Hook {
                if (it.result.toString().contains(".gif") && settings.getBool(
                        "nitroBanner",
                        true
                    )) return@Hook
                //if ((it.args[3] as Boolean) == false) return@Hook if not main profile, unhook
                val id = it.args[0] as Long
                if (mapCache.containsKey(id)) it.result = mapCache[id]?.let { 
		    it1 -> if ((it.args[3] as Boolean)) it1.animated else it1.static
                } else {
                    val matcher = Pattern.compile(
                        id.toString() + regex
                    ).matcher(data)
                    if (matcher.find()) {
                    	if (settings.getBool("debugEnabled", false)) UserPFP.log.debug(it.args[0].toString() + getStatic(matcher.group(1)) + matcher.group(1) + " id, animated, static")
                        mapCache[id] = PFP(matcher.group(1), getStatic(matcher.group(1))).also {
                                it1 -> if ((it.args[3] as Boolean)) it.result = it1.animated else it1.static
                        }
                    } 
                }

            }
        )

        patcher.patch(
            IconUtils::class.java.getDeclaredMethod("setIcon", ImageView::class.java, String::class.java, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Boolean::class.javaPrimitiveType, Function1::class.java, MGImages.ChangeDetector::class.java), Hook {
                if (it.args[1] == null || (it.args[1] as String).contains("https://cdn.discordapp.com/role-icons")) return@Hook
		UserPFP.log.debug("arg1 is " + it.args[1] + "is this the cause of your issues?")

                val simpleDraweeView = it.args[0] as SimpleDraweeView
                simpleDraweeView.apply {
                    hierarchy.n(s.l)
                    clipToOutline = true
                    background = if(PluginManager.isPluginEnabled("SquareAvatars")) {
                    	ShapeDrawable(RoundRectShape(RoundValue(), null, null)).apply { paint.color = Color.TRANSPARENT }
                    } else {
                        ShapeDrawable(OvalShape()).apply { paint.color = Color.TRANSPARENT }
                    }
                }
            })
    }

    fun RoundValue(): FloatArray {
    	val SquareSettings = PluginManager.plugins.get("SquareAvatars")!!.settings!!.getInt("roundCorners", 3)
    	val FloatValue = FloatArray (8) {SquareSettings!!.toFloat()}
    	return FloatValue
    }

    fun getStatic(gif: String): String {
    	val encoded = URLEncoder.encode(gif, "UTF-8")
        return "https://static-gif.nexpid.workers.dev/convert.gif?url=" + "$encoded" + "&_=$hash"
    }

    data class PFP(val animated: String, val static: String)
}
