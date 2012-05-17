package de.fubatra.archiv.client.util;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;

public class ViewUtil {
	
	/**
	 * Adds the given display to the provider if it is not present yet
	 * 
	 * @param provider
	 * @param display
	 */
	public static <T> void addDataDisplay(AbstractDataProvider<T> provider, HasData<T> display) {
		if (provider == null || display == null) {
			return;
		}
		if (!provider.getDataDisplays().contains(display)) {
			provider.addDataDisplay(display);
		}
	}
	
	/**
	 * Checks the position of the eastPanel. If it overlaps the centerPanel it
	 * will be moved to the bottom of the page. If the panel is positioned east,
	 * eastInner's margin will be 1em and east's background-color will be
	 * rgba(255, 255, 255, 0.1).
	 * 
	 * <pre>
	 * .center {
	 *   // inline-block to be able to calc the div's width
	 *   display: inline-block;
	 * }
	 *	
	 * .east {
	 *   // inline-block to be able to calc the div's width
	 *   display: inline-block;
	 * }
	 *
	 * &lt;g:Panel ui:field="container"&gt;
	 *   &lt;g:Panel styleName="center" ui:field="center" /&gt;
	 *     &lt;g:Panel styleName="east" ui:field="east"&gt;
	 *       &lt;g:Panel ui:field="eastInner" /&gt;
	 *     &lt;/g:Panel&gt;
	 * &lt;/g:Panel&gt;
	 * </pre>
	 * 
	 * @param containerWidth
	 *            container is the wrapper of center and east
	 * @param centerWidth
	 * @param eastWidth
	 * @param east
	 *            eastPanel to edit it's style
	 * @param eastInner
	 *            eastInnerPanel to edit it's style
	 */
	public static void checkEastPanelPosition(final int containerWidth, final int centerWidth, final Panel east, final Panel eastInner) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				boolean overlaps = centerWidth + east.getOffsetWidth() + 10 > containerWidth;
				Style eastStyle = east.getElement().getStyle();
				Style eastInnerStyle = eastInner.getElement().getStyle();
				if (overlaps) {
					eastStyle.clearPosition();
					eastStyle.clearTop();
					eastStyle.clearRight();
					eastStyle.clearBackgroundColor();
					eastInnerStyle.clearMargin();
				} else {
					eastStyle.setPosition(Position.ABSOLUTE);
					eastStyle.setTop(0, Unit.PX);
					eastStyle.setRight(0, Unit.PX);
					eastStyle.setBackgroundColor("rgba(255, 255, 255, 0.1)");
					eastInnerStyle.setMargin(1, Unit.EM);
				}
			}
		});
	}
	
	/**
	 * Removes the given display from it's provider if it is present.
	 * 
	 * @param provider
	 * @param display
	 */
	public static <T> void removeDataDisplay(AbstractDataProvider<T> provider, HasData<T> display) {
		if (provider == null || display == null) {
			return;
		}
		if (provider.getDataDisplays().contains(display)) {
			provider.removeDataDisplay(display);
		}
	}

}
