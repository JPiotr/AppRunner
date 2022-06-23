package datio.apps.AppRunner.applications;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AppService {
    private final AppRepo appRepo;

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
