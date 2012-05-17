package de.fubatra.archiv.client.ui.view;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FlowPanel;

import de.fubatra.archiv.shared.domain.TrainingSessionPostProxy;

public class TrainingSessionPostListView extends FlowPanel implements IsEditor<ListEditor<TrainingSessionPostProxy, TrainingSessionPostView>> {

	public interface IPresenter extends TrainingSessionPostView.IPresenter {
		
		void onPostViewMouseOut(TrainingSessionPostView postView);
		
		void onPostViewMouseOver(TrainingSessionPostView postView);
	}

	private final IPresenter presenter;
	private ListEditor<TrainingSessionPostProxy, TrainingSessionPostView> editor = ListEditor.of(new MyEditorSource());
	
	public TrainingSessionPostListView(IPresenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public ListEditor<TrainingSessionPostProxy, TrainingSessionPostView> asEditor() {
		return editor;
	}

	class MyEditorSource extends EditorSource<TrainingSessionPostView> {
		
		@Override
		public TrainingSessionPostView create(final int index) {
			final TrainingSessionPostView postView = new TrainingSessionPostView(presenter);
			postView.addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					presenter.onPostViewMouseOut(postView);
				}
			});
			postView.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					presenter.onPostViewMouseOver(postView);
				}
			});
			TrainingSessionPostListView.this.insert(postView, 0);
			return postView;
		}
		
		@Override
		public void dispose(TrainingSessionPostView subEditor) {
			subEditor.removeFromParent();
		}
		
	}

}
