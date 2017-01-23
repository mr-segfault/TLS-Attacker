/**
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2016 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package tlsattacker.fuzzer.exceptions;

/**
 * Exception thrown if the Fuzzer fails to find a free Server object.
 * 
 * @author ic0ns
 */
public class NoServerException extends RuntimeException {

    public NoServerException(String message) {
        super(message);
    }
}
