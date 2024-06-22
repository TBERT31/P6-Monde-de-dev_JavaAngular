package com.openclassrooms.mddapi.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

/**
 * Configuration pour l'utilisation de Gson dans l'application.
 */
@Configuration
public class GsonConfig {

    /**
     * Crée une instance de Gson avec un format de date spécifique.
     * @return l'instance de Gson.
     */
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
    }

    /**
     * Crée un convertisseur de messages HTTP Gson avec l'instance de Gson spécifiée.
     * @param gson l'instance de Gson à utiliser pour le convertisseur.
     * @return le convertisseur de messages HTTP Gson.
     */
    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter(Gson gson) {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson);
        return converter;
    }
}