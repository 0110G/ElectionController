package com.electionController.structures.deserializers;

import com.electionController.structures.Election;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ElectionDeserializer extends StdDeserializer<Election> {

    public ElectionDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Election deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        Election election = new Election();
        JsonToken currentToken = null;
        while((currentToken = parser.nextValue()) != null) {
            switch (currentToken) {
                case VALUE_STRING:
                    System.out.println(parser.getCurrentName() + "VAL_STRING");
                    break;
                case VALUE_EMBEDDED_OBJECT:
                    System.out.println(parser.getCurrentName() + "VAL_EMBEDDED_OBJ");
                    break;
                default:
                    System.out.println(parser.getCurrentName() + "DEFAULT");
            }
        }
        return null;
    }
}
