/*
 * Copyright (C) 2005-2017 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */

package org.truelicense.api;

import org.truelicense.api.io.Store;
import org.truelicense.api.misc.Builder;
import org.truelicense.api.misc.Injection;

import java.nio.file.Path;

/**
 * A builder for {@linkplain ConsumerLicenseManager consumer license managers}.
 * Call its {@link #build} method to obtain the configured license consumer
 * manager.
 * <p>
 * Clients should not implement this interface because it's subject to expansion
 * in future minor version updates.
 *
 * @author Christian Schlichtherle
 */
public interface ConsumerLicenseManagerBuilder
extends Builder<ConsumerLicenseManager>,
        Injection<ConsumerLicenseManagerBuilder>,
        LicenseManagerBuilder<ConsumerLicenseManagerBuilder> {

    /**
     * Sets the free trial period (FTP) in days (the 24 hour equivalent).
     * If this is zero, then no FTP is configured.
     * Otherwise, the {@linkplain #authentication key store} needs to have a
     * password configured for the private key entry and a
     * {@linkplain #parent parent consumer license manager}
     * needs to be configured for the regular license keys.
     *
     * @return {@code this}.
     */
    ConsumerLicenseManagerBuilder ftpDays(int ftpDays);

    /**
     * Returns a builder for the parent consumer license manager.
     * Call its {@link ConsumerLicenseManagerBuilder#inject} method to build and inject
     * the configured parent consumer license manager into this builder and
     * return it.
     * <p>
     * A parent consumer license manager is required to configure a
     * non-zero {@linkplain #ftpDays free trial period} (FTP).
     * The parent consumer license manager will be tried first whenever a
     * {@linkplain ConsumerLicenseManager life cycle management method}
     * is executed, e.g. when verifying a license key.
     *
     * @see #parent(ConsumerLicenseManager)
     */
    ConsumerLicenseManagerBuilder parent();

    /**
     * Sets the parent consumer license manager.
     * A parent consumer license manager is required to configure a
     * non-zero {@linkplain #ftpDays free trial period} (FTP).
     * The parent consumer license manager will be tried first whenever a
     * {@linkplain ConsumerLicenseManager life cycle management method}
     * is executed, e.g. when verifying a license key.
     *
     * @return {@code this}.
     */
    ConsumerLicenseManagerBuilder parent(ConsumerLicenseManager parent);

    /**
     * Store the license key in the given store.
     * If a non-zero {@linkplain #ftpDays free trial period} (FTP) is
     * configured, then the store will be used for the auto-generated FTP
     * license key and MUST BE KEPT SECRET!
     *
     * @return {@code this}.
     */
    ConsumerLicenseManagerBuilder storeIn(Store store);

    /**
     * Store the license key in the given path.
     * If a non-zero {@linkplain #ftpDays free trial period} (FTP) is
     * configured, then the store will be used for the auto-generated FTP
     * license key and MUST BE KEPT SECRET!
     *
     * @return {@code this}.
     */
    ConsumerLicenseManagerBuilder storeInPath(Path path);

    /**
     * Store the license key in the system preferences node for the package
     * of the given class.
     * If a non-zero {@linkplain #ftpDays free trial period} (FTP) is
     * configured, then the store will be used for the auto-generated FTP
     * license key and MUST BE KEPT SECRET!
     *
     * @return {@code this}.
     */
    ConsumerLicenseManagerBuilder storeInSystemPreferences(Class<?> classInPackage);

    /**
     * Store the license key in the user preferences node for the package
     * of the given class.
     * If a non-zero {@linkplain #ftpDays free trial period} (FTP) is
     * configured, then the store will be used for the auto-generated FTP
     * license key and MUST BE KEPT SECRET!
     *
     * @return {@code this}.
     */
    ConsumerLicenseManagerBuilder storeInUserPreferences(Class<?> classInPackage);
}
