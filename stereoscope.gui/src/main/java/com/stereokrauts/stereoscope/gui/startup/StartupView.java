package com.stereokrauts.stereoscope.gui.startup;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.controller.StartupController;
import com.stereokrauts.stereoscope.gui.util.ImageUtil;
import com.stereokrauts.stereoscope.plugin.guihelper.MixerPluginImageAssociation;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

/***
 * This class defines the layout of the main view that is displayed
 * after startup.
 * @author th
 *
 */
public final class StartupView extends ViewPart {
	public static final String ID = "com.stereokrauts.stereoscope.gui.StartupView"; //$NON-NLS-1$
	public static final SLogger LOG = StereoscopeLogManager.getLogger(StartupView.class);
	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());

	private final List<MixerPluginImageAssociation> mixerButtons = new ArrayList<MixerPluginImageAssociation>();
	private Composite mixerImagesContainer;
	private StartupController controller;

	public StartupView() {
		this.setTitleImage(ResourceManager.getPluginImage("org.eclipse.ui", "/icons/full/elcl16/home_nav.gif"));
		this.setPartName("Quick Start");
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {

		final SashForm sashDemoMixerSelection = new SashForm(parent, SWT.VERTICAL);
		toolkit.adapt(sashDemoMixerSelection);
		toolkit.paintBordersFor(sashDemoMixerSelection);

		final Composite container = this.toolkit.createComposite(sashDemoMixerSelection, SWT.NONE);
		this.toolkit.paintBordersFor(container);
		container.setLayout(new FillLayout());

		final ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinWidth(0);
		this.toolkit.adapt(scrolledComposite);
		this.toolkit.paintBordersFor(scrolledComposite);

		this.mixerImagesContainer = new Composite(scrolledComposite, SWT.BORDER);
		this.toolkit.adapt(this.mixerImagesContainer);
		this.toolkit.paintBordersFor(this.mixerImagesContainer);

		final RowLayout mixerImagesContainerLayout = new RowLayout(SWT.HORIZONTAL);
		mixerImagesContainerLayout.justify = true;
		mixerImagesContainerLayout.center = true;
		this.mixerImagesContainer.setLayout(mixerImagesContainerLayout);

		scrolledComposite.setMinHeight(this.mixerImagesContainer.computeSize(scrolledComposite.getBounds().width, SWT.DEFAULT).y);
		scrolledComposite.setContent(this.mixerImagesContainer);
		sashDemoMixerSelection.setWeights(new int[] {1 });

		for (final MixerPluginImageAssociation assc : this.mixerButtons) {
			final StartupMixerButton button = new StartupMixerButton(this.mixerImagesContainer, SWT.NONE);
			button.setText(assc.getPluginName());
			button.setImage(new Image(Display.getCurrent(), ImageUtil.getScaledImageDataFrom(assc.getImage(), 300, 100)));
			button.setPluginAssociation(assc);

			button.addListener(SWT.MouseUp, new Listener() {
				@Override
				public void handleEvent(final Event event) {
					LOG.info("Clicked: " + button.getPluginAssociation().getPluginId());
					StartupView.this.controller.createNewDocumentFromStartupView(button.getPluginAssociation().getPluginId());
				}
			});
		}


		this.createActions();
		this.initializeToolBar();
		this.initializeMenu();
	}

	public void addMixerButton(final MixerPluginImageAssociation assc) {
		this.mixerButtons.add(assc);
	}



	@Override
	public void dispose() {
		this.toolkit.dispose();
		super.dispose();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		@SuppressWarnings("unused")
		final
		IToolBarManager tbm = this.getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		@SuppressWarnings("unused")
		final
		IMenuManager manager = this.getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	public StartupController getController() {
		return this.controller;
	}

	public void setController(final StartupController controller) {
		this.controller = controller;
	}
}
