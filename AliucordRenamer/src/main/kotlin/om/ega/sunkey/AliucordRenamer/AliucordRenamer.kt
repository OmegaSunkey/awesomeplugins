package om.ega.sunkey.AliucordRenamer

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import com.aliucord.Constants
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.after
import com.discord.utilities.color.ColorCompat
import com.discord.widgets.settings.WidgetSettings
import com.lytefast.flexinput.R

@AliucordPlugin
class AliucordRenamer : Plugin() {
    init {
        settingsTab = SettingsTab(
            PluginSettings::class.java,
            SettingsTab.Type.PAGE
        ).withArgs(settings)
    }

    @Suppress("SetTextI18n", "Deprecation") // cope
    override fun start(ctx: Context) {
	patcher.after<WidgetSettings>("onViewBound", View::class.java) {
		val context = requireContext()
		val root = it.args[0] as CoordinatorLayout
		val view = (root.getChildAt(1) as NestedScrollView).getChildAt(0) as LinearLayoutCompat

            val version = view.findViewById<TextView>(Utils.getResId("app_info_header", "id"))
            version.text = settings.getString("name", "set a name in settings")
        }
    }
override fun stop(ctx: Context) {
        patcher.unpatchAll()
        commands.unregisterAll()
    }
}
