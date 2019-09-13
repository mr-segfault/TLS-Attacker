/**
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.tlsattacker.core.workflow.action;

import de.rub.nds.tlsattacker.core.constants.CipherSuite;
import de.rub.nds.tlsattacker.core.constants.RunningModeType;
import de.rub.nds.tlsattacker.core.config.Config;
import de.rub.nds.tlsattacker.core.protocol.message.DHClientKeyExchangeMessage;
import de.rub.nds.tlsattacker.core.state.State;
import de.rub.nds.tlsattacker.core.state.TlsContext;
import de.rub.nds.tlsattacker.core.exceptions.WorkflowExecutionException;
import de.rub.nds.tlsattacker.core.record.layer.TlsRecordLayer;
import de.rub.nds.tlsattacker.core.record.AbstractRecord;

import de.rub.nds.tlsattacker.core.workflow.WorkflowTrace;
import de.rub.nds.tlsattacker.core.workflow.action.SendDynamicClientKeyExchangeAction;

import de.rub.nds.tlsattacker.core.workflow.factory.WorkflowConfigurationFactory;
import de.rub.nds.tlsattacker.core.workflow.factory.WorkflowTraceType;

import de.rub.nds.tlsattacker.core.unittest.helper.FakeTransportHandler;

import de.rub.nds.tlsattacker.transport.ConnectionEndType;
import de.rub.nds.tlsattacker.util.tests.SlowTests;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 *
 * @author mario
 */
public class SendDynamicClientKeyExchangeActionTest {

    private State state;
    private TlsContext tlsContext;
    private Config config;

    private SendDynamicClientKeyExchangeAction action;

    @Before
    public void setUp() {
        action = new SendDynamicClientKeyExchangeAction();

        // create Config to not overly rely on default values (aka. make sure
        // we're client)
        config = Config.createConfig();
        // This isn't a typo on my side, but a "bug" in the code
        config.setDefaulRunningMode(RunningModeType.CLIENT);

        WorkflowTrace trace = new WorkflowTrace();
        trace.addTlsAction(action);
        state = new State(config, trace);

        tlsContext = state.getTlsContext();
        tlsContext.setSelectedCipherSuite(CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA);
        tlsContext.setRecordLayer(new TlsRecordLayer(tlsContext));
        tlsContext.setTransportHandler(new FakeTransportHandler(ConnectionEndType.CLIENT));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testExecute() {
        action.execute(state);
        assertTrue(action.executedAsPlanned());
        assertTrue(action.isExecuted());
        try {
            action.execute(state);
            assertTrue(false); // Code shouldn't be reached at all
        } catch (WorkflowExecutionException e) {
            assertTrue(e.getMessage().equals("Action already executed!"));
        } catch (Exception e) {
            // any other Exception than what should have been thrown appeared
            assertTrue(false);
        }
    }

    @Test
    public void testGetSendMessages() {
        assertTrue(action.getSendMessages() instanceof ArrayList && action.getSendMessages().isEmpty());
        action.execute(state);
        assertTrue(action.getSendMessages() instanceof ArrayList && action.getSendMessages().size() == 1
                && action.getSendMessages().get(0) instanceof DHClientKeyExchangeMessage);
    }

    @Test
    public void testGetSendRecords() {
        assertTrue(action.getSendRecords() instanceof ArrayList && action.getSendRecords().isEmpty());
        action.execute(state);
        assertTrue(action.getSendRecords() instanceof ArrayList && action.getSendRecords().size() == 1
                && action.getSendRecords().get(0) instanceof AbstractRecord);
    }

    @Test
    public void testSetRecords() {
        action.execute(state);
        List<AbstractRecord> expectedRecords = new ArrayList<>();
        action.setRecords(expectedRecords);
        assertTrue(action.getSendRecords().equals(expectedRecords));
    }

    @Test
    public void testReset() {
        assertFalse(action.isExecuted());
        action.execute(state);
        assertTrue(action.isExecuted());
        action.reset();
        assertFalse(action.isExecuted());
        action.execute(state);
        assertTrue(action.isExecuted());
    }

    @Test
    public void testEquals() {
        assertTrue(action.equals(action));
        assertFalse(action.equals(null));
        assertFalse(action.equals(new Object()));
    }

    @Test
    public void testToString() {
        // Should it really be Send Action? Or Send Dynamic Client Key Exchange
        // Action: ?
        assertTrue(action.toString().equals("Send Action: (not executed)\n\tMessages:\n"));
    }

    @Test
    public void testToCompactString() {
        assertTrue(action.toCompactString().equals("SendDynamicClientKeyExchangeAction [client] (no messages set)"));
    }
}