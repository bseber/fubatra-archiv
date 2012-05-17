package de.fubatra.archiv.client.util;

import java.util.Iterator;
import java.util.List;

import de.fubatra.archiv.shared.domain.Team;
import de.fubatra.archiv.shared.domain.TrainingSessionSubject;

public class ListToStringUtil {
	
	public static String subjectListToString(List<TrainingSessionSubject> list) {
		return build(list, new ItemToString<TrainingSessionSubject>() {
			@Override
			public String getName(TrainingSessionSubject item) {
				return item.getName();
			}
		});
	}
	
	public static String teamListToString(List<Team> list) {
		return build(list, new ItemToString<Team>() {
			@Override
			public String getName(Team item) {
				return item == Team.NONE ? "" : item.name();
			}
		});
	}
	
	private static <T> String build(List<T> list, ItemToString<T> builder) {
		if (list == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<T> iterator = list.iterator();
		while (iterator.hasNext()) {
			T item = iterator.next();
			sb.append(builder.getName(item));
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	
	private interface ItemToString<T> {
		String getName(T item);
	}
	
	private ListToStringUtil() {
	}
	
}
