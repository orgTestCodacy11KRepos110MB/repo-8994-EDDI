package ai.labs.resources.impl.http.rest;

import ai.labs.persistence.IResourceStore;
import ai.labs.resources.impl.resources.rest.RestVersionInfo;
import ai.labs.resources.rest.documentdescriptor.IDocumentDescriptorStore;
import ai.labs.resources.rest.documentdescriptor.model.DocumentDescriptor;
import ai.labs.resources.rest.http.IHttpCallsStore;
import ai.labs.resources.rest.http.IRestHttpCallsStore;
import ai.labs.resources.rest.http.model.HttpCallsConfiguration;
import ai.labs.rest.restinterfaces.IRestInterfaceFactory;
import ai.labs.rest.restinterfaces.RestInterfaceFactory;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author ginccc
 */
@Slf4j
public class RestHttpCallsStore extends RestVersionInfo<HttpCallsConfiguration> implements IRestHttpCallsStore {
    private final IHttpCallsStore httpCallsStore;
    private IRestHttpCallsStore restHttpCallsStore;

    @Inject
    public RestHttpCallsStore(IHttpCallsStore httpCallsStore,
                              IRestInterfaceFactory restInterfaceFactory,
                              IDocumentDescriptorStore documentDescriptorStore) {
        super(resourceURI, httpCallsStore, documentDescriptorStore);
        this.httpCallsStore = httpCallsStore;
        initRestClient(restInterfaceFactory);
    }

    private void initRestClient(IRestInterfaceFactory restInterfaceFactory) {
        try {
            restHttpCallsStore = restInterfaceFactory.get(IRestHttpCallsStore.class);
        } catch (RestInterfaceFactory.RestInterfaceFactoryException e) {
            restHttpCallsStore = null;
            log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public List<DocumentDescriptor> readHttpCallsDescriptors(String filter, Integer index, Integer limit) {
        return readDescriptors("ai.labs.httpcalls", filter, index, limit);
    }

    @Override
    public HttpCallsConfiguration readHttpCalls(String id, Integer version) {
        return read(id, version);
    }

    @Override
    public Response updateHttpCalls(String id, Integer version, HttpCallsConfiguration httpCallsConfiguration) {
        return update(id, version, httpCallsConfiguration);
    }

    @Override
    public Response createHttpCalls(HttpCallsConfiguration httpCallsConfiguration) {
        return create(httpCallsConfiguration);
    }

    @Override
    public Response deleteHttpCalls(String id, Integer version) {
        return delete(id, version);
    }

    @Override
    public Response duplicateResource(String id, Integer version) {
        validateParameters(id, version);
        HttpCallsConfiguration httpCallsConfiguration = restHttpCallsStore.readHttpCalls(id, version);
        return restHttpCallsStore.createHttpCalls(httpCallsConfiguration);
    }

    @Override
    protected IResourceStore.IResourceId getCurrentResourceId(String id) throws IResourceStore.ResourceNotFoundException {
        return httpCallsStore.getCurrentResourceId(id);
    }
}
