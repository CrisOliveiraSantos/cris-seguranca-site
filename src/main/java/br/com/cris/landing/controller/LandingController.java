package br.com.cris.landing.controller;

import br.com.cris.landing.model.ContentData;
import javafx.application.HostServices;

import java.util.Set;

public class LandingController {

    private final HostServices hostServices;

    public LandingController(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void abrirWhatsApp() {
        abrirLinkSeguro(ContentData.WHATSAPP_URL);
    }

    public void abrirEmail() {
        abrirLinkSeguro("mailto:" + ContentData.EMAIL);
    }

    private void abrirLinkSeguro(String link) {
        // Somente esquemas esperados e definidos internamente.
        Set<String> esquemasPermitidos = Set.of("https://", "mailto:");
        boolean permitido = esquemasPermitidos.stream().anyMatch(link::startsWith);
        if (permitido) {
            hostServices.showDocument(link);
        }
    }
}
