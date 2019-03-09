package me.identityprovider.common.service;

import java.util.List;
import java.util.Optional;
import me.identityprovider.common.exception.AppCreationException;
import me.identityprovider.common.exception.NoSuchAppException;
import me.identityprovider.common.model.App;
import me.identityprovider.common.repository.AppRepository;

import me.identityprovider.common.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppService {

    private AppRepository appRepository;

    @Autowired
    public AppService(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    /**
     *
     * @param app
     * @return
     * @throws AppCreationException
     */
    public App save(App app) throws AppCreationException {

        boolean redirectValid = isRedirectValid(app.getLoginRedirect());
        if (!redirectValid) {
            throw new AppCreationException("The redirect url is invalid");
        }

        app.setSecret(SecurityUtil.appSecret());
        app.setId(SecurityUtil.appId());

        return appRepository.save(app);
    }

    /**
     * Find an app by its id.
     *
     * @param appId id of the app to find.
     * @return an {@link App} whose id matches the id supplied.
     * @throws NoSuchAppException if there is no app with the given id
     */
    public App read(String appId) throws NoSuchAppException {
        Optional<App> app = appRepository.findById(appId);
        if (!app.isPresent()) {
            throw new NoSuchAppException("App Id did not match any existing apps");
        }
        return app.get();
    }

    public Optional<List<App>> getAppsOf(String devId) {
        return Optional.of(appRepository.findByDevId(devId));
    }

    /**
     * Deletes all the apps of a developer with given id.
     * @param devId id of the developer whose apps to delete.
     */
    public void deleteAppsOf(String devId) {
        appRepository.deleteByDevId(devId);
    }

    /**
     * Delete app with given id.
     * @param id id of the app to delete.
     */
    public void delete(String id) {
        appRepository.deleteById(id);
    }

    /**
     * @return true if an app exists with the given id.
     */
    public boolean exists(String entityId) {
        return appRepository.existsById(entityId);
    }

    private boolean isRedirectValid(String url) {
        return true;
    }

}
