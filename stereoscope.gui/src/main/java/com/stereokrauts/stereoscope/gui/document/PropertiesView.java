package com.stereokrauts.stereoscope.gui.document;

import model.properties.IPropertyElement;
import model.properties.IPropertyProvider;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

/**
 * Displays the properties of a property container on the screen.
 * @author th
 *
 */
public final class PropertiesView extends Composite {
	private static final int HEADING_COLOR_BLUE = 200;
	private static final int HEADING_COLOR_GREEN = 200;
	private static final int HEADING_COLOR_RED = 200;
	private static final int MINIMUM_COLUMN_WIDTH = 100;
	private static final int VALUE_WEIGHT = 30;
	private static final int KEY_WEIGHT = 20;
	private final Table table;
	private final TableViewer tableViewer;
	private TableViewerColumn keyColumn;
	private TableViewerColumn valueColumn;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PropertiesView(final Composite parent, final int style) {
		super(parent, style);
		this.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		this.setLayout(tableColumnLayout);
		
		this.tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		this.tableViewer.setContentProvider(PropertiesStructuredContentProvider.getInstance());
		this.createColumns(this, this.tableViewer);
		
		tableColumnLayout.setColumnData(this.keyColumn.getColumn(), new ColumnWeightData(KEY_WEIGHT, MINIMUM_COLUMN_WIDTH, true));
		tableColumnLayout.setColumnData(this.valueColumn.getColumn(), new ColumnWeightData(VALUE_WEIGHT, MINIMUM_COLUMN_WIDTH, true));
		
		this.table = this.tableViewer.getTable();
		this.table.setHeaderVisible(true);
		this.table.setLinesVisible(true);
	}
	
	private void createColumns(final Composite parent, final TableViewer viewer) {		
		this.keyColumn = new TableViewerColumn(viewer, SWT.NONE);
		this.keyColumn.getColumn().setText("Key");
	
		this.keyColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element instanceof IPropertyElement) {
					final IPropertyElement p = (IPropertyElement) element;
					return p.getDisplayName() != null ? p.getDisplayName().toString() : null;
				}
				if (element instanceof PropertyTableHeading) {
					final PropertyTableHeading propertyTableHeading = (PropertyTableHeading) element;
					return propertyTableHeading.getHeading();
				}
				return element.toString();
			}
			
			@Override
			public Color getBackground(final Object element) {
				if (element instanceof PropertyTableHeading) {
					return new Color(PropertiesView.this.getDisplay(), HEADING_COLOR_RED, HEADING_COLOR_GREEN, HEADING_COLOR_BLUE);
				} else {
					return super.getBackground(element);
				}
			}
			
			@Override
			public Font getFont(final Object element) {
				if (element instanceof PropertyTableHeading) {
					final Font font = PropertiesView.this.table.getFont();
					final FontData[] data = font.getFontData();
					data[0].setStyle(SWT.BOLD);
					return new Font(PropertiesView.this.getDisplay(), data);
				} else {
					return super.getFont(element);
				}
			}
		}); 
		
		this.valueColumn = new TableViewerColumn(viewer, SWT.NONE);
		this.valueColumn.getColumn().setText("Value");
		this.valueColumn.setEditingSupport(new PropertyValueEditingSupport(this.tableViewer));
		this.valueColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element instanceof IPropertyElement) {
					final IPropertyElement p = (IPropertyElement) element;
					return (p.getValue() == null) ? "" : p.getValue().toString();
				}
				return "";
			}
			
			@Override
			public Color getBackground(final Object element) {
				if (element instanceof PropertyTableHeading) {
					return new Color(PropertiesView.this.getDisplay(), HEADING_COLOR_RED, HEADING_COLOR_GREEN, HEADING_COLOR_BLUE);
				} else {
					return super.getBackground(element);
				}
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void setInput(final IPropertyProvider properties) {
		this.tableViewer.setInput(properties);
		this.tableViewer.refresh();
	}
}
