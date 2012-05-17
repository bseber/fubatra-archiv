package de.fubatra.archiv.client.ioc;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

@History(type = HistoryConverterType.SIMPLE)
public class AppHistoryConverter implements HistoryConverter<AppEventBus> {
	
	public String convertToToken(String historyName) {
		return "";
	}
	
	public String convertToToken(String historyName, long id) {
		return "id=" + id;
	}
	@Override
	public void convertFromToken(String historyName, String param, AppEventBus eventBus) {
		if ("startPage".equals(historyName)) {
			eventBus.startPage();
		} else if ("createTrainingSession".equals(historyName)) {
			eventBus.createTrainingSession();
		} else if ("siteNotice".equals(historyName)) {
			eventBus.siteNotice();
		} else if ("login".equals(historyName)) {
			eventBus.login();
		} else if ("logout".equals(historyName)) {
			eventBus.logout();
		} else {
			Long id = parseId(param);
			if (id < 0) {
				return;
			}
			if ("showTrainingSession".equals(historyName)) {
				eventBus.showTrainingSession(id);
			} else if ("editTrainingSession".equals(historyName)) {
				eventBus.editTrainingSession(id);
			} else if ("profile".equals(historyName)) {
				eventBus.profile(id);
			} else if ("listTrainingSessionsOfUser".equals(historyName)) {
				eventBus.listTrainingSessionsOfUser(id);
			}
		}
	}

	@Override
	public boolean isCrawlable() {
		return true;
	}
	
	private long parseId(String requestQuery) {
		try {
			String[] params = requestQuery.split("&");
			for (String p : params) {
				String[] value = p.split("=");
				if ("id".equals(value[0])) {
					return Long.valueOf(value[1]);
				}
			}
			return -1;
		} catch (Exception e) {
			return -1;
		}
	}

}
