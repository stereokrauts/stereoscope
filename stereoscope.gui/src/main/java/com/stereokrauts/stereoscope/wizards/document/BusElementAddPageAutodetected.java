package com.stereokrauts.stereoscope.wizards.document;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.logging.Level;

import model.surface.touchosc.BonjourAdvertisedTouchOscInstance;
import model.surface.touchosc.BonjourTouchOscHelper;
import model.surface.touchosc.IObserveClients;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.wizards.document.BusElementAddWizardResult.ClientType;

public final class BusElementAddPageAutodetected extends WizardPage implements IObserveClients {
	final class AutoDetectionLabelProvider extends ColumnLabelProvider {
		private Image image;

		@Override
		public String getText(final Object element) {
			final BonjourAdvertisedTouchOscInstance p = (BonjourAdvertisedTouchOscInstance) element;
			return "Surface at " +  p.getIpAddress() + ", Port " + p.getPort();
		}

		@Override
		public Image getImage(final Object element) {
			if (this.image == null) {
				this.loadImage();
			}
			return this.image;
		}

		private void loadImage() {
			URL imageUrl;
			try {
				imageUrl = new URL("platform:/plugin/stereoscope/resources/images/a_ipad.png");
				final ImageData imgData = new ImageData(imageUrl.openStream());
				this.image = this.resize(new Image(PlatformUI.getWorkbench().getDisplay(), imgData), 75, 55);
			} catch (final Exception e) {
				/* uncritical */
			}
		}

		private Image resize(final Image image, final int width, final int height) {
			final Image scaled = new Image(Display.getDefault(), width, height);
			final GC gc = new GC(scaled);
			gc.setAntialias(SWT.ON);
			gc.setInterpolation(SWT.HIGH);
			gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, width, height);
			gc.dispose();
			image.dispose(); // don't forget about me!
			return scaled;
		}
	}

	private static final SLogger LOGGER = StereoscopeLogManager.getLogger(BusElementAddPageAutodetected.class); 

	private BonjourTouchOscHelper bonjourHelper;
	private TableViewer autodetectedList;

	private Table table;

	private Spinner webclientPort;

	final class AutoDetectionContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		}

		@Override
		public Object[] getElements(final Object inputElement) {
			if (inputElement instanceof BonjourTouchOscHelper) {
				return ((BonjourTouchOscHelper) inputElement).getInstances().toArray();
			}
			return new Object[0];
		}

	}

	/**
	 * Create the wizard.
	 */
	public BusElementAddPageAutodetected() {
		super("wizardPage");
		this.setTitle("Add a new element to the bus");
		this.setDescription("Found by Autodetection");
		this.setPageComplete(true);

		final ProgressMonitorDialog progress = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
		try {
			progress.run(false, false, new IRunnableWithProgress() {
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException,
				InterruptedException {
					monitor.setTaskName("Starting Bonjour Subsystem ...");
					try {
						BusElementAddPageAutodetected.this.bonjourHelper = BonjourTouchOscHelper.getInstance();
					} catch (final Exception e) {
						LOGGER.log(Level.WARNING, "Could not autodetect TouchOSC surfaces", e);
					}
					BusElementAddPageAutodetected.this.bonjourHelper.registerObserver(BusElementAddPageAutodetected.this);
					BusElementAddPageAutodetected.this.update();
				}
			});
		} catch (final Exception e) {
			LOGGER.log(Level.WARNING, "Could not autodetect TouchOSC surfaces", e);
		}

	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	@Override
	public void createControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);

		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);

		final Button webclient = new Button(container, SWT.RADIO);
		webclient.setText("Add a new server for a webclient on port");
		webclient.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				autodetectedList.getTable().setEnabled(false);
				webclientPort.setEnabled(true);
				getCastedWizard().getResult().setType(ClientType.WEBCLIENT);
				getCastedWizard().getResult().setWebClientPort(webclientPort.getText());
				getWizard().getContainer().updateButtons();
			}


			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}
		});

		webclientPort = new Spinner(container, SWT.DEFAULT);
		webclientPort.setValues(8090, 0, 65535, 0, 1, 10);
		webclientPort.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				getCastedWizard().getResult().setWebClientPort(webclientPort.getText());				
			}
		});
		webclientPort.setEnabled(false);

		final Button touchosc = new Button(container, SWT.RADIO);
		touchosc.setText("Add a new TouchOSC client (autodetection below, manual setup on next page)");
		touchosc.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				autodetectedList.getTable().setEnabled(true);
				webclientPort.setEnabled(false);
				getCastedWizard().getResult().setType(ClientType.TOUCHOSC);
				getWizard().getContainer().updateButtons();
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}
		});
		final GridData twoCols = new GridData();
		twoCols.horizontalSpan = 2;
		touchosc.setLayoutData(twoCols);

		this.setControl(container);

		this.autodetectedList = new TableViewer(container, SWT.BORDER | SWT.V_SCROLL);
		this.table = this.autodetectedList.getTable();
		final GridData twoCols3 = new GridData();
		twoCols3.horizontalSpan = 2;
		twoCols3.horizontalIndent = 18;
		twoCols3.grabExcessHorizontalSpace = true;
		twoCols3.grabExcessVerticalSpace = true;
		twoCols3.verticalAlignment = SWT.TOP;
		twoCols3.minimumWidth = 450;
		table.setLayoutData(twoCols3);
		this.autodetectedList.setContentProvider(new AutoDetectionContentProvider());
		this.autodetectedList.setLabelProvider(new AutoDetectionLabelProvider());
		this.table.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				BusElementAddPageAutodetected.this.processResultOfSelection();
			}
		});

		touchosc.setSelection(true);

		this.update();
	}

	private BusElementAddWizard getCastedWizard() {
		return (BusElementAddWizard) getWizard();
	}

	private void processResultOfSelection() {
		final TableItem[] selection = this.table.getSelection();
		final BonjourAdvertisedTouchOscInstance instance = (BonjourAdvertisedTouchOscInstance) selection[0].getData();
		((BusElementAddWizard) this.getWizard()).getResult().setClientPort("" + instance.getPort());
		((BusElementAddWizard) this.getWizard()).getResult().setNetworkAddress(instance.getIpAddress());
		getWizard().getContainer().updateButtons();
	}

	@Override
	public void surfaceDetected(final BonjourAdvertisedTouchOscInstance inst) {
		this.update();
	}

	private void update() {
		if (this.autodetectedList != null && this.bonjourHelper != null) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					BusElementAddPageAutodetected.this.autodetectedList.setInput(BusElementAddPageAutodetected.this.bonjourHelper);
					BusElementAddPageAutodetected.this.autodetectedList.refresh();
					if (BusElementAddPageAutodetected.this.autodetectedList.getElementAt(0) != null) {
						BusElementAddPageAutodetected.this.autodetectedList.setSelection(new StructuredSelection(BusElementAddPageAutodetected.this.autodetectedList.getElementAt(0)));
						BusElementAddPageAutodetected.this.processResultOfSelection();
					}
					getWizard().getContainer().updateButtons();
				}
			});
		}
	}

	@Override
	public boolean canFlipToNextPage() {
		return getCastedWizard().getResult().isTouchOSC();
	}

	@Override
	public IWizardPage getNextPage() {
		if (getCastedWizard().getResult().isTouchOSC()) {
			return super.getNextPage();
		}
		return null;
	}
}
