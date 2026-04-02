# Cris Seguranca JavaFX

Aplicacao JavaFX com visual premium para captacao de clientes de servicos de regularizacao profissional no SIRPWEB.

## Requisitos

- JDK 21
- Maven 3.9+

## Executar (1 clique no Windows)

- Clique duas vezes em `iniciar-app.bat`

## Executar via terminal

```powershell
Set-Location "C:\Users\Cris\Desktop\cris-seguranca-javafx"
mvn javafx:run
```

## Estrutura

- `src/main/java/br/com/cris/landing/MainApp.java`: inicializacao da aplicacao
- `src/main/java/br/com/cris/landing/view/LandingView.java`: composicao visual da landing
- `src/main/java/br/com/cris/landing/controller/LandingController.java`: acoes de contato
- `src/main/java/br/com/cris/landing/model/ContentData.java`: textos e dados institucionais
- `src/main/java/br/com/cris/landing/util/Animations.java`: animacoes e efeitos
- `src/main/resources/styles/app.css`: tema e identidade visual

## Observacoes de seguranca

- Aplicacao informativa, sem backend e sem banco de dados
- Nenhuma entrada externa e processada
- Links externos sao fixos e validados por esquema permitido
