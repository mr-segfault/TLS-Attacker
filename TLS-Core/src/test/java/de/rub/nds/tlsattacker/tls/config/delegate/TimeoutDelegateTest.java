/**
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2016 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.tlsattacker.tls.config.delegate;

import com.beust.jcommander.JCommander;
import de.rub.nds.tlsattacker.tls.workflow.TlsConfig;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Robert Merget - robert.merget@rub.de
 */
public class TimeoutDelegateTest {

    private TimeoutDelegate delegate;
    private JCommander jcommander;
    private String[] args;

    public TimeoutDelegateTest() {
    }

    @Before
    public void setUp() {
        this.delegate = new TimeoutDelegate();
        this.jcommander = new JCommander(delegate);
    }

    /**
     * Test of getTimeout method, of class TimeoutDelegate.
     */
    @Test
    public void testGetTimeout() {
        args = new String[2];
        args[0] = "-timeout";
        args[1] = "123";
        assertFalse(delegate.getTimeout() == 123);
        jcommander.parse(args);
        assertTrue(delegate.getTimeout() == 123);
    }

    /**
     * Test of setTimeout method, of class TimeoutDelegate.
     */
    @Test
    public void testSetTimeout() {
        assertFalse(delegate.getTimeout() == 123);
        delegate.setTimeout(123);
        assertTrue(delegate.getTimeout() == 123);
    }

    /**
     * Test of getTlsTimeout method, of class TimeoutDelegate.
     */
    @Test
    public void testGetTlsTimeout() {
        args = new String[2];
        args[0] = "-tls_timeout";
        args[1] = "123";
        assertFalse(delegate.getTlsTimeout() == 123);
        jcommander.parse(args);
        assertTrue(delegate.getTlsTimeout() == 123);
    }

    /**
     * Test of setTlsTimeout method, of class TimeoutDelegate.
     */
    @Test
    public void testSetTlsTimeout() {
        assertFalse(delegate.getTlsTimeout() == 123);
        delegate.setTlsTimeout(123);
        assertTrue(delegate.getTlsTimeout() == 123);
    }

    /**
     * Test of applyDelegate method, of class TimeoutDelegate.
     */
    @Test
    public void testApplyDelegate() {
        TlsConfig config = new TlsConfig();
        args = new String[4];
        args[0] = "-tls_timeout";
        args[1] = "123";
        args[2] = "-timeout";
        args[3] = "456";
        jcommander.parse(args);
        delegate.applyDelegate(config);
        assertTrue(config.getTimeout() == 456);
        assertTrue(config.getTlsTimeout() == 123);
    }
}
