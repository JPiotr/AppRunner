package datio.apps.AppRunner.applications;

import datio.apps.AppRunner.core.RpcService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AppService {
    @Autowired
    private final AppRepo appRepo;
    @Autowired
    private RpcService rs;

    @RabbitListener(queues = "type-collector")
    public void handleTypeApp(
            @Header(value = AmqpHeaders.REPLY_TO, required = false) String senderId,
            @Header(value = AmqpHeaders.CORRELATION_ID, required = false) String correlationId,
            String requestMessage
    ){
        if (senderId != null && correlationId != null) {
            App app = appRepo.findByName(requestMessage);
            log.info("Looking for requested app: " + requestMessage);
            if(app != null){
                log.info("App "+ requestMessage + " found");
                this.rs.sendResponse(senderId, correlationId, app.getId());
            }
            else{
                log.error("App "+ requestMessage + " not found");
                this.rs.sendResponse(senderId,correlationId, "null");
            }

        }
    }
    @RabbitListener(queues = "app-runner")
    public void runApp(
        @Header(value = AmqpHeaders.REPLY_TO, required = false) String senderId,
        @Header(value = AmqpHeaders.CORRELATION_ID, required = false) String correlationId,
        String requestMessage
    ) throws IOException {
        if (senderId != null && correlationId != null) {
            Optional<App> app = appRepo.findById(requestMessage);
            log.info("Trying to run requested app: " + requestMessage);
            if(app.isPresent()){
                Process pr = Runtime.getRuntime().exec(app.get().getLocalPath());
                log.info("App "+ app.get().getName() + " is running.");
                this.rs.sendResponse(senderId, correlationId, "running");
            }
            else {
                log.error("Cannot run app "+ requestMessage);
                this.rs.sendResponse(senderId, correlationId, "error");
            }
        }
    }

    public String getPath(String name){
        App app = appRepo.findByName(name);
        if(app == null) return null;
        log.info("Local application path for "+ name +" is "+ app.getLocalPath());
        return app.getLocalPath();
    }

    public void addPid(Long pid, String name){
        App app = appRepo.findByName(name);
        if(app == null)return;
        log.info("Saving pid for " + name);
        app.setPid(pid);
        appRepo.save(app);
    }

    public void addNewApp(BasicApp basicApp){
        App app = new App(basicApp.name, basicApp.path);
        log.info("Add new App to Database: "+app.getName());
        appRepo.save(app);
    }

    public boolean checkExists(String name){
        App check = appRepo.findByName(name);
        if(check != null){
            log.warn("App Entry " + check.getName() + " exists in database!");
            return true;
        }
        return false;
    }
    public boolean checkExistsSilent(String name){
        App check = appRepo.findByName(name);
        return check != null;
    }

    public void addNewApp(String name, String path){
        log.info("Add new App to Database: "+name);
        appRepo.save(new App(name,path));
    }
    public void addNewApps(Collection<BasicApp> apps){
        Collection<App> appsList = new ArrayList<>();
        for (BasicApp app:
             apps) {
            log.info("Add new App to List: "+app.getName());
            appsList.add(new App(app.name,app.path));
        }
        log.info("Add new Apps to Database");
        appRepo.saveAll(appsList);
    }
    public void deleteAppEntry(String name){
        App app = appRepo.findByName(name);
        if(app != null) {
            log.info("Deleting App entry " + app.getName());
            appRepo.delete(app);
        }
        else {
            log.error("Could not find App entry " + name);
        }
    }

    public List<App> getAllAps(){
        return appRepo.findAll();
    }

}
