package om.ega.sunkey.Ads

import android.content.Context
import com.aliucord.Utils.createCheckedSetting
import com.aliucord.api.SettingsAPI
import com.aliucord.fragments.SettingsPage
import android.view.View
import android.widget.TextView
import com.discord.app.AppFragment
import com.discord.utilities.color.ColorCompat
import com.lytefast.flexinput.R

class PluginSettings (private val settings: SettingsAPI) : SettingsPage() {
	override fun onViewBound(view: View) {
		super.onViewBound(view)
		setActionBarTitle("Ads")
		val uid = settings.getString("uid", "1")
                val texto = TextView(view.context).apply {
			text = "uid: ${uid}"
		}
		addView(texto)
	}
}
