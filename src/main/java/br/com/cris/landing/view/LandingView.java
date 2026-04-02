package br.com.cris.landing.view;

import br.com.cris.landing.controller.LandingController;
import br.com.cris.landing.model.ContentData;
import br.com.cris.landing.util.Animations;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LandingView {

    private final LandingController controller;
    private final List<Region> secoesAnimadas = new ArrayList<>();

    public LandingView(LandingController controller) {
        this.controller = controller;
    }

    public Parent create() {
        VBox container = new VBox(26);
        container.getStyleClass().add("page-root");
        container.setPadding(new Insets(22, 42, 32, 42));

        Region hero = criarHero();
        Region sobre = criarSecaoTexto("SOBRE", ContentData.SOBRE, "card sobre-card");
        Region qualificacoes = criarSecaoProfissionalDetalhada();
        Region problema = criarSecaoTexto("O PROBLEMA", ContentData.PROBLEMA, "card problema-card");
        Region solucao = criarSecaoTexto("A SOLUÇÃO", ContentData.SOLUCAO, "card solucao-card");
        Region beneficios = criarBeneficios();
        Region contato = criarContato();
        Region conversao = criarConversaoFinal();

        container.getChildren().addAll(hero, sobre, qualificacoes, problema, solucao, beneficios, contato, conversao);

        ScrollPane scroll = new ScrollPane(container);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.getStyleClass().add("main-scroll");

        StackPane root = new StackPane();
        root.getStyleClass().add("app-shell");

        criarCamadaVideo(root).ifPresent(root.getChildren()::add);
        root.getChildren().add(scroll);
        return root;
    }

    private Optional<StackPane> criarCamadaVideo(StackPane root) {
        Optional<String> videoPath = resolverVideoDeFundo();
        if (videoPath.isEmpty()) {
            return Optional.empty();
        }

        try {
            Media media = new Media(Path.of(videoPath.get()).toUri().toString());
            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setAutoPlay(true);
            player.setMute(true);
            player.setVolume(0);

            MediaView mediaView = new MediaView(player);
            mediaView.setPreserveRatio(false);
            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());
            mediaView.setOpacity(0.30);

            Rectangle overlay = new Rectangle();
            overlay.widthProperty().bind(root.widthProperty());
            overlay.heightProperty().bind(root.heightProperty());
            overlay.setFill(Color.rgb(6, 8, 14, 0.58));

            StackPane background = new StackPane(mediaView, overlay);
            background.setMouseTransparent(true);
            return Optional.of(background);
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    private Optional<String> resolverVideoDeFundo() {
        for (String candidato : ContentData.VIDEO_BACKGROUND_CANDIDATOS) {
            Path path = Path.of(candidato);
            if (Files.exists(path) && Files.isRegularFile(path)) {
                return Optional.of(candidato);
            }
        }
        return Optional.empty();
    }

    public void playEntranceAnimations() {
        SequentialTransition sequencia = new SequentialTransition();
        int i = 0;
        for (Region secao : secoesAnimadas) {
            Animation anim = Animations.criarEntrada(secao, Duration.millis(i * 90L));
            sequencia.getChildren().add(anim);
            i++;
        }
        sequencia.play();
    }

    private Region criarHero() {
        VBox hero = new VBox(12);
        hero.getStyleClass().addAll("card", "hero");
        hero.setPadding(new Insets(36));

        Label badge = new Label("ATENDIMENTO PREMIUM PARA REGULARIZAÇÃO SIRPWEB");
        badge.getStyleClass().add("badge");

        Label titulo = new Label(ContentData.TITULO);
        titulo.getStyleClass().add("hero-title");

        Label frase = new Label(ContentData.FRASE_IMPACTO);
        frase.getStyleClass().add("hero-phrase");

        Label subtitulo = new Label(ContentData.SUBTITULO);
        subtitulo.getStyleClass().add("hero-subtitle");
        subtitulo.setWrapText(true);

        HBox botoes = new HBox(12);
        Button btnWhats = new Button("FALAR AGORA NO WHATSAPP");
        btnWhats.getStyleClass().addAll("cta", "cta-primary");
        btnWhats.setOnAction(e -> controller.abrirWhatsApp());

        Button btnEmail = new Button("ENVIAR EMAIL");
        btnEmail.getStyleClass().addAll("cta", "cta-ghost");
        btnEmail.setOnAction(e -> controller.abrirEmail());

        Animations.aplicarHoverElegante(btnWhats);
        Animations.aplicarHoverElegante(btnEmail);

        botoes.getChildren().addAll(btnWhats, btnEmail);

        FlowPane destaques = new FlowPane();
        destaques.setHgap(10);
        destaques.setVgap(10);
        destaques.getChildren().addAll(
            miniStat("Autoridade", "Especialista em regularização"),
            miniStat("Confiança", "Processo guiado sem incertezas"),
            miniStat("Agilidade", "Redução real de retrabalho")
        );

        hero.getChildren().addAll(badge, titulo, frase, subtitulo, botoes, destaques);
        secoesAnimadas.add(hero);
        return hero;
    }

    private VBox miniStat(String titulo, String desc) {
        VBox box = new VBox(4);
        box.getStyleClass().add("mini-stat");
        box.setPadding(new Insets(12));

        Label t = new Label(titulo);
        t.getStyleClass().add("mini-stat-title");

        Label d = new Label(desc);
        d.getStyleClass().add("mini-stat-desc");
        d.setWrapText(true);

        box.getChildren().addAll(t, d);
        return box;
    }

    private Region criarSecaoTexto(String tituloSecao, String texto, String styleClasses) {
        VBox box = new VBox(10);
        for (String styleClass : styleClasses.split(" ")) {
            box.getStyleClass().add(styleClass);
        }
        box.setPadding(new Insets(28));

        Label titulo = new Label(tituloSecao);
        titulo.getStyleClass().add("section-title");

        Label descricao = new Label(texto);
        descricao.getStyleClass().add("section-text");
        descricao.setWrapText(true);

        box.getChildren().addAll(titulo, descricao);
        Animations.aplicarHoverElegante(box);
        secoesAnimadas.add(box);
        return box;
    }

    private Region criarBeneficios() {
        VBox box = new VBox(14);
        box.getStyleClass().addAll("card", "beneficios-card");
        box.setPadding(new Insets(28));

        Label titulo = new Label("BENEFÍCIOS DIRETOS");
        titulo.getStyleClass().add("section-title");

        VBox lista = new VBox(10);
        for (String beneficio : ContentData.BENEFICIOS) {
            HBox item = new HBox(10);
            item.setAlignment(Pos.TOP_LEFT);
            Label bullet = new Label("+ ");
            bullet.getStyleClass().add("benefit-bullet");
            Label texto = new Label(beneficio);
            texto.getStyleClass().add("benefit-text");
            texto.setWrapText(true);
            item.getChildren().addAll(bullet, texto);
            lista.getChildren().add(item);
        }

        box.getChildren().addAll(titulo, lista);
        Animations.aplicarHoverElegante(box);
        secoesAnimadas.add(box);
        return box;
    }

    private Region criarSecaoProfissionalDetalhada() {
        VBox box = new VBox(16);
        box.getStyleClass().addAll("card", "qualificacoes-card");
        box.setPadding(new Insets(28));

        Label titulo = new Label("FORMAÇÃO, CURSOS E QUALIFICAÇÕES PROFISSIONAIS");
        titulo.getStyleClass().add("section-title");
        titulo.setWrapText(true);

        VBox formacao = criarBlocoTexto(ContentData.FORMACAO_TITULO, ContentData.FORMACAO_DESCRICAO, "bloco-formacao");
        VBox cursos = criarBlocoCursos();
        VBox qualificacoes = criarBlocoLista(ContentData.QUALIFICACOES_TITULO, ContentData.QUALIFICACOES);
        VBox tecnologia = criarBlocoTecnologia();
        VBox ia = criarBlocoSistemaIa();
        VBox posicionamento = criarBlocoTexto("POSICIONAMENTO FINAL", ContentData.POSICIONAMENTO_FINAL, "bloco-posicionamento");

        box.getChildren().addAll(titulo, formacao, cursos, qualificacoes, tecnologia, ia, posicionamento);
        Animations.aplicarHoverElegante(box);
        secoesAnimadas.add(box);
        return box;
    }

    private VBox criarBlocoTexto(String tituloBloco, String textoBloco, String styleClass) {
        VBox bloco = new VBox(8);
        bloco.getStyleClass().addAll("subcard", styleClass);
        bloco.setPadding(new Insets(18));

        Label titulo = new Label(tituloBloco);
        titulo.getStyleClass().add("subcard-title");

        Label texto = new Label(textoBloco);
        texto.getStyleClass().add("section-text");
        texto.setWrapText(true);

        bloco.getChildren().addAll(titulo, texto);
        return bloco;
    }

    private VBox criarBlocoCursos() {
        VBox bloco = new VBox(10);
        bloco.getStyleClass().addAll("subcard", "bloco-cursos");
        bloco.setPadding(new Insets(18));

        Label titulo = new Label(ContentData.CURSOS_TITULO);
        titulo.getStyleClass().add("subcard-title");

        FlowPane grade = new FlowPane();
        grade.setHgap(10);
        grade.setVgap(10);

        for (String curso : ContentData.CURSOS) {
            Label chip = new Label(curso);
            chip.getStyleClass().add("curso-chip");
            chip.setWrapText(true);
            grade.getChildren().add(chip);
        }

        bloco.getChildren().addAll(titulo, grade);
        return bloco;
    }

    private VBox criarBlocoLista(String tituloBloco, String[] itens) {
        VBox bloco = new VBox(8);
        bloco.getStyleClass().addAll("subcard", "bloco-lista");
        bloco.setPadding(new Insets(18));

        Label titulo = new Label(tituloBloco);
        titulo.getStyleClass().add("subcard-title");

        VBox lista = new VBox(8);
        for (String itemTexto : itens) {
            HBox item = new HBox(10);
            item.setAlignment(Pos.TOP_LEFT);
            Label bullet = new Label("◆");
            bullet.getStyleClass().add("qualificacao-bullet");
            Label texto = new Label(itemTexto);
            texto.getStyleClass().add("benefit-text");
            texto.setWrapText(true);
            item.getChildren().addAll(bullet, texto);
            lista.getChildren().add(item);
        }

        bloco.getChildren().addAll(titulo, lista);
        return bloco;
    }

    private VBox criarBlocoTecnologia() {
        VBox bloco = new VBox(10);
        bloco.getStyleClass().addAll("subcard", "bloco-tecnologia");
        bloco.setPadding(new Insets(18));

        Label titulo = new Label(ContentData.TECNOLOGIA_TITULO);
        titulo.getStyleClass().add("subcard-title");

        Label destaque = new Label(ContentData.TECNOLOGIA_DESTAQUE);
        destaque.getStyleClass().add("tech-highlight");
        destaque.setWrapText(true);

        VBox lista = new VBox(8);
        for (String itemTexto : ContentData.TECNOLOGIA_ITENS) {
            HBox item = new HBox(10);
            item.setAlignment(Pos.TOP_LEFT);
            Label bullet = new Label("+");
            bullet.getStyleClass().add("benefit-bullet");
            Label texto = new Label(itemTexto);
            texto.getStyleClass().add("benefit-text");
            texto.setWrapText(true);
            item.getChildren().addAll(bullet, texto);
            lista.getChildren().add(item);
        }

        bloco.getChildren().addAll(titulo, destaque, lista);
        return bloco;
    }

    private VBox criarBlocoSistemaIa() {
        VBox bloco = new VBox(10);
        bloco.getStyleClass().addAll("subcard", "bloco-ia");
        bloco.setPadding(new Insets(18));

        Label titulo = new Label(ContentData.SISTEMA_IA_TITULO);
        titulo.getStyleClass().add("subcard-title");

        Label descricao = new Label(ContentData.SISTEMA_IA_DESCRICAO);
        descricao.getStyleClass().add("section-text");
        descricao.setWrapText(true);

        VBox listaIa = new VBox(8);
        for (String ia : ContentData.IAS) {
            Label linhaIa = new Label(ia);
            linhaIa.getStyleClass().add("ia-item");
            linhaIa.setWrapText(true);
            listaIa.getChildren().add(linhaIa);
        }

        Label ganhosTitulo = new Label("RESULTADOS ENTREGUES");
        ganhosTitulo.getStyleClass().add("subcard-mini-title");

        FlowPane ganhos = new FlowPane();
        ganhos.setHgap(10);
        ganhos.setVgap(10);
        for (String ganho : ContentData.GANHOS_SISTEMA) {
            Label pill = new Label(ganho);
            pill.getStyleClass().add("ia-gain-pill");
            ganhos.getChildren().add(pill);
        }

        bloco.getChildren().addAll(titulo, descricao, listaIa, ganhosTitulo, ganhos);
        return bloco;
    }

    private Region criarContato() {
        VBox box = new VBox(12);
        box.getStyleClass().addAll("card", "contato-card");
        box.setPadding(new Insets(30));

        Label titulo = new Label("CONTATO DIRETO");
        titulo.getStyleClass().add("section-title");

        Label telefone = new Label("Telefone: " + ContentData.TELEFONE);
        telefone.getStyleClass().add("contato-info");

        Label email = new Label("Email: " + ContentData.EMAIL);
        email.getStyleClass().add("contato-info");

        Button botao = new Button("FALAR AGORA NO WHATSAPP");
        botao.getStyleClass().addAll("cta", "cta-primary", "cta-large");
        botao.setOnAction(e -> controller.abrirWhatsApp());
        Animations.aplicarHoverElegante(botao);

        box.getChildren().addAll(titulo, telefone, email, botao);
        Animations.aplicarHoverElegante(box);
        secoesAnimadas.add(box);
        return box;
    }

    private Region criarConversaoFinal() {
        VBox box = new VBox();
        box.getStyleClass().addAll("card", "final-card");
        box.setPadding(new Insets(28));

        Label frase = new Label(ContentData.FRASE_FINAL);
        frase.getStyleClass().add("final-text");
        frase.setWrapText(true);

        box.getChildren().add(frase);
        secoesAnimadas.add(box);
        return box;
    }
}
