package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

import de.fubatra.archiv.client.ui.resources.AppResources;
import de.fubatra.archiv.shared.domain.Rating;

public class RatingWidget extends Composite implements LeafValueEditor<Rating>, HasClickHandlers {

	private final AbstractImagePrototype star;
	private final AbstractImagePrototype empty;

	private FlowPanel container;
	private Image[] ratingImageArray;

	private Rating rating;

	public RatingWidget(AppResources resources, boolean editable) {
		this.star = AbstractImagePrototype.create(resources.star());
		this.empty = AbstractImagePrototype.create(resources.emptyStar());

		Image badStar = initBadStar(editable);
		Image goodStar = initGoodStar(editable);
		Image superStar = initSuperStar(editable);
		Image awesomeStar = initAwesomeStar(editable);
		ratingImageArray = new Image[4];
		ratingImageArray[0] = badStar;
		ratingImageArray[1] = goodStar;
		ratingImageArray[2] = superStar;
		ratingImageArray[3] = awesomeStar;
		
		container = new FlowPanel();
		container.add(badStar);
		container.add(goodStar);
		container.add(superStar);
		container.add(awesomeStar);
		initWidget(container);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return container.addDomHandler(handler, ClickEvent.getType());
	}

	@Override
	public void setValue(Rating value) {
		rating = value;
		showOriginalRating();
	}

	@Override
	public Rating getValue() {
		return rating;
	}

	private Image initAwesomeStar(boolean editable) {
		Image awesomeStar = new Image();
		if (!editable) {
			return awesomeStar;
		}
		awesomeStar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				rating = Rating.AWESOME;
			}
		});
		awesomeStar.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				showRating(Rating.AWESOME);
			}
		});
		awesomeStar.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				showOriginalRating();
			}
		});
		return awesomeStar;
	}

	private Image initSuperStar(boolean editable) {
		Image superStar = new Image();
		if (!editable) {
			return superStar;
		}
		superStar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				rating = Rating.SUPER;
			}
		});
		superStar.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				showRating(Rating.SUPER);
			}
		});
		superStar.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				showOriginalRating();
			}
		});
		return superStar;
	}

	private Image initGoodStar(boolean editable) {
		Image goodStar = new Image();
		if (!editable) {
			return goodStar;
		}
		goodStar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				rating = Rating.GOOD;
			}
		});
		goodStar.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				showRating(Rating.GOOD);
			}
		});
		goodStar.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				showOriginalRating();
			}
		});
		return goodStar;
	}

	private Image initBadStar(boolean editable) {
		Image badStar = new Image();
		if (!editable) {
			return badStar;
		}
		badStar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				rating = Rating.BAD;
			}
		});
		badStar.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				showRating(Rating.BAD);
			}
		});
		badStar.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				showOriginalRating();
			}
		});
		return badStar;
	}
	
	private void showOriginalRating() {
		showRating(rating);
	}
	
	private void showRating(Rating rating) {
		switch (rating) {
		case NONE:
			empty.applyTo(ratingImageArray[0]);
			empty.applyTo(ratingImageArray[1]);
			empty.applyTo(ratingImageArray[2]);
			empty.applyTo(ratingImageArray[3]);
			break;
		case BAD:
			star.applyTo(ratingImageArray[0]);
			empty.applyTo(ratingImageArray[1]);
			empty.applyTo(ratingImageArray[2]);
			empty.applyTo(ratingImageArray[3]);
			break;
		case GOOD:
			star.applyTo(ratingImageArray[0]);
			star.applyTo(ratingImageArray[1]);
			empty.applyTo(ratingImageArray[2]);
			empty.applyTo(ratingImageArray[3]);
			break;
		case SUPER:
			star.applyTo(ratingImageArray[0]);
			star.applyTo(ratingImageArray[1]);
			star.applyTo(ratingImageArray[2]);
			empty.applyTo(ratingImageArray[3]);
			break;
		case AWESOME:
			star.applyTo(ratingImageArray[0]);
			star.applyTo(ratingImageArray[1]);
			star.applyTo(ratingImageArray[2]);
			star.applyTo(ratingImageArray[3]);
			break;
		}
	}

}
