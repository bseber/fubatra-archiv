package de.fubatra.archiv.client.presenter;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.CyclePresenter;

import de.fubatra.archiv.client.ioc.AppEventBus;
import de.fubatra.archiv.client.ui.view.TrainingSessionListView;
import de.fubatra.archiv.client.util.ViewUtil;
import de.fubatra.archiv.shared.domain.Team;
import de.fubatra.archiv.shared.domain.TrainingSessionProxy;
import de.fubatra.archiv.shared.domain.TrainingSessionSubject;
import de.fubatra.archiv.shared.service.AppRequestFactory;
import de.fubatra.archiv.shared.service.TrainingSessionServiceRequest;

@Presenter(view = TrainingSessionListView.class)
public class TrainingSessionListPresenter extends CyclePresenter<TrainingSessionListView, AppEventBus> implements TrainingSessionListView.IPresenter {

	private long fetchRequestId = 0;
	private AsyncDataProvider<TrainingSessionProxy> dataProvider;
	private SingleSelectionModel<TrainingSessionProxy> selectionModel;
	
	private final AppRequestFactory requestFactory;
	
	@SuppressWarnings("unchecked")
	private List<TrainingSessionSubject> subjectsFilter = Collections.EMPTY_LIST;
	
	@SuppressWarnings("unchecked")
	private List<Team> teamsFilter = Collections.EMPTY_LIST;
	
	@Inject
	public TrainingSessionListPresenter(AppRequestFactory requestFactory) {
		super();
		this.requestFactory = requestFactory;
	}
	
	@Override
	public void bindView() {
		TrainingSessionProxy.KeyProvider keyProvider = new TrainingSessionProxy.KeyProvider();
		dataProvider = new AsyncDataProvider<TrainingSessionProxy>(keyProvider) {
			@Override
			protected void onRangeChanged(HasData<TrainingSessionProxy> display) {
				int start = display.getVisibleRange().getStart();
				fetchData(start);
			}
		};
		
		selectionModel = new SingleSelectionModel<TrainingSessionProxy>(keyProvider);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				TrainingSessionProxy selected = selectionModel.getSelectedObject();
				if (selected != null) {
					eventBus.showTrainingSession(selected.getId());
					// deselect to be able to navigate there again
					selectionModel.setSelected(selected, false);
				}
			}
		});
		view.getSessionList().setSelectionModel(selectionModel);
	}
	
	public void onStartPage() {
		ViewUtil.addDataDisplay(dataProvider, view.getSessionList());
		eventBus.setContent(view);
	}

	@Override
	public void onFilterChanged(List<TrainingSessionSubject> subjects, List<Team> teams) {
		this.subjectsFilter = subjects;
		this.teamsFilter = teams;
		refreshData();
	}
	
	@Override
	public void refreshData() {
		fetchData(0);
	}
	
	@Override
	public void onUnload() {
		ViewUtil.removeDataDisplay(dataProvider, view.getSessionList());
	}
	
	@Override
	public void onLoad() {
		// forces data reload
		ViewUtil.addDataDisplay(dataProvider, view.getSessionList());
	}
	
	private void fetchData(final int start) {
		// show loading widget till row data is set
		view.getSessionList().setVisibleRangeAndClearData(view.getSessionList().getVisibleRange(), false);
		view.getSessionList().setRowCount(1);

		fetchRequestId++;
		final long fetchId = fetchRequestId;
		
		// a small delay if user quickly selects more items
		//
		int delayInMillis = 1000;
		new Timer() {
			@Override
			public void run() {
				
				if (fetchId != fetchRequestId) {
					return;
				}
				
				Receiver<Integer> countReceiver = new Receiver<Integer>() {
					@Override
					public void onSuccess(Integer response) {
						dataProvider.updateRowCount(response, true);
					}
				};
				
				Receiver<List<TrainingSessionProxy>> listReceiver = new Receiver<List<TrainingSessionProxy>>() {
					@Override
					public void onSuccess(List<TrainingSessionProxy> response) {
						dataProvider.updateRowData(start, response);
					}
					
					@Override
					public void onFailure(ServerFailure error) {
						Window.alert("Uups... Da ist wohl etwas schiefgelaufen. Versuche es mal mit dem neu Laden der Seite.");
						super.onFailure(error);
					}
				};
				
				String[] path = new String[4];
				path[0] = "owner";
				path[1] = "teams";
				path[2] = "subjects";
				path[3] = "statistic";
				
				final TrainingSessionServiceRequest service = requestFactory.trainingSessionServiceRequest();
				service.countPublicSessionsByAttributes(teamsFilter, subjectsFilter).to(countReceiver);
				service.findPublicSessionsByAttributes(teamsFilter, subjectsFilter, view.getSelectedSortByItem(), start, view.getVisibleItemLimit()).with(path).to(listReceiver);
				service.fire();
			}
		}.schedule(delayInMillis);
	}

}
