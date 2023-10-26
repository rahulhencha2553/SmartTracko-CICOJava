package com.cico.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

class LocalDateTypeAdapter1 extends TypeAdapter<LocalDate> {
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");

	@Override
	public void write(JsonWriter out, LocalDate value) throws IOException {
		if (value != null) {
			out.value(formatter.format(value));
		} else {
			out.nullValue();
		}
	}

	@Override
	public LocalDate read(JsonReader in) throws IOException {
		if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		String dateStr = in.nextString();
		return LocalDate.parse(dateStr, formatter);
	}
}

class LocalDateTypeAdapter {
	public static void main(String[] args) {
         
	}

	public LocalDateTypeAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static LocalDate getParsedDate(String str) {
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter1()).create();
		return gson.fromJson(str, LocalDate.class);
	}
}
