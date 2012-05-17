package de.fubatra.archiv.shared.domain;

public enum Rating {

	// --------------------------------------------------------------
	// DO NOT CHANGE ORDER!!
	// --------------------------------------------------------------
	// gae checks enums with it's name instead of ordinal...
	// so a list ordered by rating would be
	// - AWESOME
	// - BAD
	// - GOOD
	// - NONE
	// - SUPER
	// workaround: save the ordinal in database and get enum with Rating.values[ordinal] 
	
	NONE
	, BAD
	, GOOD
	, SUPER
	, AWESOME

}
