package de.fubatra.archiv.client.ui.resources;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellList;

public interface TrainingSessionListResources extends CellList.Resources {
	
    @Source("trainingSessionListStyle.css")
    TrainingSessionListStyle cellListStyle();
    
    @Source("doubleArrow.png")
    ImageResource doubleArrow();
	
}
