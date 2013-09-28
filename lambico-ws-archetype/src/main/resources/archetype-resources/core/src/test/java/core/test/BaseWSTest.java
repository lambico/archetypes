#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Lambico Team <info@lambico.org>
 *
 * This file is part of Lambico WS Archetype Template - Core.
 */
package ${package}.core.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * A base class for web service tests.
 *
 * @author Lucio Benfante <lucio@benfante.com>
 */
@ContextConfiguration(locations = {"/${packageInPathFormat}/core/service/client-beans-test.xml"})
public abstract class BaseWSTest extends AbstractTransactionalJUnit4SpringContextTests {
}
