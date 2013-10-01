#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.rest;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import ${package}.core.bean.StringResult;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import org.hibernate.criterion.DetachedCriteria;
import org.lambico.dao.generic.GenericDao;
import org.lambico.dao.generic.Page;
import org.lambico.dao.generic.PageDefaultImpl;
import org.lambico.dao.spring.hibernate.HibernateGenericDao;
import ${package}.core.bean.ItemResult;
import ${package}.core.bean.PaginatedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.transaction.annotation.Transactional;

/**
 * Some sample REST services.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
@Path("/abstractRestService") // Override this!
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
public class AbstractCrudRestService<ENTITY, PK extends Serializable, DAO extends GenericDao<ENTITY, PK>> implements CrudRestService<ENTITY> {

    private static final Logger log = LoggerFactory.getLogger(AbstractCrudRestService.class);
    @Resource
    private DAO dao;

    @GET
    @Path(value = "/")
    @Produces({"application/json", "application/xml"})
    @Consumes({"application/json", "application/xml"})
    @Override
    public PaginatedResult<ENTITY> getItems(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize) {
        log.debug("Calling getItems.");
        PaginatedResult<ENTITY> result = new PaginatedResult<ENTITY>();
        try {
            Page<ENTITY> persons = (Page<ENTITY>) doRetrievePaginatedItems(page, pageSize);
            result.setItems(persons.getList());
            result.setPage(persons.getPage());
            result.setRowCount(persons.getRowCount());
            result.setLastPage(persons.getLastPage());
            result.setNextPage(persons.isNextPage());
            result.setPreviousPage(persons.isPreviousPage());
        } catch (Exception ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @GET
    @Path(value = "/{id}")
    @Override
    public ItemResult<ENTITY> getItem(@PathParam("id") String id) {
        log.debug("Calling getItem with {}.", id);
        PK idVal = fromStringToPk(id);
        ENTITY item = (ENTITY) dao.get(idVal);
        if (item == null) {
            ResponseBuilder builder =
                    Response.status(Status.BAD_REQUEST);
            builder.entity(new StringResult("Item Not Found"));
            throw new WebApplicationException(builder.build());
        } else {
            return new ItemResult<ENTITY>(item);
        }
    }

    /**
     * Delete an item.
     *
     * @param id The identifier of the item.
     * @return
     */
    @DELETE
    @Path(value = "/{id}")
    @Override
    public Response deleteItem(@PathParam("id") String id) {
        log.debug("Calling deleteItem with {}.", id);
        PK idVal = fromStringToPk(id);
        ENTITY item = dao.get(idVal);
        if (item == null) {
            ResponseBuilder builder =
                    Response.status(Status.BAD_REQUEST);
            builder.entity(new StringResult("Item Not Found"));
            throw new WebApplicationException(builder.build());
        } else {
            dao.delete(item);
        }
        return Response.ok().build();
    }

    /**
     * Add an item.
     *
     * @param item The item data.
     * @return
     */
    @POST
    @Path(value = "/")
    @Override
    public Response addItem(ENTITY item) {
        log.info("Calling addItem for {}.", item);
        dao.store(item);
        return Response.ok().build();
    }

    /**
     * Update an item.
     *
     * @param item The item data.
     * @return
     */
    @PUT
    @Path(value = "/")
    @Override
    public Response updateItem(ENTITY item) {
        log.debug("Calling updateItem for {}.", item);
        dao.store(item);
        return Response.ok().build();
    }

    @Transactional(readOnly = true)
    protected Page<ENTITY> doRetrievePaginatedItems(Integer page, Integer pageSize) {
        log.debug("Executing doRetrievePaginatedItems.");
        DetachedCriteria crit = DetachedCriteria.forClass(dao.getType());
        Page<ENTITY> result;
        if (pageSize != null) {
            if (page == null) {
                page = new Integer(1);
            }
            result = ((HibernateGenericDao) dao).searchPaginatedByCriteria(page, pageSize, crit);
        } else {
            List items = ((HibernateGenericDao) dao).searchByCriteria(crit);
            result = new PageDefaultImpl<ENTITY>(items, new Integer(1), items.size(), items.size());
        }
        log.debug("Retrieved {} items.", result.getList().size());
        return result;
    }

    protected PK fromStringToPk(String id) {
        PK idVal = null;
        Class[] typeArguments = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractCrudRestService.class);
        Class pkArg = typeArguments[1];
        try {
            try {
                Constructor constructor = pkArg.getConstructor(String.class);
                idVal = (PK) constructor.newInstance(id);
            } catch (NoSuchMethodException ex) {
                try {
                    Method valueOfMethod = pkArg.getMethod("valueOf", String.class);
                    idVal = (PK) valueOfMethod.invoke(null, id);
                } catch (NoSuchMethodException ex1) {
                    Method fromStringMethod = pkArg.getMethod("fromString", String.class);
                    idVal = (PK) fromStringMethod.invoke(null, id);
                }
            }
        } catch (Exception exception) {
            ResponseBuilder builder =
                    Response.status(Status.BAD_REQUEST);
            builder.entity(new StringResult("Can't convert id parameter. Parameter class " + pkArg.getName() + " has no constructor, valueOf or fromString methods accepting a String parameter."));
            throw new WebApplicationException(exception, builder.build());
        }
        return idVal;
    }
}
