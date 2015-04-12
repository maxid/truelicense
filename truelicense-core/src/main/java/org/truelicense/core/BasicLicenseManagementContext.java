/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */

package org.truelicense.core;

import org.truelicense.api.*;
import org.truelicense.api.auth.Authentication;
import org.truelicense.api.auth.AuthenticationParameters;
import org.truelicense.api.auth.Repository;
import org.truelicense.api.crypto.Encryption;
import org.truelicense.api.crypto.PbeParameters;
import org.truelicense.api.passwd.PasswordPolicy;
import org.truelicense.api.passwd.PasswordProtection;
import org.truelicense.core.auth.Notary;
import org.truelicense.core.passwd.Passwords;
import org.truelicense.obfuscate.ObfuscatedString;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.Immutable;
import java.util.Date;
import java.util.Objects;

/**
 * A basic license management context.
 * <p>
 * Unless stated otherwise, all no-argument methods need to return consistent
 * objects so that caching them is not required.
 * A returned object is considered to be consistent if it compares
 * {@linkplain Object#equals(Object) equal} or at least behaves identical to
 * any previously returned object.
 *
 * @author Christian Schlichtherle
 */
@Immutable
public abstract class BasicLicenseManagementContext
implements LicenseManagementContext<ObfuscatedString> {

    private final String subject;

    protected BasicLicenseManagementContext(final String subject) {
        this.subject = Objects.requireNonNull(subject);
    }

    @Override
    public final LicenseVendorContext<ObfuscatedString> vendor() {
        return new BasicLicenseVendorContext(this);
    }

    @Override
    public final LicenseConsumerContext<ObfuscatedString> consumer() {
        return new BasicLicenseConsumerContext(this);
    }

    /** Returns a <em>new</em> license. */
    @Override
    public abstract License license();

    /**
     * Returns the licensing subject.
     */
    @Override
    public final String subject() { return subject; }

    /**
     * {@inheritDoc}
     * <p>
     * The implementation in the class {@link BasicLicenseManagementContext}
     * returns an authorization which clears all operation requests.
     */
    @Override
    public LicenseAuthorization authorization() {
        return new BasicLicenseAuthorization();
    }

    /**
     * {@inheritDoc}
     * <p>
     * The implementation in the class {@link BasicLicenseManagementContext}
     * returns an initialization which initializes the license
     * {@linkplain License#getConsumerType consumer type},
     * {@linkplain License#getHolder holder},
     * {@linkplain License#getIssued issue date/time},
     * {@linkplain License#getIssuer issuer} and
     * {@linkplain License#getSubject subject}
     * unless these properties are already set respectively.
     */
    @Override
    public LicenseInitialization initialization() {
        return new BasicLicenseInitialization(this);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The implementation in the class {@link BasicLicenseManagementContext}
     * returns a validation which validates the license
     * {@linkplain License#getConsumerAmount consumer amount},
     * {@linkplain License#getConsumerType consumer type},
     * {@linkplain License#getHolder holder},
     * {@linkplain License#getIssued issue date/time},
     * {@linkplain License#getIssuer issuer},
     * {@linkplain License#getSubject subject},
     * {@linkplain License#getNotAfter not after date/time} (if set) and
     * {@linkplain License#getNotBefore not before date/time} (if set).
     */
    @Override
    public LicenseValidation validation() {
        return new BasicLicenseValidation(this);
    }

    /**
     * Returns a <em>new</em> repository to use for
     * {@linkplain #license licenses}.
     */
    @Override
    public abstract Repository repository();

    /**
     * Returns an authentication for the given key store parameters.
     * <p>
     * The implementation in the class {@link BasicLicenseManagementContext}
     * returns a new {@link Notary} for the given parameters.
     *
     * @param parameters the authentication parameters.
     */
    @Override
    public Authentication authentication(AuthenticationParameters parameters) {
        return new Notary(parameters);
    }

    /**
     * Returns an encryption for the given PBE parameters.
     *
     * @param pbe the PBE parameters.
     */
    @Override
    public abstract Encryption encryption(PbeParameters pbe);

    /** Returns a password protection for the given obfuscated string. */
    @Override
    public PasswordProtection protection(ObfuscatedString os) {
        return Passwords.newPasswordProtection(os);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The implementation in the class {@link BasicLicenseManagementContext}
     * returns {@link Passwords#newPasswordPolicy()}.
     */
    @Override
    public PasswordPolicy policy() { return Passwords.newPasswordPolicy(); }

    /**
     * {@inheritDoc}
     * <p>
     * The implementation in the class {@link BasicLicenseManagementContext}
     * returns a new {@link Date}.
     */
    @Override
    public Date now() { return new Date(); }

    /**
     * {@inheritDoc}
     * <p>
     * The implementation in the class {@link BasicLicenseManagementContext}
     * returns the current thread's context class loader.
     */
    @Override
    public @CheckForNull ClassLoader classLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * {@inheritDoc}
     * <p>
     * The implementation in the class {@link BasicLicenseManagementContext}
     * returns half an hour (in milliseconds) to account for external changes
     * to the configured store for the license key.
     */
    @Override
    public long cachePeriodMillis() { return 30 * 60 * 1000; }
}