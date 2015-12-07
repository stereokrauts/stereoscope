package commands;

import model.Document;

import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPlugin;
import com.stereokrauts.stereoscope.plugin.interfaces.IPersistentPluginConfiguration;
import com.stereokrauts.stereoscope.plugin.model.InternalMixerPluginBuilder;

/**
 * This command adds a new mixer to a document.
 * @author theide
 *
 */
public final class AddMixerToDocumentCommand implements ICommand {

	private final String pluginId;
	private IPersistentPluginConfiguration configuration = null;
	
	private final Document doc;
	private IMixerPlugin createdPlugin;

	public AddMixerToDocumentCommand(final Document document, final String pluginId, final IPersistentPluginConfiguration configuration) {
		this.pluginId = pluginId;
		this.configuration = configuration;
		this.doc = document;
	}

	public AddMixerToDocumentCommand(final Document document, final String pluginId) {
		this.doc = document;
		this.pluginId = pluginId;
	}

	@Override
	public String getDescription() {
		return "Adds a new mixer plugin instances to the currently focused document.";
	}

	@Override
	public void execute() throws Exception {
		if (this.configuration == null) {
			this.createdPlugin = InternalMixerPluginBuilder.build(this.pluginId, this.doc.getApplicationContextForPlugin());
		} else {
			this.createdPlugin = InternalMixerPluginBuilder.build(this.pluginId, this.doc.getApplicationContextForPlugin(), this.configuration);
		}
		this.doc.addMixerPlugin(this.createdPlugin);
	}

	@Override
	public Object getReturnValue() {
		return null;
	}

	@Override
	public boolean isUndoable() {
		return true;
	}

	@Override
	public void undo() throws Exception {
		this.doc.removeMixerPlugin(this.createdPlugin);
	}

}
