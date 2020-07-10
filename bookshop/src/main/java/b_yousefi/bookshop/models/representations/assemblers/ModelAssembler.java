package b_yousefi.bookshop.models.representations.assemblers;

import lombok.SneakyThrows;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Created by: b.yousefi
 * Date: 7/7/2020
 */
public abstract class ModelAssembler<T, D extends RepresentationModel<?>>
        extends RepresentationModelAssemblerSupport<T, D> {
    private RepositoryRestConfiguration config;

    public ModelAssembler(Class<?> controllerClass, Class<D> resourceType, RepositoryRestConfiguration config) {
        super(controllerClass, resourceType);
        this.config = config;
    }


    protected Link fixLinkSelf(Object invocationValue) {
        return fixLinkTo(invocationValue).withSelfRel();
    }

    @SneakyThrows
    protected Link fixLinkTo(Object invocationValue) {
        UriComponentsBuilder uriComponentsBuilder = linkTo(invocationValue).toUriComponentsBuilder();
        URL url = new URL(uriComponentsBuilder.toUriString());
        uriComponentsBuilder.replacePath(config.getBasePath() + url.getPath());
        return new Link(uriComponentsBuilder.toUriString());
    }
}
