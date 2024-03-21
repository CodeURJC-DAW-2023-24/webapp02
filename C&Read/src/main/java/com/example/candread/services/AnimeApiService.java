package com.example.candread.services;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;

public class AnimeApiService {

    public static String obtenerTokenAcceso(String client_id, String client_secret) throws Exception {
        // URL de la API de AniList para obtener el token de acceso
        String url = "https://anilist.co/api/v2/oauth/token";

        // Parámetros de la solicitud
        Map<Object, Object> data = new HashMap<>();
        data.put("grant_type", "client_credentials");
        data.put("client_id", client_id);
        data.put("client_secret", client_secret);

        // Realizar la solicitud POST para obtener el token de acceso
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(mapToJson(data)))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        // Verificar si la solicitud fue exitosa
        if (response.statusCode() == 200) {
            // Extraer el token de acceso de la respuesta
            return response.body();
        } else {
            throw new RuntimeException("Error al obtener el token de acceso: " + response.body());
        }
    }

    // Método para convertir un mapa a JSON
    private static String mapToJson(Map<Object, Object> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
        }
        json.deleteCharAt(json.length() - 1); // Eliminar la última coma
        json.append("}");
        return json.toString();
    }
}
