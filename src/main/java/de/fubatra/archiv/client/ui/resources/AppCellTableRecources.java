package de.fubatra.archiv.client.ui.resources;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Style;

public interface AppCellTableRecources extends CellTable.Resources {

    /**
     * The styles used in this widget.
     */
    @Source("celltable-style.css")
    Style cellTableStyle();
	
}
