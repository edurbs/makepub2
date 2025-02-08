package com.github.edurbs.makepub2.app.usecase;

import org.springframework.stereotype.Service;

import com.vaadin.flow.function.SerializableConsumer;

@Service
public class TestService {

    public void startBackgroundJob(SerializableConsumer<String> onJobCompleted, SerializableConsumer<String[]> progressBar,
            SerializableConsumer<Exception> onJobFailed) {
        
        new Thread(() -> {
            // fake set timeout for 1 second
            try {
                Thread.sleep(1000);
                progressBar.accept(new String[]{"0.1", "Iniciando..."}); 
                Thread.sleep(1000);
                progressBar.accept(new String[]{"0.3", "Fase 2..."}); 
                Thread.sleep(1000);   
                progressBar.accept(new String[]{"0.6", "Exportando..."}); 
                Thread.sleep(1000);   
                progressBar.accept(new String[]{"1.0", "Finalizado..."}); 
                onJobCompleted.accept("Finalizado com sucesso!!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }).start();
        
    }

}
