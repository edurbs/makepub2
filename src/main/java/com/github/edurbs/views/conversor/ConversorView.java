package com.github.edurbs.views.conversor;

import com.github.edurbs.app.usecase.TestService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("Conversor")
@Route("")
public class ConversorView extends VerticalLayout {

    private TextArea textArea = new TextArea();
    private TextField fieldTitle = new TextField();
    private TextField fieldPeriod = new TextField();
    private TextField fieldStudyNumber = new TextField();
    private ProgressBar progressBar = new ProgressBar();
    private Span progressBarLabelValue = new Span();
    private NativeLabel progressBarLabelText = new NativeLabel();
    private HorizontalLayout progressBarLabel = new HorizontalLayout(
    progressBarLabelText, progressBarLabelValue);

    public ConversorView() {
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

        Button startButton = new Button("Gerar EPUB");
        startButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        startButton.setDisableOnClick(true);
        startButton.addClickListener(this::onClickStartButton);
        
        Button cleanButton = new Button("Limpar");
        cleanButton.addClickListener(this::onClickCleanButton);

        HorizontalLayout buttonLayout = new HorizontalLayout(startButton, cleanButton);        
        layout.add(buttonLayout);

        progressBar.addThemeVariants(ProgressBarVariant.LUMO_CONTRAST);        
        progressBarLabel.setJustifyContentMode(JustifyContentMode.BETWEEN);        
        layout.add(progressBarLabel,progressBar);
        setProgressBarVisibility(false);
        add(layout);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    private void setProgressBarVisibility(boolean visible){
        progressBarLabel.setVisible(visible);
        progressBar.setVisible(visible);
    }

    private void onClickStartButton(ClickEvent<Button> event) {
        event.getSource().setEnabled(true);
        setProgressBarVisibility(true);
        var ui = UI.getCurrent();
        TestService ts = new TestService();
        ts.startBackgroundJob(
            ui.accessLater(this::onJobCompleted, null), 
            ui.accessLater(this::updateProgressBar, null),
            ui.accessLater(this::onJobFailed, null)
        );
    
    }

    private void updateProgressBar(String message[]) {
        Double status = Double.parseDouble(message[0]);
        progressBar.setValue(status);
        progressBarLabelValue.setText(status*100 + "%");
        progressBarLabelText.setText(message[1]);
        
    }
    private void onClickCleanButton(ClickEvent<Button> event) {
        textArea.clear();
        fieldTitle.clear();
        fieldPeriod.clear();
        fieldStudyNumber.clear();
    }

    private void onJobCompleted(String result) {
        Notification.show(result);
        setProgressBarVisibility(false);
    }

    private void onJobFailed(Exception error) {
        Notification.show(error.getMessage());
        setProgressBarVisibility(false);
    }

}
