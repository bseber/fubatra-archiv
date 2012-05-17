package de.fubatra.archiv.client.ui.widgets.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import de.fubatra.archiv.client.ui.resources.AppResources;
import de.fubatra.archiv.client.ui.resources.TrainingSessionListResources;
import de.fubatra.archiv.client.util.ListToStringUtil;
import de.fubatra.archiv.shared.domain.Rating;
import de.fubatra.archiv.shared.domain.TrainingSessionProxy;

public class TrainingSessionCell extends AbstractCell<TrainingSessionProxy> {

	public interface ITemplate extends SafeHtmlTemplates {
		
		@Template("<h2>{0}</h2>")
		SafeHtml title(String text);
		
		@Template("<table>" +
				"<colgroup>" +
					"<col style=\"width:10em;\" />" +
					"<col />" +
					"<col />" +
				"</colgroup>" +
				"<tbody>" +
					"<tr>" +
						"<td>Erstellt von:</td>" +
						"<td>{0}</td>" +
					"</tr>" +
					"<tr>" +
						"<td>Erstellt am:</td>" +
						"<td>{1}</td>" +
					"</tr>" +
					"<tr>" +
						"<td style=\"padding-top:1em;\">Thema:</td>" +
						"<td colspan=\"2\" style=\"padding-top:1em;\">{2}</td>" +
					"</tr>" +
					"<tr>" +
						"<td>Altersgruppe:</td>" +
						"<td colspan=\"2\">{3}</td>" +
					"</tr>" +
				"</tbody>" +
				"</table>")
		SafeHtml table(String owner, String created, String subject, String team);
		
	}
	
	private static final ITemplate template = GWT.create(ITemplate.class);
	private static final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
	
	private final SafeHtml arrowSafeHtml;
	private final SafeHtml emptyStarSafeHtml;
	private final SafeHtml starSafeHtml;
	
	public TrainingSessionCell(AppResources appResources, TrainingSessionListResources listResources, String... consumedEvents) {
		super(consumedEvents);
		arrowSafeHtml = AbstractImagePrototype.create(listResources.doubleArrow()).getSafeHtml();
		emptyStarSafeHtml = AbstractImagePrototype.create(appResources.emptyStar()).getSafeHtml();
		starSafeHtml = AbstractImagePrototype.create(appResources.star()).getSafeHtml();
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, TrainingSessionProxy value, SafeHtmlBuilder sb) {
		if (value == null) {
			return;
		}
		
		String title = value.getTopic();
		if (title == null) {
			title = "";
		}
		String subjects = ListToStringUtil.subjectListToString(value.getSubjects());
		String teams = ListToStringUtil.teamListToString(value.getTeams());
		String owner = value.getOwner().getName();
		String created = dateTimeFormat.format(value.getCreationDate());
		int ratingCount = value.getRatingCount();
		Rating rating = value.getAverageRating();
		
		sb.append(template.title(title))
		.appendHtmlConstant("<table width=\"100%\">" +
				"<colgroup>" +
					"<col style=\"width:17em;\" />" +
					"<col />" +
					"<col style=\"width:5em;vertical-align:top;text-align:right;\" />" +
				"</colgroup>" +
				"<tbody>" +
					"<tr>" +
						"<td style='vertical-align:top;padding-top:5px'>");
		//
		// training session average rating
		//
		sb.appendHtmlConstant("<div>")
		.appendEscaped("Durchschnittliche Bewertung")
		.appendHtmlConstant("</div>");
		if (rating == null) {
			sb.append(emptyStarSafeHtml)
			.append(emptyStarSafeHtml)
			.append(emptyStarSafeHtml)
			.append(emptyStarSafeHtml);
		} else {
			switch (rating) {
			case AWESOME:
				sb.append(starSafeHtml)
				.append(starSafeHtml)
				.append(starSafeHtml)
				.append(starSafeHtml);
				break;
			case SUPER:
				sb.append(starSafeHtml)
				.append(starSafeHtml)
				.append(starSafeHtml)
				.append(emptyStarSafeHtml);
				break;
			case GOOD:
				sb.append(starSafeHtml)
				.append(starSafeHtml)
				.append(emptyStarSafeHtml)
				.append(emptyStarSafeHtml);
				break;
			case BAD:
				sb.append(starSafeHtml)
				.append(emptyStarSafeHtml)
				.append(emptyStarSafeHtml)
				.append(emptyStarSafeHtml);
				break;
			default:
				sb.append(emptyStarSafeHtml)
				.append(emptyStarSafeHtml)
				.append(emptyStarSafeHtml)
				.append(emptyStarSafeHtml);
				break;
			}
		}
		sb.appendHtmlConstant("<div>");
		if (ratingCount == 1) {
			sb.appendEscaped("(1 Bewertung)");
		} else {
			sb.appendEscaped("(" + ratingCount + " Bewertungen)");
		}
		sb.appendHtmlConstant("</div>")
		.appendHtmlConstant("</td>");
		//
		// training session info
		//
		sb.appendHtmlConstant("<td>")
		.append(template.table(owner, created, subjects, teams))
		.appendHtmlConstant("</td>")
		.appendHtmlConstant("<td>")
		.append(arrowSafeHtml)
		.appendHtmlConstant("</td></tr></tbody></table>");
	}
	
}
