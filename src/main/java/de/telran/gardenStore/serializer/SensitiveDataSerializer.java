package de.telran.gardenStore.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
public class SensitiveDataSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        String strValue = value.toString();
        // maskiruem polnostju esli net - visibleChars
        gen.writeString("***");

        // mozno chast simvolov ostavit, tak toze chasto delajut
        // if (strValue.length() > 2) {
        //     gen.writeString(strValue.charAt(0) + "***" + strValue.charAt(strValue.length()-1));
        // } else {
        //     gen.writeString("***");
        // }
    }
}