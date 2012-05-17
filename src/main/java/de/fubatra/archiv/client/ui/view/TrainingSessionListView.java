package de.fubatra.archiv.client.ui.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.mvp4g.client.view.BaseCycleView;
import com.mvp4g.client.view.ReverseViewInterface;

import de.fubatra.archiv.client.events.TrainingFilterChangedEvent;
import de.fubatra.archiv.client.ui.resources.AppResources;
import de.fubatra.archiv.client.ui.resources.TrainingSessionListResources;
import de.fubatra.archiv.client.ui.widgets.SimplePagerI18N;
import de.fubatra.archiv.client.ui.widgets.cell.TrainingSessionCell;
import de.fubatra.archiv.client.ui.widgets.filter.TeamFilter;
import de.fubatra.archiv.client.ui.widgets.filter.TrainingSessionSubjectFilter;
import de.fubatra.archiv.shared.domain.SortByItem;
import de.fubatra.archiv.shared.domain.Team;
import de.fubatra.archiv.shared.domain.TrainingSessionProxy;
import de.fubatra.archiv.shared.domain.TrainingSessionSubject;

public class TrainingSessionListView extends BaseCycleView implements ReverseViewInterface<TrainingSessionListView.IPresenter>, TrainingFilterChangedEvent.Handler {

	private static TrainingSessionListViewUiBinder uiBinder = GWT.create(TrainingSessionListViewUiBinder.class);
	
	public interface IPresenter {

		void onFilterChanged(List<TrainingSessionSubject> subjects, List<Team> teams);

		void refreshData();
	}
	
	interface TrainingSessionListViewUiBinder extends UiBinder<Widget, TrainingSessionListView> {
	}
	
	@UiField
	TrainingSessionSubjectFilter subjects;
	
	@UiField
	TeamFilter teams;
	
	@UiField
	CellList<TrainingSessionProxy> sessionList;
	
	@UiField
	SimplePager pager;
	
	@UiField
	ListBox listBoxSortByItem;
	
	@UiField
	ListBox lbVisibleItems;
	
	private IPresenter presenter;
	private HandlerRegistration subjectsChangeHandler;
	private HandlerRegistration teamsChangeHandler;
	
	private final AppResources appResources;
	private final TrainingSessionListResources listResources;
	
	@Inject
	public TrainingSessionListView(AppResources appResources, TrainingSessionListResources listResources) {
		this.appResources = appResources;
		this.listResources = listResources;
	}
	
	@Override
	public void createView() {
		initWidget(uiBinder.createAndBindUi(this));
		pager.setDisplay(sessionList);
		// TODO save selectedIndex in localStorage and read it again
		lbVisibleItems.setSelectedIndex(2);
		onVisibleItemCountChanged(null);
	}
	
	public HasData<TrainingSessionProxy> getSessionList() {
		return sessionList;
	}
	
	public SortByItem getSelectedSortByItem() {
		int selectedIndex = listBoxSortByItem.getSelectedIndex();
		String enumName = listBoxSortByItem.getValue(selectedIndex);
		return SortByItem.valueOf(enumName);
	}
	
	@Override
	public void setPresenter(IPresenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public IPresenter getPresenter() {
		return presenter;
	}
	
	public int getVisibleItemLimit() {
		int selected = lbVisibleItems.getSelectedIndex();
		return Integer.valueOf(lbVisibleItems.getValue(selected));
	}

	@Override
	public void onTrainingSessionFilterChanged(TrainingFilterChangedEvent event) {
		List<TrainingSessionSubject> subjectsList = subjects.getValue();
		List<Team> teamsList = teams.getValue();
		presenter.onFilterChanged(subjectsList, teamsList);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		// TODO move to presenter?
		if (subjectsChangeHandler == null) {
			subjectsChangeHandler = subjects.addTrainingFilterChangedHandler(this);
		}
		if (teamsChangeHandler == null) {
			teamsChangeHandler = teams.addTrainingFilterChangedHandler(this);
		}
	}
	
	@UiHandler("listBoxSortByItem")
	void onSortItemChanged(ChangeEvent event) {
		presenter.refreshData();
	}
	
	@UiHandler("lbVisibleItems")
	void onVisibleItemCountChanged(ChangeEvent event) {
		int limit = getVisibleItemLimit();
		pager.setPageSize(limit);
		presenter.refreshData();
	}
	
	@UiFactory
	CellList<TrainingSessionProxy> createSessionList() {
		TrainingSessionCell cell = new TrainingSessionCell(appResources, listResources);
		CellList<TrainingSessionProxy> cellList = new CellList<TrainingSessionProxy>(cell, listResources,  new TrainingSessionProxy.KeyProvider());
		
		Image loadingCircle = AbstractImagePrototype.create(appResources.loadingCircle()).createImage();
		loadingCircle.getElement().getStyle().setMargin(1, Unit.EM);
		cellList.setLoadingIndicator(loadingCircle);
		
		Label noDataWidget = new Label("Keine Trainingseinheiten gefunden");
		noDataWidget.getElement().getStyle().setPadding(2, Unit.EM);
		noDataWidget.getElement().getStyle().setFontSize(1.5, Unit.EM);
		noDataWidget.getElement().getStyle().setProperty("textAlign", "center");
		
		cellList.setEmptyListWidget(noDataWidget);
		return cellList;
	}
	
	@UiFactory
	SimplePager createPager() {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePagerI18N(TextLocation.CENTER, pagerResources);
		pager.setRangeLimited(true);
		return pager;
	}

}
