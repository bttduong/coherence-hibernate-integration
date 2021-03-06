/*
 * File: CoherenceNaturalIdRegion.java
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * The contents of this file are subject to the terms and conditions of
 * the Common Development and Distribution License 1.0 (the "License").
 *
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License by consulting the LICENSE.txt file
 * distributed with this file, or by consulting https://oss.oracle.com/licenses/CDDL
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file LICENSE.txt.
 *
 * MODIFICATIONS:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 */

package com.oracle.coherence.hibernate.cache.region;

import com.oracle.coherence.hibernate.cache.access.CoherenceRegionAccessStrategy;
import com.oracle.coherence.hibernate.cache.access.NaturalIdNonstrictReadWriteCoherenceRegionAccessStrategy;
import com.oracle.coherence.hibernate.cache.access.NaturalIdReadWriteCoherenceRegionAccessStrategy;
import com.oracle.coherence.hibernate.cache.access.NaturalIdReadOnlyCoherenceRegionAccessStrategy;
import com.tangosol.net.NamedCache;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cfg.Settings;

import java.util.Properties;

/**
 * A CoherenceNaturalIdRegion is a CoherenceTransactionalDataRegion intended to cache Hibernate natural IDs.
 *
 * @author Randy Stafford
 */
public class CoherenceNaturalIdRegion
extends CoherenceTransactionalDataRegion
implements NaturalIdRegion
{


    // ---- Constructors

    /**
     * Complete constructor.
     *
     * @param namedCache the NamedCache implementing this CoherenceNaturalIdRegion
     * @param settings the Hibernate settings object
     * @param properties configuration properties for this CoherenceNaturalIdRegion
     * @param metadata the description of the data in this CoherenceNaturalIdRegion
     */
    public CoherenceNaturalIdRegion(NamedCache namedCache, Settings settings, Properties properties, CacheDataDescription metadata)
    {
        super(namedCache, settings, properties, metadata);
    }


    // ---- interface org.hibernate.cache.spi.NaturalIdRegion

    /**
     * {@inheritDoc}
     */
    @Override
    public NaturalIdRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException
    {
        debugf("%s.buildAccessStrategy(%s)", this, accessType);
        switch (accessType)
        {
            case NONSTRICT_READ_WRITE :
                return new NaturalIdNonstrictReadWriteCoherenceRegionAccessStrategy(this, getSettings());
            case READ_ONLY :
                return new NaturalIdReadOnlyCoherenceRegionAccessStrategy(this, getSettings());
            case READ_WRITE :
                return new NaturalIdReadWriteCoherenceRegionAccessStrategy(this, getSettings());
            case TRANSACTIONAL :
                throw new CacheException(CoherenceRegionAccessStrategy.TRANSACTIONAL_STRATEGY_NOT_SUPPORTED_MESSAGE);
            default :
                throw new CacheException("Unknown AccessType: " + accessType);
        }
    }


}
