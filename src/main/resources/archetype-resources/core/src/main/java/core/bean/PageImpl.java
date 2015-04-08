#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.bean;

import java.util.Collections;
import java.util.List;
import org.lambico.dao.generic.Page;

/**
 * Bean for managing list paginations.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
public class PageImpl<T> implements Page<T> {
    /**
     * The content of the page.
     */
    private List<T> results;
    /**
     * The size of the page, i.e. the max number of rows in a single page.
     */
    private int pageSize;
    /**
     * The index of the current page.
     */
    private int page;
    /**
     * The total rows count.
     */
    private int rowCount;
    /**
     * An empty page.
     */
    @SuppressWarnings("unchecked")
    public static final Page EMPTY =
            new PageImpl(Collections.EMPTY_LIST, 1, 1, 0);

    public PageImpl() {
    }
    
    /**
     * The constructor of a page.
     *
     * @param list The page content.
     * @param page The index of the page.
     * @param pageSize The size of the page.
     * @param rowCount The total rows count.
     */
    public PageImpl(final List<T> list, final int page,
            final int pageSize, final int rowCount) {
        this.page = page;
        this.pageSize = pageSize;
        this.rowCount = rowCount;
        results = list;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getPage() {
        return page;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isNextPage() {
        return page < getLastPage();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isPreviousPage() {
        return page > 1;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public List<T> getList() {
        return results;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getLastPage() {
        int lastPage = rowCount / pageSize;
        if (rowCount % pageSize > 0) {
            lastPage++;
        }
        return lastPage;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        return rowCount;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(
            List<T> results) {
        this.results = results;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    
}
