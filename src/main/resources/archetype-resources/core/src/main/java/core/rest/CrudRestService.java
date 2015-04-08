#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.rest;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import ${package}.core.bean.ItemResult;
import ${package}.core.bean.PaginatedResult;

/**
 * The interface for crud services.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
public interface CrudRestService<ENTITY> {

    /**
     * Add an item.
     *
     * @param item The item data.
     * @return
     */
    Response addItem(ENTITY item);

    /**
     * Delete an item.
     *
     * @param id The identifier of the item.
     * @return
     */
    Response deleteItem(@PathParam(value = "id") String id);

    ItemResult<ENTITY> getItem(@PathParam(value = "id") String id);

    PaginatedResult<ENTITY> getItems(@QueryParam(value = "page") Integer page, @QueryParam(value = "pageSize") Integer pageSize);

    /**
     * Update an item.
     *
     * @param item The item data.
     * @return
     */
    Response updateItem(ENTITY item);
    
}
