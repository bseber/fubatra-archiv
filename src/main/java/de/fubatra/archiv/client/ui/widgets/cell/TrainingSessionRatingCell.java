package de.fubatra.archiv.client.ui.widgets.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import de.fubatra.archiv.client.ui.resources.AppResources;
import de.fubatra.archiv.shared.domain.Rating;

public class TrainingSessionRatingCell extends AbstractCell<Rating> {

	private AbstractImagePrototype emptyStar;
	private AbstractImagePrototype star;

	public TrainingSessionRatingCell(AppResources resources) {
		emptyStar = AbstractImagePrototype.create(resources.emptyStar16());
		star = AbstractImagePrototype.create(resources.star16());
	}
	
	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, Rating value, SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}
		switch (value) {
		case AWESOME:
			sb.append(star.getSafeHtml());
			sb.append(star.getSafeHtml());
			sb.append(star.getSafeHtml());
			sb.append(star.getSafeHtml());
			break;
		case SUPER:
			sb.append(star.getSafeHtml());
			sb.append(star.getSafeHtml());
			sb.append(star.getSafeHtml());
			sb.append(emptyStar.getSafeHtml());
			break;
		case GOOD:
			sb.append(star.getSafeHtml());
			sb.append(star.getSafeHtml());
			sb.append(emptyStar.getSafeHtml());
			sb.append(emptyStar.getSafeHtml());
			break;
		case BAD:
			sb.append(star.getSafeHtml());
			sb.append(emptyStar.getSafeHtml());
			sb.append(emptyStar.getSafeHtml());
			sb.append(emptyStar.getSafeHtml());
			break;
		case NONE:
			sb.append(emptyStar.getSafeHtml());
			sb.append(emptyStar.getSafeHtml());
			sb.append(emptyStar.getSafeHtml());
			sb.append(emptyStar.getSafeHtml());
			break;
		}
	}

}
