package om.ega.sunkey.StartupSound2

import android.content.Context
import com.aliucord.Utils.createCheckedSetting
import com.aliucord.api.SettingsAPI
import com.aliucord.fragments.SettingsPage
import com.aliucord.views.TextInput
import android.text.Editable
import android.text.InputType
import android.view.View
import com.discord.app.AppFragment
import com.discord.utilities.view.text.TextWatcher
import om.ega.sunkey.StartupSound2.StartupSound2

class PluginSettings (private val settings: SettingsAPI) : SettingsPage() {
	val sonido = "https://github.com/OmegaSunkey/awesomeplugins/blob/main/Discord%20Startup%20Sound%20HQ.mp3?raw=true"
	override fun onViewBound(view: View) {
		super.onViewBound(view)
		setActionBarTitle("StartupSound")
                val texto = TextInput(view.context)
		texto.editText.setText(
			settings.getString("sonido", sonido)
		)
		texto.editText.inputType = InputType.TYPE_CLASS_TEXT
		texto.editText.addTextChangedListener(object : TextWatcher() {
			override fun afterTextChanged(editable: Editable) {
				try {
					if (java.lang.String.valueOf(editable) != null) settings.setString("sonido",
					java.lang.String.valueOf(editable)
				)

				} catch (e: Exception) {
					settings.setString("sonido", sonido)
				}
			}
		})
	}
}
