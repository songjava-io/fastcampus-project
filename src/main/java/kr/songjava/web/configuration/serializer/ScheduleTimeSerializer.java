package kr.songjava.web.configuration.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import kr.songjava.web.domain.ScheduleTime;

import java.io.IOException;

public class ScheduleTimeSerializer extends StdSerializer<ScheduleTime> {

    public ScheduleTimeSerializer() {
        super(ScheduleTime.class);
    }


    public void serialize(
            ScheduleTime scheduleTime, JsonGenerator generator, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName("name");
        generator.writeString(scheduleTime.name());
        generator.writeFieldName("label");
        generator.writeString(scheduleTime.label());
        generator.writeEndObject();
    }
}
