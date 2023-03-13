package kr.songjava.web.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import kr.songjava.web.configuration.serializer.ScheduleTypeSerializer;

@JsonSerialize(using = ScheduleTypeSerializer.class)
public enum ScheduleType {

	WORK("업무"),
	VACATION("휴가"),
	OUTSIDE_WORK("외근"),
	MEETING("회의"),
	LUNCH("점심"),
	DINING_TOGETHER("회식"),
	PARENTAL_LEAVE("육아휴직"),
	NIGHT_SHIFT("야근"),
	SELF_DEVELOPEMENT("자기개발")
	,
	;
	private String label;

	ScheduleType(String label) {
		this.label = label;
	}

	public String label() {
		return label;
	}
}
