package kr.songjava.web.configuration.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import kr.songjava.web.domain.ScheduleType;

import java.io.IOException;

public class ScheduleTypeSerializer extends StdSerializer<ScheduleType> {

	public ScheduleTypeSerializer() {
		super(ScheduleType.class);
	}


	public void serialize(
					ScheduleType scheduleType, JsonGenerator generator, SerializerProvider provider)
					throws IOException, JsonProcessingException {
		generator.writeStartObject();
		generator.writeFieldName("name");
		generator.writeString(scheduleType.name());
		generator.writeFieldName("label");
		generator.writeString(scheduleType.label());
		generator.writeEndObject();
	}
}