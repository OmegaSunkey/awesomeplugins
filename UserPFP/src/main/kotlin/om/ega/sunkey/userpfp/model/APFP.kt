package om.ega.sunkey.userpfp.model

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.widget.ImageView
import com.aliucord.api.PatcherAPI
import com.aliucord.api.SettingsAPI
import com.aliucord.patcher.Hook
import com.discord.utilities.icon.IconUtils
import com.facebook.drawee.view.SimpleDraweeView
import java.util.regex.Pattern
import b.f.g.e.s
import com.discord.utilities.images.MGImages


object APFP : AbstractDatabase() {
    override val regex: String = ".*(https:\\/\\/[\\w.\\/-]*).*(https:\\/\\/[\\w.\\/-]*)" //.*(https:\\/\\/.*\\.gif).*(https:\\/\\/.*\\.png|https:\\/\\/.*\\.jpg)
    override val url: String = "https://raw.githubusercontent.com/OmegaSunkey/UserPFP-Discord/main/UserPFP.txt"

    override var data: String = ""

    override val mapCache: MutableMap<Long, PFP> = HashMap()
    override val name: String = "APFP"

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
                val id = it.args[0] as Long
                if (mapCache.containsKey(id))
                    it.result = mapCache[id]?.let { it1 ->  if ((it.args[3] as Boolean)) it1.animated else it1.static
                } else {
                    val matcher = Pattern.compile(
                        id.toString() + regex
                    ).matcher(data)
                    if (matcher.find()) {
                    	if (settings.getBool("debugEnabled", true)) UserPFP.log.debug(it.args[0].toString() + matcher.group(2) + matcher.group(1) + " id, animated, static")
                        mapCache[id] = PFP(matcher.group(2), matcher.group(1)).also {
                                it1 ->  if ((it.args[3] as Boolean)) it.result = it1.animated else it.result = it1.static
                        }
                    }
                }

            }
        )

        patcher.patch(
            IconUtils::class.java.getDeclaredMethod("setIcon", ImageView::class.java, String::class.java, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Boolean::class.javaPrimitiveType, Function1::class.java, MGImages.ChangeDetector::class.java), Hook {
                if ((it.args[1] as String).contains("https://cdn.discordapp.com/role-icons")) return@Hook

                val simpleDraweeView = it.args[0] as SimpleDraweeView
                simpleDraweeView.apply {
                    hierarchy.n(s.l)
                    clipToOutline = true
                    background =
                        ShapeDrawable(OvalShape()).apply { paint.color = Color.TRANSPARENT }
                }
            })
    }

    data class PFP(val static: String, val animated: String)
}
