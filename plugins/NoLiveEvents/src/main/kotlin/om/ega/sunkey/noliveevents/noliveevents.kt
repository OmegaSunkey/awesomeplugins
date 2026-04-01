package om.ega.sunkey.noliveevents

import android.content.Context

import com.discord.widgets.guildscheduledevent.WidgetPreviewGuildScheduledEvent
import com.discord.widgets.guildscheduledevent.PreviewGuildScheduledEventViewModel

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.Utils
import com.aliucord.Logger
import com.aliucord.patcher.*

@AliucordPlugin
class NoLiveEvents : Plugin() {
    override fun start(c: Context) {
	patcher.patch(
		WidgetPreviewGuildScheduledEvent::class.java.getDeclaredMethod(
			"configureUi",
			PreviewGuildScheduledEventViewModel.ViewState::class.java
		), PreHook {
			it.result = null
		}
	)
    }
    override fun stop(c: Context) {
	patcher.unpatchAll()
    }
}
