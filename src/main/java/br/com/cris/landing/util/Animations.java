package br.com.cris.landing.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

public final class Animations {

    private Animations() {
    }

    public static Animation criarEntrada(Node node, Duration delay) {
        node.setOpacity(0);
        node.setTranslateY(24);

        FadeTransition fade = new FadeTransition(Duration.millis(780), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(780), node);
        slide.setFromY(24);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition parallel = new ParallelTransition(fade, slide);
        parallel.setDelay(delay);
        return parallel;
    }

    public static void aplicarHoverElegante(Node node) {
        ScaleTransition enter = new ScaleTransition(Duration.millis(180), node);
        enter.setToX(1.015);
        enter.setToY(1.015);

        ScaleTransition exit = new ScaleTransition(Duration.millis(180), node);
        exit.setToX(1);
        exit.setToY(1);

        node.setOnMouseEntered(e -> enter.playFromStart());
        node.setOnMouseExited(e -> exit.playFromStart());
    }
}
