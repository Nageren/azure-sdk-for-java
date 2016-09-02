/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */
package com.microsoft.azure.management.network;

import java.util.Map;

import com.microsoft.azure.management.network.implementation.FrontendIPConfigurationInner;
import com.microsoft.azure.management.resources.fluentcore.arm.models.ChildResource;
import com.microsoft.azure.management.resources.fluentcore.model.Wrapper;

/**
 * An immutable client-side representation of an HTTP load balancing rule.
 */
public interface Frontend extends
    Wrapper<FrontendIPConfigurationInner>,
    ChildResource {

    /**
     * @return true if the front end is Internet-facing, i.e. it has a public IP address associated with it
     */
    boolean isInternetFacing();

    /**
     * @return the inbound NAT pools on this load balancer that use this frontend, indexed by their names
     */
    Map<String, InboundNatPool> inboundNatPools();

    /**
     * @return the inbound NAT rules on this load balancer that use this frontend, indexed by their names
     */
    Map<String, InboundNatRule> inboundNatRules();

    /**
     * @return the load balancing rules on this load balancer that use this frontend, indexed by their names
     */
    Map<String, LoadBalancingRule> loadBalancingRules();
}
