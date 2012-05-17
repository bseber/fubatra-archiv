package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;

public class SimplePagerI18N extends SimplePager {

	public SimplePagerI18N(TextLocation location, Resources resources) {
		super(location, resources, false, 0, true);
	}
	
	@Override
	protected String createText() {
	    HasRows display = getDisplay();
	    Range range = display.getVisibleRange();
	    int pageStart = range.getStart() + 1;
	    int pageSize = range.getLength();
	    int dataSize = display.getRowCount();
	    int endIndex = Math.min(dataSize, pageStart + pageSize - 1);
	    endIndex = Math.max(pageStart, endIndex);
	    
	    if (dataSize == 0) {
	    	pageStart = 0;
	    	endIndex = 0;
	    }
	    
	    // Default text is 1 based.
	    NumberFormat formatter = NumberFormat.getFormat("#.###");
	    String start = formatter.format(pageStart);
		String end = formatter.format(endIndex);
		String size = formatter.format(dataSize);
		
		if (display.isRowCountExact()) {
			return start + "-" + end + " von " + size;
		} else {
			return start + "-" + end + " von über " + size;
		}
	}
	
}
