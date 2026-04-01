package om.ega.sunkey.NoticeSound

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

class PluginSettings (private val settings: SettingsAPI) : SettingsPage() {
	val sonido = "https://github.com/OmegaSunkey/awesomeplugins/raw/main/ping.mp3"
	override fun onViewBound(view: View) {
		super.onViewBound(view)
		setActionBarTitle("NoticeSound")
                val texto = TextInput(view.context, "Añadir enlace / Set link")
		texto.editText.setText(
			settings.getString("sonido", sonido)
		)
		texto.editText.inputType = InputType.TYPE_CLASS_TEXT
		texto.editText.addTextChangedListener(object : TextWatcher() {
			override fun afterTextChanged(editable: Editable) {
				try {
				        settings.setString("sonido",
					java.lang.String.valueOf(editable)
				)

				} catch (e: Exception) {
					settings.setString("sonido", sonido)
				}
			}
		})
		addView(texto)
	}
}
