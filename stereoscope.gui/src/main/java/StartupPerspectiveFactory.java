

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * This class defines the perspective that is displayed after
 * stereoscope is started.
 * @author th
 *
 */
public final class StartupPerspectiveFactory implements IPerspectiveFactory {
	private static final String VIEW_LOG = "org.eclipse.pde.runtime.LogView";
	private static final String VIEW_LICENSE = "stereoscope.controller.license";

	private static final float STARTUP_MIXERCHOOSER_PERCENTAGE = 0.41f;

	@Override
	public void createInitialLayout(final IPageLayout layout) {
		layout.addStandaloneView("stereoscope.controller.startup", true, IPageLayout.LEFT, STARTUP_MIXERCHOOSER_PERCENTAGE, layout.getEditorArea());
		final IFolderLayout folder = layout.createFolder("bottom", IPageLayout.BOTTOM, 1.0f - STARTUP_MIXERCHOOSER_PERCENTAGE, layout.getEditorArea());
		folder.addView(VIEW_LOG);
		folder.addView(VIEW_LICENSE);

		layout.setEditorAreaVisible(true);

		layout.getViewLayout(VIEW_LOG).setCloseable(false);
		layout.getViewLayout(VIEW_LICENSE).setCloseable(false);
	}
}
