package de.fubatra.archiv.shared.domain;

public enum TrainingSessionSubject {
	
	BALLCONTROL("Ballkontrolle")
	, DRIBBLING("Dribbling")
	, FEINTS("Finten")
	, HEADER("Kopfball")
	, PASSING("Passspiel")
	, SHOOTING("Torschuss")
	, OFF_INDIVIDUAL("Angriff Individualtaktik")
	, OFF_GROUP("Angriff Gruppentaktik")
	, OFF_TEAM("Angriff Mannschaftstaktik")
	, DEF_INDIVIDUAL("Abwehr Individualtaktik")
	, DEF_GROUP("Abwehr Gruppentaktik")
	, DEF_TEAM("Abwehr Mannschaftstaktik")
	, STAMINA("Ausdauer")
	, COORDINATION("Koordination")
	, POWER("Kraft")
	, SPEED("Schnelligkeit");
	
	private String name;
	
	TrainingSessionSubject(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

}
