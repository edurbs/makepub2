package com.github.edurbs.makepub2.infra.views.conversor;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

import com.github.edurbs.makepub2.app.domain.EpubFile;
import com.github.edurbs.makepub2.app.usecase.epub.EpubCreator;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("Conversor")
@Route("")
public class ConversorView extends VerticalLayout {

    private transient EpubCreator epubCreator;

    private TextArea textArea = new TextArea();
    private TextField fieldTitle = new TextField();
    private TextField fieldPeriod = new TextField();
    private TextField fieldStudyNumber = new TextField();
    private ProgressBar progressBar = new ProgressBar();
    private Span progressBarLabelValue = new Span();
    private NativeLabel progressBarLabelText = new NativeLabel();
    private HorizontalLayout progressBarLabel = new HorizontalLayout(
            progressBarLabelText, progressBarLabelValue);

    private HorizontalLayout buttonLayout;

    private Button startButton = new Button("Gerar EPUB");

    public ConversorView(EpubCreator epubCreator) {
        this.epubCreator = epubCreator;
        setSpacing(false);

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setMaxWidth(900, Unit.PIXELS);

        H3 header = new H3("Conversor de texto com markups para EPUB 3");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setSizeFull();
        headerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        headerLayout.add(header);
        layout.add(headerLayout);

        textArea.setWidth("90%");
        textArea.setMinHeight("150px");
        textArea.setMaxHeight("200px");
        textArea.setLabel("Texto com markup");
        textArea.setPlaceholder("Cole aqui o texto com os markups");
        textArea.setClearButtonVisible(true);
        textArea.setValue("");

        fieldTitle.setLabel("Título");
        fieldTitle.setPlaceholder("Digite o título do estudo");
        fieldTitle.setClearButtonVisible(true);
        fieldTitle.setWidth("90%");

        fieldPeriod.setLabel("Período");
        fieldPeriod.setPlaceholder("Digite o dia inicial e o final da semana do estudo");
        fieldPeriod.setClearButtonVisible(true);
        fieldPeriod.setWidth("90%");

        fieldStudyNumber.setLabel("Estudo");
        fieldStudyNumber.setPlaceholder("Digite o número do estudo");
        fieldStudyNumber.setClearButtonVisible(true);
        fieldStudyNumber.setWidth("90%");

        FormLayout formLayout = new FormLayout(textArea, fieldTitle, fieldPeriod, fieldStudyNumber);
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("450", 2));
        formLayout.setColspan(fieldTitle, 2);
        formLayout.setColspan(textArea, 2);

        layout.add(formLayout);

        startButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        startButton.setDisableOnClick(true);
        startButton.addClickListener(this::onClickStartButton);

        Button cleanButton = new Button("Limpar");
        cleanButton.addClickListener(this::onClickCleanButton);

        buttonLayout = new HorizontalLayout(startButton, cleanButton);
        layout.add(buttonLayout);

        progressBar.addThemeVariants(ProgressBarVariant.LUMO_CONTRAST);
        progressBarLabel.setJustifyContentMode(JustifyContentMode.BETWEEN);
        layout.add(progressBarLabel, progressBar);
        setProgressBarVisibility(false);

        add(layout);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    private void setProgressBarVisibility(boolean visible) {
        progressBarLabel.setVisible(visible);
        progressBar.setVisible(visible);
    }

    @Async
    public void onClickStartButton(ClickEvent<Button> event) {
        setProgressBarVisibility(true);
        String text = textArea.getValue();
        String title = fieldTitle.getValue();
        String period = fieldPeriod.getValue();
        String studyNumber = fieldStudyNumber.getValue();

        var ui = UI.getCurrent();

        CompletableFuture.supplyAsync(() -> epubCreator.execute(
                ui.accessLater(this::updateProgressBar, null),
                ui.accessLater(this::onJobFailed, null),
                text, title, period, studyNumber)).thenAccept(ui.accessLater(this::onJobCompleted, null));

    }

    
    public void handleEpubFile(EpubFile epubFile) {
        if (epubFile == null) {
            onJobFailed(new Exception("Erro na conversão."));
            return;
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "file_" + timestamp + ".epub";
        StreamResource resource = new StreamResource(filename, () -> new ByteArrayInputStream(epubFile.content()));
        Anchor anchor = new Anchor(resource, "");
        anchor.getElement().setAttribute("download", true);
        anchor.getElement().getStyle().set("display", "none");
        anchor.setText("Download");
        anchor.setHref(resource);
        Button downloadButton = new Button("Download");
        downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_WARNING);
        downloadButton.addClickListener(event -> {
            anchor.getElement().callJsFunction("click");
            buttonLayout.remove(downloadButton);
        });

        buttonLayout.add(anchor, downloadButton);
    }

    private void updateProgressBar(String[] message) {
        Double status = Double.parseDouble(message[0]);
        progressBar.setValue(status);
        progressBarLabelText.setText(message[1]);

    }

    private void onClickCleanButton(ClickEvent<Button> event) {
        textArea.clear();
        fieldTitle.clear();
        fieldPeriod.clear();
        fieldStudyNumber.clear();
    }

    private void onJobCompleted(EpubFile epubFile) {
        Notification.show("Finalizado!!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        setProgressBarVisibility(false);
        startButton.setEnabled(true);
        handleEpubFile(epubFile);
    }

    private void onJobFailed(Exception error) {
        Notification.show(error.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
        setProgressBarVisibility(false);
        startButton.setEnabled(true);
    }

}
